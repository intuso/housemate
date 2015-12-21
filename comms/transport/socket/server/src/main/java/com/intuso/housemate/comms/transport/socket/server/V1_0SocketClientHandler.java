package com.intuso.housemate.comms.transport.socket.server;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.comms.v1_0.api.ClientConnection;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus;
import com.intuso.housemate.comms.v1_0.api.payload.StringPayload;
import com.intuso.housemate.comms.v1_0.serialiser.api.Serialiser;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Set;

/**
 * A class that is created for each client that connects. This class starts threads that live as long as the client connection.
 * If a client disconnects and reconnects with the same proxy key, the connection will be handled by a different instance of
 * this class
 *
 */
public final class V1_0SocketClientHandler implements Router.Receiver {

    public interface Factory {
        V1_0SocketClientHandler create(Logger logger, Socket socket);
    }

	/**
	 * The log to use
	 */
	private final Logger logger;
	
	/**
	 * This client's socket
	 */
	private final Socket socket;
	
	/**
	 * The thread that reads from the socket's input stream 
	 */
	private final StreamReader streamReader;

    private final Router.Registration routerRegistration;

    private final Serialiser serialiser;
	
	/**
	 * Create a new handle for the given client connection
	 * @param socket client connection
	 * @param logger log to use
	 */
    @Inject
	public V1_0SocketClientHandler(Router<?> router,
                                   Set<Serialiser.Factory> serialiserFactories,
                                   @Assisted Logger logger,
                                   @Assisted Socket socket) {
        super();

		// save all the given input params
		this.socket = socket;
		this.logger = logger;
		
		// construct the client address used to log messages with
		this.logger.debug("Received client connection from " + socket.getInetAddress());
		
		// set the keepalive and timeouts on the socket
		try {
			this.socket.setKeepAlive(true);
		} catch (SocketException e1) {
			this.logger.error("Could not set socket keepalive to true. Closing connection");
			throw new HousemateCommsException("Could not set socket keepalive to true. Closing connection");
		}
		try {
			this.socket.setSoTimeout(0);
		} catch (SocketException e1) {
			this.logger.error("Could not set read timeout on socket. Closing connection");
			throw new HousemateCommsException("Could not set read timeout on socket. Closing connection from");
		}

        Map<String, String> clientDetails = readClientData(socket);

        serialiser = getSerialiser(clientDetails, serialiserFactories, socket);

        streamReader = new StreamReader();

        routerRegistration = router.registerReceiver(this);
		
		// start the reader/writer threads 
		streamReader.start();
	}

    private Map<String, String> readClientData(Socket socket) {
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
                    if(i >= 0 && i != '\r')
                        data.append((char)i);
                }
                if(data.length() > 1 && data.substring(data.length() - 2).equals("\n\n"))
                    break;
            }
            for(String line : data.toString().split("\n")) {
                int index = line.indexOf("=");
                if(index > 0)
                    result.put(line.substring(0, index), line.substring(index + 1));
                else if (line.length() > 0)
                    result.put(line, null);
            }
            return result;
        } catch(InterruptedException e) {
            throw new HousemateCommsException("Interrupted reading client data", e);
        } catch(IOException e) {
            throw new HousemateCommsException("Error reading client data", e);
        }
    }

    private Serialiser getSerialiser(Map<String, String> clientDetails,
                                     Set<Serialiser.Factory> serialiserFactories, Socket socket) {
        String serialiserType = clientDetails.get(Serialiser.DETAILS_KEY);
        if(serialiserType == null) {
            try {
                socket.getOutputStream().write(("Missing " + Serialiser.DETAILS_KEY + " property\n").getBytes());
                socket.getOutputStream().flush();
            } catch(IOException e) {
                logger.error("Failed to send missing serialisation key message to client", e);
            }
            throw new HousemateCommsException("No serialisation key in client details");
        }
        Serialiser.Factory serialiserFactory = null;
        for(Serialiser.Factory sf : serialiserFactories) {
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
                logger.error("Failed to send unknown serialisation type message to client", e);
            }
            throw new HousemateCommsException("Unknown serialiser type " + serialiserType);
        }
        try {
            logger.debug("Found serialiser type for client: " + serialiserType);
            socket.getOutputStream().write(("Success\n").getBytes());
            socket.getOutputStream().flush();
            return serialiserFactory.create(socket.getOutputStream(), socket.getInputStream());
        } catch (IOException e) {
            throw new HousemateCommsException("Failed to create serialiser", e);
        }
    }
    
    @Override
    public void messageReceived(Message message) {
        logger.debug("Sending message " + message);
        _sendMessage(message);
    }

    @Override
    public void serverConnectionStatusChanged(ClientConnection clientConnection, ConnectionStatus connectionStatus) {
        _sendMessage(new Message(new String[] {""}, ClientConnection.NEXT_CONNECTION_STATUS_TYPE, connectionStatus));
    }

    @Override
    public void newServerInstance(ClientConnection clientConnection, String serverId) {
        _sendMessage(new Message(new String[] {""}, ClientConnection.SERVER_INSTANCE_ID_TYPE, new StringPayload(serverId)));
    }

    private synchronized void _sendMessage(Message message) {
        try {
            serialiser.write(message);
        } catch (IOException e) {
            logger.error("Failed to write message to client, closing connection");
            close();
            throw new HousemateCommsException("Client connection no longer open", e);
        }
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
				
				logger.debug("Closing connection");
				
				// interrupt the reader/writer threads
				streamReader.interrupt();
				
				// wait for the reader/writer threads to stop
				try {
					streamReader.join();
				} catch(InterruptedException e) {
					logger.error("Interrupted waiting for reader/writer threads to stop");
				}
		
				// close the socket
				try {
					socket.close();
				} catch (IOException e) {
					logger.error("Could not close client socket connection");
				}
				
				logger.debug("Closed connection");

                routerRegistration.unregister();
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
                    if(message == null)
                        return;
                    else {
                        logger.debug("Received message " + message);
                        routerRegistration.sendMessage(message);
                    }
				} catch(HousemateCommsException e) {
                    logger.error("Failed to read message from the queue", e);
					close();
					break;
				}
			}
		}
		
		/**
		 * Read a message from the client
		 * @return the next message read
		 */
		public Message<?> readMessage() {
            while(true) {
                try {
                    return serialiser.read();
                } catch(HousemateCommsException e) {
                    logger.error("Problem reading message, retrying", e);
                } catch(SocketException e) {
                    if(e.getMessage().equals("Socket closed"))
                        return null;
                    throw new HousemateCommsException("Could not read message", e);
                } catch(IOException e) {
                    throw new HousemateCommsException("Could not read message", e);
                } catch (InterruptedException e) {
                    throw new HousemateCommsException("Interrupted waiting to receive message", e);
                }
            }
		}
	}
}
