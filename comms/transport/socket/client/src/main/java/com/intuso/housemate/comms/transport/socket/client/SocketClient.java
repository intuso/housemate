package com.intuso.housemate.comms.transport.socket.client;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.resources.Resources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Client comms implementation that provides socket-based communications with the broker
 *
 */
public class SocketClient extends Router {

    /**
     * broker host
     */
    private final String host;

    /**
     * broker port
     */
    private final int port;

    /**
     * True if a connection attempt is underway
     */
    private boolean connecting;

    /**
     * The socket to send/receive over
     */
    private Socket socket;

    /**
     * The queue that messages to send are put on
     */
    private LinkedBlockingQueue<Message> outputQueue;

    /**
     * thread that reads the stream and creates copies of messages that were sent by the broker
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
    public SocketClient(Resources resources, String host, int port) {
        super(resources);

        // create the queues and threads
        outputQueue = new LinkedBlockingQueue<Message>();

        // get the details of the broker (host, port and root topic)
        this.host = host;
        this.port = port;

        // log for debug info
        getLog().d("Broker host is \"" + this.host + "\"");
        getLog().d("Broker port is \"" + this.port + "\"");
    }

    @Override
    public final void disconnect() {
        shutdownComms(false);
    }

    /**
     * Connect to the broker
     */
    @Override
    public final void connect() {
        int delay = 1;
        while(socket == null || !socket.isConnected()) {
            try {
                getLog().d("Attempting to connect to broker (" + host + ":" + port + ")");

                // create the socket and the streams
                socket = new Socket(host, port);
                socket.setKeepAlive(true);
                socket.setSoTimeout(60000);

                getLog().d("Connected to broker, creating reader/writer threads");
                messageSender = new MessageSender();
                streamReader = new StreamReader();

                // start the threads
                streamReader.start();
                messageSender.start();

                getLog().d("Connected to broker");
                setRouterStatus(Status.Connected);

                // return from the method
                return;

            } catch (UnknownHostException e) {
                getLog().e("Broker host \"" + host + ":" + port + "\" is unknown, cannot connect");
            } catch (IOException e) {
                getLog().e("Error connecting to broker: " + e.getMessage());
                getLog().st(e);
            } catch (HousemateException e) {
                getLog().e("Error connecting to broker: " + e.getMessage());
                getLog().st(e);
            }

            getLog().e("Failed to connect to broker. Retrying in " + delay + " seconds");
            try {
                Thread.sleep(delay * 1000);
            } catch(InterruptedException e) {
                getLog().e("Interrupted waiting to retry connection. Aborting trying to connect");
                return;
            }
            delay *= 2;
        }
    }

    /**
     * Shutdown the comms connection
     */
    private void shutdownComms(boolean reconnecting) {

        getLog().d("Disconnecting from the broker");
        try {
            socket.close();
        } catch(IOException e) {
            if(!socket.isClosed())
                getLog().e("Error closing client connection to broker");
        }
        socket = null;

        getLog().d("Interrupting all reader/writer threads");
        streamReader.interrupt();
        messageSender.interrupt();

        try {
            getLog().d("Joining all reader/writer threads");
            streamReader.join();
            messageSender.join();
        } catch(InterruptedException e) {
            getLog().e("Interrupted while waiting for reader/writer threads to stop");
        }

        getLog().d("Disconnected");
        setRouterStatus(reconnecting ? Status.Connecting : Status.Disconnected);
    }

    /**
     * Reconnect to the broker
     */
    private void reconnect() {
        // do this in a new thread so the reader/writer threads don't block and then get interrupted
        if(!connecting) {
            getLog().d("Starting a thread to reconnect to the broker");
            new Thread() {
                @Override
                public void run() {
                    connecting = true;
                    getLog().d("Running reconnect thread");
                    shutdownComms(true);
                    connect();
                    getLog().d("Reconnected");
                    connecting = false;
                }
            }.start();
        }
    }

    @Override
    public void sendMessage(Message message) {
        outputQueue.add(message);
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
                getLog().e("Error reading from broker socket connection");
                reconnect();
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
     * Thread to read messages off the output queue and send them to the broker
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
                    getLog().e("Error sending message to client. Will attempt to reconnect");
                    getLog().st(e);
                    reconnect();
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