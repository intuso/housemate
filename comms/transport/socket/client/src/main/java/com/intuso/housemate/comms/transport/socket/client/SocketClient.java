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
    public final static String KEEP_OPEN = "client.socket.keepOpen";
    public final static String CHECK_IN = "client.socket.checkIn";

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

    private final List<ListenerRegistration> listenerRegistrations = Lists.newArrayList();
    private ApplicationDetails applicationDetails = null;
    private boolean networkAvailable = true;

    /**
     * Create a new client comms
     * @throws HousemateException
     */
    @Inject
    public SocketClient(Log log, ListenersFactory listenersFactory, PropertyRepository properties, StreamSerialiserFactory serialiserFactory) {
        super(log, listenersFactory, properties);
        this.properties = properties;
        this.serialiserFactory = serialiserFactory;
        // create the queues and threads
        outputQueue = new LinkedBlockingQueue<Message>();
    }

    @Override
    public synchronized final void connect() {

        if (shouldBeConnected)
            return;
        shouldBeConnected = true;

        listenerRegistrations.add(properties.addListener(HOST, this));
        listenerRegistrations.add(properties.addListener(PORT, this));
        listenerRegistrations.add(properties.addListener(KEEP_OPEN, this));
        listenerRegistrations.add(properties.addListener(CHECK_IN, this));

        _ensureConnected();
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
        super.register(applicationDetails);
    }

    @Override
    public void unregister() {
        this.applicationDetails = null;
        super.unregister();
    }

    public synchronized void _ensureConnected() {
        if(connectThread != null || socket != null)
            return;
        if(activityMonitor == null) {
            activityMonitor = new ActivityMonitor();
            propertyValueChanged(KEEP_OPEN, null, properties.get(KEEP_OPEN));
            propertyValueChanged(CHECK_IN, null, properties.get(CHECK_IN));
            activityMonitor.start();
        }
        connectThread = new ConnectThread();
        connectThread.start();
    }

    @Override
    public synchronized final void disconnect() {

        if(!shouldBeConnected)
            return;
        shouldBeConnected = false;

        for(ListenerRegistration listenerRegistration : listenerRegistrations)
            listenerRegistration.removeListener();
        listenerRegistrations.clear();

        _disconnect();
    }

    private final void _disconnect() {
        _disconnect(true);
    }

    /**
     * Shutdown the comms connection
     */
    private synchronized final void _disconnect(boolean reconnect) {

        if(connectThread == null && socket == null)
            return;

        getLog().d("Disconnecting from the server");

        if(connectThread != null) {
            connectThread.interrupt();
            connectThread = null;
        }

        if(socket != null) {
            try {
                socket.close();
            } catch(IOException e) {
                if(!socket.isClosed())
                    getLog().e("Error closing client connection to server");
            }
            socket = null;
        }

        getLog().d("Interrupting all reader/writer threads");
        if(streamReader != null) {
            streamReader.interrupt();
            streamReader = null;
        }
        if(messageSender != null) {
            messageSender.interrupt();
            messageSender = null;
        }

        getLog().d("Disconnected");

        // set the connection status, and check if we should reconnect
        if(shouldBeConnected) {
            setServerConnectionStatus(ServerConnectionStatus.DisconnectedTemporarily);
            if(reconnect)
                _ensureConnected();
        } else {
            setServerConnectionStatus(ServerConnectionStatus.DisconnectedPermanently);
            getLog().d("Should not be connected, leaving disconnected");
            if(activityMonitor != null)
                activityMonitor.interrupt();
        }
    }

    @Override
    public void sendMessage(Message message) {
        _ensureConnected();
        outputQueue.add(message);
    }

    @Override
    public void propertyValueChanged(String key, String oldValue, String newValue) {
        if(key.equals(HOST) || key.equals(PORT)) {
            // in a new thread so it doesn't block the caller
            new Thread() {
                @Override
                public void run() {
                    _disconnect();
                }
            }.start();
        } else if(activityMonitor != null) {
            if(key.equals(CHECK_IN) && newValue != null) {
                try {
                    activityMonitor.setCheckInTimeout(Long.parseLong(properties.get(SocketClient.CHECK_IN)));
                } catch (NumberFormatException e) {
                    getLog().e("Failed to parse check in timeout " + properties.get(CHECK_IN));
                }
            } else if(key.equals(KEEP_OPEN) && newValue != null) {
                try {
                    activityMonitor.setInactivityTimeout(Long.parseLong(properties.get(SocketClient.KEEP_OPEN)));
                } catch (NumberFormatException e) {
                    getLog().e("Failed to parse check in timeout " + properties.get(KEEP_OPEN));
                }
            }
        }
    }

    public void networkAvailable(boolean networkAvailable) {
        this.networkAvailable = networkAvailable;
        // check if we should disconnect, activity monitor will take care of reconnecting
        if(!networkAvailable)
            _disconnect(false);
    }

    private class ConnectThread extends Thread {
        @Override
        public void run() {
            if(getServerConnectionStatus() != ServerConnectionStatus.DisconnectedTemporarily)
                setServerConnectionStatus(ServerConnectionStatus.Connecting);
            String host = properties.get(HOST);
            int port = Integer.parseInt(properties.get(PORT));

            // log for debug info
            getLog().d("Server host is \"" + host + "\"");
            getLog().d("Server port is \"" + port + "\"");
            int delay = 1;
            while(!isInterrupted() && (socket == null || !socket.isConnected())) {
                try {
                    getLog().d("Attempting to connect");

                    // create the socket and the streams. Create then connect, so that socket is assigned before
                    // the connection is attempted.
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(host, port));
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(60000);

                    getLog().d("Connected to server, writing details and creating reader/writer threads");
                    writeDetails();
                    checkResponse();
                    serialiser = serialiserFactory.create(socket.getOutputStream(), socket.getInputStream());

                    if(activityMonitor != null)
                        activityMonitor.somethingHappened();

                    messageSender = new MessageSender();
                    streamReader = new StreamReader();

                    if(applicationDetails != null) {
                        getLog().d("Re-registering");
                        setServerConnectionStatus(ServerConnectionStatus.ConnectedToServer);
                        serialiser.write(new Message<ApplicationRegistration>(ConnectionManager.ROOT_PATH, Root.APPLICATION_REGISTRATION_TYPE,
                                new ApplicationRegistration(applicationDetails, properties.get(ConnectionManager.APPLICATION_INSTANCE_ID), ClientType.Router)));
                    } else
                        setServerConnectionStatus(ServerConnectionStatus.ConnectedToRouter);

                    // start the threads
                    streamReader.start();
                    messageSender.start();

                    getLog().d("Connected to server");

                    // return from the method
                    return;

                } catch (UnknownHostException e) {
                    getLog().e("Server host \"" + host + ":" + port + "\" is unknown, cannot connect");
                } catch (IOException e) {
                    getLog().e("Error connecting to server: " + e.getMessage(), e);
                } catch(HousemateException e) {
                    getLog().e("Error initiating connection to router", e);
                }

                getLog().e("Failed to connect to server. Retrying in " + delay + " seconds");
                try {
                    Thread.sleep(delay * 1000);
                } catch(InterruptedException e) {
                    getLog().e("Interrupted waiting to retry connection. Aborting trying to connect");
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
                throw new HousemateException("Failed to write client details", e);
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
                getLog().d("Read handshake response from server: " + data);
                if(!data.toString().equals("Success"))
                    throw new HousemateException("Bad handshake response from server: " + data);
            } catch(InterruptedException e) {
                throw new HousemateException("Error reading handshake response from server", e);
            } catch(IOException e) {
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

            getLog().d("Starting stream reader");

            // read continuously from it
            try {
                while(!isInterrupted()) {
                    Message message = this.readMessage();
                    if(message == null)
                        return;
                    else if(!(message.getPath().length == 0 && message.getType().equals("heartbeat"))) {
                        getLog().d("Message received " + message);
                        if(activityMonitor != null)
                            activityMonitor.somethingHappened();
                        messageReceived(message);
                    }
                }
            } catch (HousemateException e) {
                getLog().e("Error reading from server socket connection", e);
                _disconnect();
            }

            getLog().d("Stopped stream reader");
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
                    getLog().e("Problem reading message, retrying", e);
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
        private Message heartbeat = new Message<NoPayload>(new String[] {}, "heartbeat", NoPayload.INSTANCE);

        @Override
        public void run() {

            getLog().d("Starting the message sender");

            while(!isInterrupted()) {

                Message message;

                try {
                    // get the next message
                    message = outputQueue.poll(30, TimeUnit.SECONDS);
                    if(message == null) {
                        message = heartbeat;
                    } else
                        getLog().d("Sending message " + message.toString());
                    serialiser.write(message);
                } catch(InterruptedException e) {
                    if(socket == null || socket.isClosed())
                        break;
                    getLog().d("Interrupted waiting for message to send. Trying again");
                } catch(IOException e) {
                    getLog().e("Error sending message to client", e);
                    _disconnect();
                    break;
                }
            }

            getLog().d("Stopped message sender");
        }
    }

    private class ActivityMonitor extends Thread {

        private long checkInTimeout = 0L;
        private long inactivityTimeout = Long.MAX_VALUE;

        private long lastActivity = System.currentTimeMillis();
        private long inactiveAt = Long.MAX_VALUE;

        public void setCheckInTimeout(long checkInTimeout) {
            this.checkInTimeout = checkInTimeout;
        }

        public void setInactivityTimeout(long inactivityTimeout) {
            this.inactivityTimeout = inactivityTimeout;
        }

        public void somethingHappened() {
            lastActivity = System.currentTimeMillis();
        }

        @Override
        public void run() {
            while(!isInterrupted()) {
                if(networkAvailable) {
                    // if not connected and check in timeout reached
                    if (getServerConnectionStatus() != ServerConnectionStatus.ConnectedToServer && getServerConnectionStatus() != ServerConnectionStatus.ConnectedToRouter) {
                        long disconnectedFor = (System.currentTimeMillis() - inactiveAt);
                        if (disconnectedFor > checkInTimeout) {
                            getLog().d("Check in timeout reached, ensuring connected");
                            _ensureConnected();
                        }
                        // else if connecting/ed and inactivity timeout reached, then disconnect
                    } else {
                        long inactiveFor = (System.currentTimeMillis() - lastActivity);
                        if (inactiveFor > inactivityTimeout) {
                            getLog().d("Inactivity timeout reached. Disconnecting");
                            _disconnect(false);
                            inactiveAt = System.currentTimeMillis();
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