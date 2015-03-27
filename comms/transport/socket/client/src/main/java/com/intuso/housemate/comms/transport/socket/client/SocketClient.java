package com.intuso.housemate.comms.transport.socket.client;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.comms.serialiser.api.Serialiser;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.PropertyValueChangeListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Client comms implementation that provides socket-based communications with the server
 *
 */
public class SocketClient extends Router implements PropertyValueChangeListener {

    public final static String HOST = "client.socket.host";
    public final static String PORT = "client.socket.port";
    public final static String ACTIVITY_DELAY = "client.socket.activity-delay";
    public final static String MAX_INACTIVE_TIME = "client.socket.max-inactive-time";

    private final PropertyRepository properties;
    private final StreamSerialiserFactory serialiserFactory;

    private Socket socket;
    private ConnectThread connectThread;
    private ActivityMonitor activityMonitor;

    private final LinkedBlockingQueue<Message> outputQueue;
    private Serialiser serialiser;
    private StreamReader streamReader;
    private MessageSender messageSender;

    private boolean shouldBeConnected = false;
    private boolean networkAvailable = true;
    private boolean active = true;

    private final List<ListenerRegistration> listenerRegistrations = Lists.newArrayList();
    private ApplicationDetails applicationDetails = null;

    /**
     * Create a new client comms
     */
    @Inject
    public SocketClient(Log log, ListenersFactory listenersFactory, PropertyRepository properties, StreamSerialiserFactory serialiserFactory) {
        super(log, listenersFactory, properties);
        this.properties = properties;
        this.serialiserFactory = serialiserFactory;
        // create the queues and threads
        outputQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public synchronized final void connect() {

        getLog().d("Socket Client: connect()");

        if (shouldBeConnected)
            return;
        shouldBeConnected = true;

        listenerRegistrations.add(properties.addListener(HOST, this));
        listenerRegistrations.add(properties.addListener(PORT, this));
        listenerRegistrations.add(properties.addListener(ACTIVITY_DELAY, this));
        listenerRegistrations.add(properties.addListener(MAX_INACTIVE_TIME, this));

        activityMonitor = new ActivityMonitor();
        propertyValueChanged(ACTIVITY_DELAY, null, properties.get(ACTIVITY_DELAY));
        propertyValueChanged(MAX_INACTIVE_TIME, null, properties.get(MAX_INACTIVE_TIME));
        activityMonitor.start();

        checkConnection();
    }

    @Override
    public synchronized final void disconnect() {

        getLog().d("Socket Client: disconnect()");

        if(!shouldBeConnected)
            return;
        shouldBeConnected = false;

        for(ListenerRegistration listenerRegistration : listenerRegistrations)
            listenerRegistration.removeListener();
        listenerRegistrations.clear();

        if(activityMonitor != null) {
            activityMonitor.interrupt();
            activityMonitor = null;
        }

        checkConnection();
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {

        getLog().d("Socket Client: register()");

        this.applicationDetails = applicationDetails;
        super.register(applicationDetails);
    }

    @Override
    public void unregister() {

        getLog().d("Socket Client: unregister()");

        this.applicationDetails = null;
        super.unregister();
    }

    @Override
    public void sendMessage(Message message) {
        active = true;
        activityMonitor.somethingHappened();
        checkConnection();
        outputQueue.add(message);
    }

    @Override
    public void propertyValueChanged(String key, String oldValue, String newValue) {
        if(key.equals(HOST) || key.equals(PORT)) {
            getLog().d("Socket Client: host/port changed");
            _disconnect();
            checkConnection();
        } else if(activityMonitor != null) {
            if(key.equals(MAX_INACTIVE_TIME) && newValue != null) {
                try {
                    Long newMaxInactiveTime = Long.parseLong(properties.get(SocketClient.MAX_INACTIVE_TIME));
                    getLog().d("Socket Client: " + MAX_INACTIVE_TIME + " changed to " + newMaxInactiveTime);
                    activityMonitor.setMaxInactiveTime(newMaxInactiveTime);
                } catch (NumberFormatException e) {
                    getLog().e("Socket Client: Failed to parse " + MAX_INACTIVE_TIME + " new value " + properties.get(MAX_INACTIVE_TIME));
                }
            } else if(key.equals(ACTIVITY_DELAY) && newValue != null) {
                try {
                    Long newActivityDelay = Long.parseLong(properties.get(SocketClient.ACTIVITY_DELAY));
                    getLog().d("Socket Client: " + ACTIVITY_DELAY + " changed to " + newActivityDelay);
                    activityMonitor.setActivityDelay(newActivityDelay);
                } catch (NumberFormatException e) {
                    getLog().e("Socket Client: Failed to parse " + ACTIVITY_DELAY + " new value " + properties.get(ACTIVITY_DELAY));
                }
            }
        }
    }

    public void networkAvailable(boolean networkAvailable) {
        getLog().d("Socket Client: networkAvailable(" + networkAvailable + ")");
        this.networkAvailable = networkAvailable;
        checkConnection();
    }

    public synchronized void checkConnection() {
        if(shouldBeConnected && networkAvailable && active)
            _ensureConnected();
        else
            _disconnect();
    }

    public synchronized void _ensureConnected() {

        if(connectThread != null || socket != null)
            return;

        connectThread = new ConnectThread();
        connectThread.start();
    }

    /**
     * Shutdown the comms connection
     */
    private synchronized void _disconnect() {

        if(connectThread == null && socket == null)
            return;

        getLog().d("Socket Client: Disconnecting from the server");

        if(connectThread != null) {
            connectThread.interrupt();
            connectThread = null;
        }

        if(socket != null) {
            try {
                socket.close();
            } catch(IOException e) {
                if(!socket.isClosed())
                    getLog().e("Socket Client: Error closing client connection to server", e);
            }
            socket = null;
        }

        getLog().d("Socket Client: Interrupting all reader/writer threads");
        if(streamReader != null) {
            streamReader.interrupt();
            streamReader = null;
        }
        if(messageSender != null) {
            messageSender.interrupt();
            messageSender = null;
        }

        getLog().d("Disconnected");

        // set the connection status
        setServerConnectionStatus(shouldBeConnected ? ServerConnectionStatus.DisconnectedTemporarily : ServerConnectionStatus.DisconnectedPermanently);
    }

    private class ConnectThread extends Thread {
        @Override
        public void run() {
            if(getServerConnectionStatus() != ServerConnectionStatus.DisconnectedTemporarily)
                setServerConnectionStatus(ServerConnectionStatus.Connecting);
            String host = properties.get(HOST);
            int port = Integer.parseInt(properties.get(PORT));

            // log for debug info
            getLog().d("Socket Client: Server host:port is " + host + ":" + port);

            int delay = 1;
            while(!isInterrupted() && (socket == null || !socket.isConnected())) {
                try {
                    getLog().d("Socket Client: Attempting to connect");

                    // create the socket and the streams. Create then connect, so that socket is assigned before
                    // the connection is attempted.
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(host, port));
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(60000);

                    getLog().d("Socket Client: Connected to server, writing details and creating reader/writer threads");
                    writeDetails();
                    checkResponse();
                    serialiser = serialiserFactory.create(socket.getOutputStream(), socket.getInputStream());

                    if(activityMonitor != null)
                        activityMonitor.somethingHappened();

                    messageSender = new MessageSender();
                    streamReader = new StreamReader();

                    if(applicationDetails != null) {
                        getLog().d("Socket Client: Re-registering");
                        serialiser.write(new Message<>(ConnectionManager.ROOT_PATH, Root.APPLICATION_REGISTRATION_TYPE,
                                new ApplicationRegistration(applicationDetails, properties.get(ConnectionManager.APPLICATION_INSTANCE_ID), ClientType.Router)));
                        setServerConnectionStatus(ServerConnectionStatus.ConnectedToServer);
                    } else
                        setServerConnectionStatus(ServerConnectionStatus.ConnectedToRouter);

                    // start the threads
                    streamReader.start();
                    messageSender.start();

                    getLog().d("Socket Client: Connected to server");

                    // return from the method
                    return;

                } catch (UnknownHostException e) {
                    getLog().e("Socket Client: Server host \"" + host + ":" + port + "\" is unknown, cannot connect");
                } catch (IOException e) {
                    getLog().e("Socket Client: Error connecting to server: " + e.getMessage(), e);
                } catch(HousemateException e) {
                    getLog().e("Socket Client: Error initiating connection to router", e);
                }

                getLog().e("Socket Client: Failed to connect to server. Retrying in " + delay + " seconds");
                try {
                    Thread.sleep(delay * 1000);
                } catch(InterruptedException e) {
                    getLog().e("Socket Client: Interrupted waiting to retry connection. Aborting trying to connect");
                    _disconnect();
                    return;
                }
                delay = Math.min(60, delay * 2);
            }
        }

        private void writeDetails() throws HousemateException {
            try {
                socket.getOutputStream().write((Serialiser.DETAILS_KEY + "=" + serialiserFactory.getType() + "\n").getBytes());
                socket.getOutputStream().write("\n".getBytes()); // blank line indicates details are finished
                socket.getOutputStream().flush();
            } catch (IOException e) {
                throw new HousemateException("Socket Client: Failed to write client details", e);
            }
        }

        private void checkResponse() throws HousemateException {
            try {
                StringBuilder data = new StringBuilder();
                InputStream in = socket.getInputStream();
                int i;
                while(true) {
                    if(in.available() == 0)
                        Thread.sleep(100);
                    else {
                        i = in.read();
                        if(i == '\n')
                            break;
                        else if(i >= 0)
                            data.append((char)i);
                    }
                }
                getLog().d("Socket Client: Read handshake response from server: " + data);
                if(!data.toString().equals("Success"))
                    throw new HousemateException("Bad handshake response from server: " + data);
            } catch(IOException|InterruptedException e) {
                throw new HousemateException("Error reading handshake response from server", e);
            }
        }
    }

    /**
     * Thread to read data off the socket and convert it into message objects
     * @author Tom Clabon
     *
     */
    private class StreamReader extends Thread {

        @Override
        public void run() {

            getLog().d("Socket Client: Starting stream reader");

            // read continuously from it
            try {
                while(!isInterrupted()) {
                    Message message = this.readMessage();
                    if(message == null)
                        return;
                    else if(!(message.getPath().length == 0 && message.getType().equals("heartbeat"))) {
                        getLog().d("Socket Client: Message received " + message);
                        if(activityMonitor != null)
                            activityMonitor.somethingHappened();
                        messageReceived(message);
                    }
                }
            } catch (HousemateException e) {
                getLog().e("Socket Client: Error reading from server socket connection", e);
                _disconnect();
                checkConnection();
            }

            getLog().d("Socket Client: Stopped stream reader");
        }

        /**
         * Read the next message
         * @return the next message
         * @throws HousemateException malformed message
         */
        private Message readMessage() throws HousemateException {
            while(true) {
                try {
                    return serialiser.read();
                } catch(HousemateException e) {
                    getLog().e("Socket Client: Problem reading message, retrying", e);
                } catch(SocketException e) {
                    if(e.getMessage().equals("Socket closed"))
                        return null;
                    throw new HousemateException("Could not read message", e);
                } catch(IOException e) {
                    throw new HousemateException("Could not read message", e);
                } catch (InterruptedException e) {
                    throw new HousemateException("Interrupted waiting to receive message", e);
                }
            }
        }
    }

    /**
     * Thread to read messages off the output queue and send them to the server
     * @author Tom Clabon
     *
     */
    private class MessageSender extends Thread {

        /**
         * heartbeat messgae
         */
        private Message heartbeat = new Message<>(new String[] {}, "heartbeat", NoPayload.INSTANCE);

        @Override
        public void run() {

            getLog().d("Socket Client: Starting the message sender");

            while(!isInterrupted()) {

                Message message;

                try {
                    // get the next message
                    message = outputQueue.poll(30, TimeUnit.SECONDS);
                    if(message == null) {
                        message = heartbeat;
                    } else
                        getLog().d("Socket Client: Sending message " + message.toString());
                    serialiser.write(message);
                } catch(InterruptedException e) {
                    if(socket == null || socket.isClosed())
                        break;
                    getLog().d("Socket Client: Interrupted waiting for message to send");
                } catch(IOException e) {
                    getLog().e("Socket Client: Error sending message to client", e);
                    _disconnect();
                    checkConnection();
                    break;
                }
            }

            getLog().d("Socket Client: Stopped message sender");
        }
    }

    private class ActivityMonitor extends Thread {

        private long maxInactiveTime = 0L; // by default, don't have any inactive time -> reconnects immediately
        private long activityDelay = Long.MAX_VALUE; // by default, wait forever after something happened -> never gets inactive

        private long lastActivity = System.currentTimeMillis();
        private long inactiveAt = 0L;

        public void setMaxInactiveTime(long maxInactiveTime) {
            this.maxInactiveTime = maxInactiveTime;
        }

        public void setActivityDelay(long activityDelay) {
            this.activityDelay = activityDelay;
        }

        public void somethingHappened() {
            lastActivity = System.currentTimeMillis();
        }

        @Override
        public void run() {
            while(!isInterrupted()) {

                // don't do the work unless it's worth it
                if(shouldBeConnected && networkAvailable) {

                    // if currently active
                    if (active) {

                        // see if the activity delay has been reached
                        long inactiveFor = (System.currentTimeMillis() - lastActivity);
                        if (inactiveFor > activityDelay) {
                            getLog().d("Socket Client: Activity delay reached. Setting active to false");
                            inactiveAt = System.currentTimeMillis();
                            active = false;
                            checkConnection();
                        }

                    // else currently inactive
                    } else {

                        // see if the check in timeout has been reached
                        long disconnectedFor = (System.currentTimeMillis() - inactiveAt);
                        if (disconnectedFor > maxInactiveTime) {
                            getLog().d("Socket Client: Max inactive time reached. Setting active to true");
                            lastActivity = System.currentTimeMillis();
                            active = true;
                            checkConnection();
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {} // don't worry, loop will break and thread will stop
            }
        }
    }
}