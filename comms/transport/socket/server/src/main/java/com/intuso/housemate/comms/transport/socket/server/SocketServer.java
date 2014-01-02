package com.intuso.housemate.comms.transport.socket.server;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.log.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 */
public class SocketServer extends Router {

    public final static String PORT = "socket.server.port";

    private final Log log;

    /**
     * The server socket
     */
    private final ServerSocket serverSocket;

    /**
     * Thread for accepting new client connections
     */
    private final Accepter accepter;

    private final Router.Registration routerRegistration;

    public SocketServer(Resources resources, Router router) throws HousemateException {

        super(resources);

        this.routerRegistration = router.registerReceiver(this);

        log = resources.getLog();

        setRouterStatus(Status.Connected);
        login(new AuthenticationMethod());

        try {
            // open the server port
            String port = resources.getProperties().get(PORT);
            if(port == null) {
                log.d("Socket server port not set, using default");
                port = "46873";
            }
            log.d("Starting server comms on port " + port);
            serverSocket = new ServerSocket(Integer.parseInt(port));
            accepter = new Accepter();
        } catch (IOException e) {
            log.e("Could not open port to listen on", e);
            throw new HousemateException("Could not open port to listen on", e);
        }
    }

    @Override
    public void connect() {
        // do nothing
    }

    @Override
    public void disconnect() {
        // do nothing
    }

    @Override
    public void sendMessage(Message<?> message) {
        routerRegistration.sendMessage(message);
    }

    public void start() {
        // start the thread that will accept connections from the port
        accepter.start();
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

            log.d("Listening for connections");

            // while we shouldn't shut down
            while(!isInterrupted()) {

                // get the next client connection
                try {
                    // pass it off to a separate class
                    Socket socket = serverSocket.accept();
                    // TODO read client "contract" - version, mime type etc
                    // could also read a name to call the client so that we can show something useful when showing
                    // who's connected?
                    new ClientHandle(SocketServer.this, socket, log);
                } catch (IOException e) {
                    if(!serverSocket.isClosed()) {
                        log.e("Error getting next client connection.", e);
                    }
                } catch(HousemateException e) {
                    log.e("Could not create client handle for new client connection", e);
                }
            }
        }
    }

    public class AuthenticationMethod implements com.intuso.housemate.api.authentication.AuthenticationMethod {

        private AuthenticationMethod() {}

        @Override
        public boolean isClientsAuthenticated() {
            return false;
        }
    }
}
