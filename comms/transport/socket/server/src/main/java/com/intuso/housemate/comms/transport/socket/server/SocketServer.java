package com.intuso.housemate.comms.transport.socket.server;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;
import com.intuso.housemate.plugin.api.ExternalClientRouter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

/**
 */
public class SocketServer extends ExternalClientRouter {

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
    private final Set<StreamSerialiserFactory> serialiserFactories;

    @Inject
    public SocketServer(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router, Set<StreamSerialiserFactory> serialiserFactories) {
        super(log, listenersFactory, properties, router);
        this.properties = properties;
        this.serialiserFactories = serialiserFactories;
    }

    @Override
    public void _start() throws HousemateException {

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
            throw new HousemateException("Could not open port to listen on", e);
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
                Socket socket;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    if(!serverSocket.isClosed()) {
                        getLog().e("Error getting next client connection.", e);
                    }
                    continue;
                }

                try {
                    // pass it off to a separate class
                    // TODO read client "contract" - version, mime type etc
                    // could also read a name to call the client so that we can show something useful when showing
                    // who's connected?
                    new SocketClientHandler(getLog(), SocketServer.this, socket, serialiserFactories);
                } catch(HousemateException e) {
                    getLog().e("Could not create client handle for new client connection", e);
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        getLog().e("Failed to create client connection and close the socket", e);
                    }
                }
            }
        }
    }
}
