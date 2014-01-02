package com.intuso.housemate.comms.transport.socket.server;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.utilities.log.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A class that is created for each client that connects. This class starts threads that live as long as the client connection.
 * If a client disconnects and reconnects with the same proxy key, the connection will be handled by a different instance of
 * this class
 *
 */
public final class ClientHandle implements Receiver<Message.Payload> {

	/**
	 * The log to use
	 */
	private final Log log;
	
	/**
	 * This client's socket
	 */
	private final Socket socket;
	
	/**
	 * Message output queue
	 */
	private final LinkedBlockingQueue<Message> outputQueue;
	
	/**
	 * The thread that reads from the socket's input stream 
	 */
	private final StreamReader streamReader;
	
	/**
	 * The thread that takes messages and writes them to the socket's output stream 
	 */
	private final MessageSender messageSender;

    private final Router.Registration routerRegistration;
	
	/**
	 * Create a new handle for the given client connection
	 * @param socket client connection
	 * @param log log to use
	 * @throws HousemateException 
	 */
	public ClientHandle(Router router, Socket socket, Log log) throws HousemateException {
        super();

		// save all the given input params
		this.socket = socket;
		this.log = log;
		
		// construct the client address used to log messages with
		this.log.d("Received client connection from " + socket.getInetAddress());
		
		// set the keepalive and timeouts on the socket
		try {
			this.socket.setKeepAlive(true);
		} catch (SocketException e1) {
			this.log.e("Could not set socket keepalive to true. Closing connection");
			close();
			throw new HousemateException("Could not set socket keepalive to true. Closing connection");
		}
		try {
			this.socket.setSoTimeout(60000);
		} catch (SocketException e1) {
			this.log.e("Could not set read timeout on socket. Closing connection");
			close();
			throw new HousemateException("Could not set read timeout on socket. Closing connection from");
		}

        outputQueue = new LinkedBlockingQueue<Message>();
        streamReader = new StreamReader();
        messageSender = new MessageSender();

        routerRegistration = router.registerReceiver(this);
		
		// start the reader/writer threads 
		streamReader.start();
		messageSender.start();
	}
    
    @Override
    public void messageReceived(Message message) {
        outputQueue.add(message);
    }
	
	/**
	 * Close the connection to this client
	 */
	protected void close() {
		// run in a new thread so that any of the reader/writer threads can call this function without blocking (they are
		// shutdown from within this function so need so should return from this function immediately to shutdown properly)
		new Thread() {
			@Override
			public void run() {
				
				log.d("Closing connection");
				
				// interrupt the reader/writer threads
				streamReader.interrupt();
				messageSender.interrupt();
				
				// wait for the reader/writer threads to stop
				try {
					streamReader.join();
					messageSender.join();
				} catch(InterruptedException e) {
					log.e("Interrupted waiting for reader/writer threads to stop");
				}
		
				// close the socket
				try {
					socket.close();
				} catch (IOException e) {
					log.e("Could not close client socket connection");
				}
				
				log.d("Closed connection");

                routerRegistration.connectionLost();
			}
		}.start();
	}
	
	/**
	 * Class to read data off the socket and convert it into message objects
	 * @author Tom Clabon
	 *
	 */
	private class StreamReader extends Thread {
		
		private ObjectInputStream ois;
		
		/**
		 * Create a new Stream Reader
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
			
			// read continuously from the socket
			while(!isInterrupted()) {
				try {
                    Message message = readMessage();
                    if(!(message.getPath().length == 0 && message.getType().equals("heartbeat"))) {
                        log.d("Received message " + message);
                        routerRegistration.sendMessage(message);
                    }
				} catch(HousemateException e) {
                    log.e("Failed to read message from the queue", e);
					close();
					break;
				}
			}
		}
		
		/**
		 * Read a message from the client
		 * @return the next message read
		 * @throws HousemateException malformed message
		 */
		public <T extends Message.Payload> Message<T> readMessage() throws HousemateException {
            while(true) {
                try {
                    Object next = ois.readObject();
                    if(next instanceof Message)
                        return (Message<T>)next;
                    else
                        log.e("Read non Message object: " + next);
                } catch(IOException e) {
                    throw new HousemateException("Could not read object from the stream", e);
                } catch(ClassNotFoundException e) {
                    throw new HousemateException("Could not deserialize object from the stream", e);
                }
            }
		}
	}
	
	/**
	 * Reads outgoing messages off a queue and sends them over the socket to the client
	 * @author Tom Clabon
	 *
	 */
	private class MessageSender extends Thread {

        /**
         * heartbeat messgae
         */
        private Message heartbeat = new Message<NoPayload>(new String[] {}, "heartbeat", NoPayload.VALUE);

        private ObjectOutputStream oos;

		/**
		 * Create a new message sender
		 */
		public MessageSender() throws HousemateException {
			// get the output stream
			try {
                oos = new ObjectOutputStream(socket.getOutputStream());
			} catch(IOException e) {
                throw new HousemateException("Could not create object output stream", e);
			}
		}
		
		@Override
		public void run() {

			try {
				
				// while we should keep running
				while(!isInterrupted()) {
					Message toSend;
					try {
						toSend = outputQueue.poll(30, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						log.e("Interrupted waiting for message to send, breaking loop");
						break;
					}
					if(toSend == null)
						toSend = heartbeat;
                    else
                        log.d("Sending message " + toSend);
					
					// send a message from the proxy output queue
					sendMessage(toSend);
				}
			} catch(HousemateException e) {
				log.e("Error sending message to client. Closing client connection", e);
				close();
			}
		}
		
		/**
		 * Send a message to the client
		 * @param message message to send
		 * @throws HousemateException
		 */
		private void sendMessage(Message message) throws HousemateException {
			try {
                // send the message and flush it
                oos.writeObject(message);
                oos.reset();
                if(outputQueue.size() == 0)
                    oos.flush();
			} catch(IOException e) {
				log.e("Error sending message to client");
                e.printStackTrace();
				throw new HousemateException("Could not send message", e);
			}
		}
	}
}
