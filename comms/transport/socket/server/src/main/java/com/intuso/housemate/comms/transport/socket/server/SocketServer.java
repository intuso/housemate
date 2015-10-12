package com.intuso.housemate.comms.transport.socket.server;

import com.google.inject.Inject;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.plugin.api.internal.ExternalClientRouter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 */
public class SocketServer extends ExternalClientRouter<SocketServer> {

    public final static String PORT = "socket.server.port";

    /**
     * The server socket
     */
    private ServerSocket serverSocket;

    /**
     * Thread for accepting new client connections
     */
    private Accepter accepter;

    private final PropertyRepository properties;
    private final V1_0SocketClientHandler.Factory v1_0SocketClientHandlerFactory;

    @Inject
    public SocketServer(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router<?> router, V1_0SocketClientHandler.Factory v1_0SocketClientHandlerFactory) {
        super(log, listenersFactory, router);
        this.properties = properties;
        this.v1_0SocketClientHandlerFactory = v1_0SocketClientHandlerFactory;
    }

    @Override
    public void _start() {

        try {
            // open the server port
            String port = properties.get(PORT);
            if(port == null) {
                getLog().d("Socket server port not set, using default");
                port = "46873";
            }
            getLog().d("Creating server comms on port " + port);
            serverSocket = new ServerSocket(Integer.parseInt(port));
            accepter = new Accepter();
        } catch (IOException e) {
            getLog().e("Could not open port to listen on", e);
            throw new HousemateCommsException("Could not open port to listen on", e);
        }
        
        // start the thread that will accept connections from the port
        accepter.start();
        getLog().d("Accepting socket connections");
    }

    @Override
    public void _stop() {
        getLog().d("Stopping server comms");
        try {
            serverSocket.close();
        } catch (IOException e) {
            getLog().e("Failed to close socket server's socket", e);
        }
        // start the thread that will accept connections from the port
        accepter.interrupt();
        try {
            accepter.join();
        } catch (InterruptedException e) {
            getLog().e("Failed to wait for accepter thread to stop");
        }
    }

    /**
     * Class to accept all connections on the server port
     * @author Tom Clabon
     *
     */
    private class Accepter extends Thread {
        @SuppressWarnings("unused")
        @Override
        public void run() {

            getLog().d("Listening for connections");

            // while we shouldn't shut down
            while(!isInterrupted()) {

                // get the next client connection
                final Socket socket;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    if(!serverSocket.isClosed()) {
                        getLog().e("Error getting next client connection.", e);
                    }
                    continue;
                }

                // pass it off to a separate class, in a new thread so if there are any problems, we don't block
                // other connections
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            v1_0SocketClientHandlerFactory.create(socket);
                        } catch (Throwable t) {
                            getLog().e("Could not create client handle for new client connection", t);
                            try {
                                socket.close();
                            } catch (IOException e1) {
                                getLog().e("Failed to create client connection and close the socket", t);
                            }
                        }
                    }
                }.start();
            }
        }
    }
}
