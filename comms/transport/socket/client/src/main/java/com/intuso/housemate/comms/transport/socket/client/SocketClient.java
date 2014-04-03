package com.intuso.housemate.comms.transport.socket.client;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.comms.serialiser.api.Serialiser;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.PropertyValueChangeListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Client comms implementation that provides socket-based communications with the server
 *
 */
public class SocketClient extends Router implements PropertyValueChangeListener {

    public final static String SERVER_PORT = "socket.server.port";
    public final static String SERVER_HOST = "socket.server.host";

    private final PropertyRepository properties;
    private final StreamSerialiserFactory serialiserFactory;

    private Socket socket;
    private final Object connectThreadLock = new Object();
    private ConnectThread connectThread;

    private final LinkedBlockingQueue<Message> outputQueue;
    private Serialiser serialiser;
    private StreamReader streamReader;
    private MessageSender messageSender;

    private final List<ListenerRegistration> listenerRegistrations = Lists.newArrayList();

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
    public final void disconnect() {
        for(ListenerRegistration listenerRegistration : listenerRegistrations)
            listenerRegistration.removeListener();
        listenerRegistrations.clear();
        disconnect(false);
    }

    private void disconnect(boolean reconnect) {
        synchronized (connectThreadLock) {
            if(connectThread != null) {
                connectThread.interrupt();
                try {
                    connectThread.join();
                } catch(InterruptedException e) {}
            }
            shutdownComms();
            if(reconnect)
                connect();
        }
    }

    /**
     * Connect to the server
     */
    @Override
    public final void connect() {
        if(listenerRegistrations.size() == 0) {
            listenerRegistrations.add(properties.addListener(SERVER_HOST, this));
            listenerRegistrations.add(properties.addListener(SERVER_PORT, this));
        }
        synchronized (connectThreadLock) {
            if(connectThread == null) {
                connectThread = new ConnectThread();
                connectThread.start();
            }
        }
    }

    /**
     * Shutdown the comms connection
     */
    private void shutdownComms() {
        synchronized (connectThreadLock) {
            getLog().d("Disconnecting from the server");
            setServerConnectionStatus(ServerConnectionStatus.Disconnected);
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
            if(streamReader != null)
                streamReader.interrupt();
            if(messageSender != null)
                messageSender.interrupt();

            try {
                getLog().d("Joining all reader/writer threads");
                if(streamReader != null)
                    streamReader.join();
                if(messageSender != null)
                    messageSender.join();
            } catch(InterruptedException e) {
                getLog().e("Interrupted while waiting for reader/writer threads to stop");
            }

            getLog().d("Disconnected");
        }
    }

    @Override
    public void sendMessage(Message message) {
        outputQueue.add(message);
    }

    private void checkProps() {
        if(properties.get(SocketClient.SERVER_HOST) != null
                && properties.get(SocketClient.SERVER_PORT) != null)
            new Thread() {
                @Override
                public void run() {
                    disconnect(true);
                }
            }.start();
    }

    @Override
    public void propertyValueChanged(String key, String oldValue, String newValue) {
        checkProps();
    }

    private class ConnectThread extends Thread {
        @Override
        public void run() {
            setServerConnectionStatus(ServerConnectionStatus.Connecting);
            String host = properties.get(SERVER_HOST);
            int port = Integer.parseInt(properties.get(SERVER_PORT));

            // log for debug info
            getLog().d("Server host is \"" + host + "\"");
            getLog().d("Server port is \"" + port + "\"");
            int delay = 1;
            while(socket == null || !socket.isConnected()) {
                try {
                    getLog().d("Attempting to connect");

                    // create the socket and the streams
                    socket = new Socket(host, port);
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(60000);

                    getLog().d("Connected to server, writing details and creating reader/writer threads");
                    writeDetails();
                    checkResponse();
                    serialiser = serialiserFactory.create(socket.getOutputStream(), socket.getInputStream());
                    setServerConnectionStatus(ServerConnectionStatus.ConnectedToRouter);
                    messageSender = new MessageSender();
                    streamReader = new StreamReader();

                    // start the threads
                    streamReader.start();
                    messageSender.start();

                    getLog().d("Connected to server");

                    // return from the method
                    synchronized (connectThreadLock) {
                        connectThread = null;
                    }
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
                    synchronized (connectThreadLock) {
                        connectThread = null;
                    }
                    return;
                }
                delay = Math.min(60, delay * 2);
            }
        }
    }

    private void writeDetails() throws HousemateException {
        try {
            socket.getOutputStream().write((Serialiser.DETAILS_KEY + ":" + serialiserFactory.getType() + "\n").getBytes());
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
                    if(!(message.getPath().length == 0 && message.getType().equals("heartbeat"))) {
                        getLog().d("Message received " + message);
                        messageReceived(message);
                    }
                }
            } catch (HousemateException e) {
                getLog().e("Error reading from server socket connection", e);
                disconnect(true);
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
                    getLog().e("Error sending message to client. Will attempt to reconnect", e);
                    disconnect(true);
                    break;
                }
            }

            getLog().d("Stopped message sender");
        }
    }
}