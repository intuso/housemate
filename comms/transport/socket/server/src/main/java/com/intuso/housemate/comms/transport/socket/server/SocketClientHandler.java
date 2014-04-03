package com.intuso.housemate.comms.transport.socket.server;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.comms.serialiser.api.Serialiser;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;
import com.intuso.utilities.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A class that is created for each client that connects. This class starts threads that live as long as the client connection.
 * If a client disconnects and reconnects with the same proxy key, the connection will be handled by a different instance of
 * this class
 *
 */
public final class SocketClientHandler implements Receiver<Message.Payload> {

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

    private final Serialiser serialiser;
	
	/**
	 * Create a new handle for the given client connection
	 * @param socket client connection
	 * @param log log to use
	 * @throws HousemateException 
	 */
	public SocketClientHandler(Log log, Router router, Socket socket, Set<StreamSerialiserFactory> serialiserFactories) throws HousemateException {
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
			throw new HousemateException("Could not set socket keepalive to true. Closing connection");
		}
		try {
			this.socket.setSoTimeout(60000);
		} catch (SocketException e1) {
			this.log.e("Could not set read timeout on socket. Closing connection");
			throw new HousemateException("Could not set read timeout on socket. Closing connection from");
		}

        Map<String, String> clientDetails = readClientData(socket);

        serialiser = getSerialiser(clientDetails, serialiserFactories, socket);

        outputQueue = new LinkedBlockingQueue<Message>();
        streamReader = new StreamReader();
        messageSender = new MessageSender();

        routerRegistration = router.registerReceiver(this);
		
		// start the reader/writer threads 
		streamReader.start();
		messageSender.start();
	}

    private Map<String, String> readClientData(Socket socket) throws HousemateException {
        try {
            Map<String, String> result = Maps.newHashMap();
            StringBuilder data = new StringBuilder();
            InputStream in = socket.getInputStream();
            int i;
            while(true) {
                if(in.available() == 0)
                    Thread.sleep(100);
                else {
                    i = in.read();
                    if(i >= 0)
                        data.append((char)i);
                }
                if(data.length() > 1 && data.substring(data.length() - 2).equals("\n\n"))
                    break;
            }
            for(String line : data.toString().split("\n")) {
                int index = line.indexOf(":");
                if(index > 0)
                    result.put(line.substring(0, index), line.substring(index + 1));
                else if (line.length() > 0)
                    result.put(line, null);
            }
            return result;
        } catch(InterruptedException e) {
            throw new HousemateException("Interrupted reading client data", e);
        } catch(IOException e) {
            throw new HousemateException("Error reading client data", e);
        }
    }

    private Serialiser getSerialiser(Map<String, String> clientDetails,
                                     Set<StreamSerialiserFactory> serialiserFactories, Socket socket) throws HousemateException {
        String serialiserType = clientDetails.get(Serialiser.DETAILS_KEY);
        if(serialiserType == null) {
            try {
                socket.getOutputStream().write(("Missing " + Serialiser.DETAILS_KEY + " property\n").getBytes());
                socket.getOutputStream().flush();
            } catch(IOException e) {
                log.e("Failed to send missing serialisation key message to client", e);
            }
            throw new HousemateException("No serialisation key in client details");
        }
        StreamSerialiserFactory serialiserFactory = null;
        for(StreamSerialiserFactory sf : serialiserFactories) {
            if(sf.getType().equals(serialiserType)) {
                serialiserFactory = sf;
                break;
            }
        }
        if(serialiserFactory == null) {
            try {
                socket.getOutputStream().write(("No serialiser known for serialisation type " + serialiserType + "\n").getBytes());
                socket.getOutputStream().flush();
            } catch(IOException e) {
                log.e("Failed to send unknown serialisation type message to client", e);
            }
            throw new HousemateException("Unknown serialiser type " + serialiserType);
        }
        try {
            log.d("Found serialiser type for client: " + serialiserType);
            socket.getOutputStream().write(("Success\n").getBytes());
            socket.getOutputStream().flush();
            return serialiserFactory.create(socket.getOutputStream(), socket.getInputStream());
        } catch (IOException e) {
            throw new HousemateException("Failed to create serialiser", e);
        }
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
		public Message<?> readMessage() throws HousemateException {
            while(true) {
                try {
                    return serialiser.read();
                } catch(HousemateException e) {
                    log.e("Problem reading message, retrying", e);
                } catch(IOException e) {
                    throw new HousemateException("Could not read message", e);
                } catch (InterruptedException e) {
                    throw new HousemateException("Interrupted waiting to receive message", e);
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
        private Message heartbeat = new Message<NoPayload>(new String[] {}, "heartbeat", NoPayload.INSTANCE);
		
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
					serialiser.write(toSend);
				}
			} catch(IOException e) {
				log.e("Error sending message to client. Closing client connection", e);
				close();
			}
		}
	}
}
