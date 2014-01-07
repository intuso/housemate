package com.intuso.housemate.comms.transport.socket.client;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Client comms implementation that provides socket-based communications with the server
 *
 */
public class SocketClient extends Router {

    public final static String SERVER_PORT = "socket.server.port";
    public final static String SERVER_HOST = "socket.server.host";

    private PropertyContainer properties;

    /**
     * The socket to send/receive over
     */
    private Socket socket;

    /**
     * The queue that messages to send are put on
     */
    private LinkedBlockingQueue<Message> outputQueue;

    private final Object connectThreadLock = new Object();
    private ConnectThread connectThread;

    /**
     * thread that reads the stream and creates copies of messages that were sent by the server
     */
    private StreamReader streamReader;

    /**
     * thread that takes messages off the output queue and writes them to the socket
     */
    private MessageSender messageSender;

    /**
     * Create a new client comms
     * @throws HousemateException
     */
    @Inject
    public SocketClient(Log log, PropertyContainer properties) {
        super(log);
        this.properties = properties;
        // create the queues and threads
        outputQueue = new LinkedBlockingQueue<Message>();
    }

    @Override
    public final void disconnect() {
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
            shutdownComms(reconnect);
//            if(reconnect)
//                connect();
        }
    }

    /**
     * Connect to the server
     */
    @Override
    public final void connect() {
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
    private void shutdownComms(boolean reconnecting) {
        synchronized (connectThreadLock) {
            getLog().d("Disconnecting from the server");
            setRouterStatus(Status.Disconnected);
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

    private class ConnectThread extends Thread {
        @Override
        public void run() {
            setRouterStatus(Status.Connecting);
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

                    getLog().d("Connected to server, creating reader/writer threads");
                    setRouterStatus(Status.ConnectedToRouter);
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
                } catch (HousemateException e) {
                    getLog().e("Error connecting to server: " + e.getMessage(), e);
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

    /**
     * Thread to read data off the socket and convert it into message objects
     * @author Tom Clabon
     *
     */
    private class StreamReader extends Thread {

        private ObjectInputStream ois;

        /**
         * Create a new stream reader
         * @throws IOException
         */
        public StreamReader() throws HousemateException {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
            } catch(IOException e) {
                throw new HousemateException("Cannot create object reader", e);
            }
        }

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
                    Object next = ois.readObject();
                    if(next instanceof Message)
                        return (Message)next;
                    else
                        getLog().e("Read non Message object: " + next);
                } catch(IOException e) {
                    throw new HousemateException("Could not read object from the stream", e);
                } catch(ClassNotFoundException e) {
                    throw new HousemateException("Could not deserialize object from the stream", e);
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
        private Message heartbeat = new Message<NoPayload>(new String[] {}, "heartbeat", NoPayload.VALUE);

        private ObjectOutputStream oos;

        public MessageSender() throws HousemateException {
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
            } catch(IOException e) {
                throw new HousemateException("Could not create object output stream", e);
            }
        }

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
                    sendMessage(message);
                } catch(InterruptedException e) {
                    if(socket == null || socket.isClosed())
                        break;
                    getLog().d("Interrupted waiting for message to send. Trying again");
                } catch(IOException e) {
                    getLog().e("Error sending message to client. Will attempt to reconnect", e);
                    disconnect(true);
                    break;
                } catch(HousemateException e) {
                    getLog().e("Error getting message text. Message will not be sent");
                }
            }

            getLog().d("Stopped message sender");
        }

        /**
         * Send a message
         * @param message the message to send
         * @throws IOException
         * @throws HousemateException
         */
        private void sendMessage(Message message) throws IOException, HousemateException {
            // send the message and flush it
            oos.writeObject(message);
            oos.reset();
            if(outputQueue.size() == 0)
                oos.flush();
        }
    }
}