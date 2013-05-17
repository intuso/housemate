package com.intuso.housemate.broker.comms.socket;

import com.intuso.housemate.broker.BrokerServerEnvironment;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.comms.Router;
import com.intuso.utils.log.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 25/03/12
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
public class SocketServer {
    
    private final Log log;

    /**
     * The server socket
     */
    private final ServerSocket serverSocket;

    /**
     * Thread for accepting new client connections
     */
    private final Accepter accepter;

    private final Router clientMap;

    public SocketServer(BrokerGeneralResources resources, Router clientMap) throws HousemateException {

        this.clientMap = clientMap;

        log = resources.getLog();

        try {
            // open the server port
            String port = resources.getProperties().get(BrokerServerEnvironment.BROKER_PORT);
            log.d("Starting server comms on port " + port);
            serverSocket = new ServerSocket(Integer.parseInt(port));
            accepter = new Accepter();
        } catch (IOException e) {
            log.e("Could not open port to listen on");
            log.st(e);
            throw new HousemateException("Could not open port to listen on", e);
        }
    }
    
    public void start() {
        // start the thread that will accept connections from the port
        accepter.start();
    }

    /**
     * Class to accept all connections on the server port
     * @author tclabon
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
                    new SocketClient(clientMap, socket, log);
                } catch (IOException e) {
                    if(!serverSocket.isClosed()) {
                        log.e("Error getting next client connection.");
                        log.st(e);
                    }
                } catch(HousemateException e) {
                    log.e("Could not create client handle for new client connection");
                    log.st(e);
                }
            }
        }
    }
}
