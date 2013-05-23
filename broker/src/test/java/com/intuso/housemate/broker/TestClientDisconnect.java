package com.intuso.housemate.broker;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.client.comms.ClientComms;
import com.intuso.housemate.object.broker.DisconnectListener;
import com.intuso.housemate.object.broker.RemoteClient;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 15/05/13
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class TestClientDisconnect {

    private static BrokerServerEnvironment environment = null;

    static {
        try {
            environment = TestUtils.startBroker(65432);
        } catch(HousemateException e) {
            System.err.println("Failed to start broker");
            e.printStackTrace();
        }
    }

    @Test
    public void testDisconnect() throws InterruptedException {

        final AtomicBoolean disconnected = new AtomicBoolean(true);

        ProxyResources<SimpleProxyFactory.All> resources = TestUtils.createProxyRootResources(environment);
        ClientComms comms = new ClientComms(resources, "localhost", 65432);
        resources = new ProxyResources<SimpleProxyFactory.All>(resources.getLog(), resources.getProperties(),
                comms, resources.getObjectFactory(), resources.getRegexMatcherFactory());
        final SimpleProxyObject.Root root = new SimpleProxyObject.Root(resources, resources);
        comms.connect(new UsernamePassword(false, "admin", "admin", false), new AuthenticationResponseHandler() {
            @Override
            public void responseReceived(Root.AuthenticationResponse response) {
                root.connect(new UsernamePassword(false, "admin", "admin", false), new AuthenticationResponseHandler() {
                    @Override
                    public void responseReceived(Root.AuthenticationResponse response) {
                        synchronized (disconnected) {
                            disconnected.set(false);
                            disconnected.notify();
                        }
                        RemoteClient client = environment.getGeneralResources().getAuthenticationController().getClient(
                                Arrays.asList("1", "0"));
                        assertNotNull(client);
                        client.addDisconnectListener(new DisconnectListener() {
                            @Override
                            public void disconnected(RemoteClient client) {
                                synchronized (disconnected) {
                                    disconnected.set(true);
                                    disconnected.notify();
                                }
                            }
                        });
                    }
                });
            }
        });

        synchronized (disconnected) {
            disconnected.wait(); // wait for messages to be processed
            assertFalse(disconnected.get());
        }
        root.disconnect();
        synchronized (disconnected) {
            disconnected.wait(); // wait for messages to be processed
            assertTrue(disconnected.get());
        }
    }

    @Test
    public void testSocketClosed() throws InterruptedException, IOException {

        // start a server socket, wait for the client to connect to it, then stream between the two
        final ServerSocket server = new ServerSocket(65433);
        Thread accepter = new Thread() {
            @Override
            public void run() {
                Socket clientSocket = null;
                Socket serverSocket = null;
                StreamBridge fromClient = null;
                StreamBridge fromServer = null;
                try {
                    clientSocket = server.accept();
                    serverSocket = new Socket("localhost", 65432);
                    fromClient = new StreamBridge(clientSocket.getInputStream(), serverSocket.getOutputStream());
                    fromServer = new StreamBridge(serverSocket.getInputStream(), clientSocket.getOutputStream());
                    fromClient.start();
                    fromServer.start();
                    // block this from returning
                    while(!isInterrupted())
                        Thread.sleep(10);
                } catch(InterruptedException e) {
                } catch(IOException e) {
                    e.printStackTrace();
                } finally {
                    try { serverSocket.close(); } catch(IOException e) {}
                    try { clientSocket.close(); } catch(IOException e) {}
                    fromClient.interrupt();
                    fromServer.interrupt();
                    try { fromClient.join(); } catch(InterruptedException e) {}
                    try { fromServer.join(); } catch(InterruptedException e) {}
                }
            }
        };
        accepter.start();

        // repeat the same test as the disconnect, but instead of disconnecting, close the server socket
        final AtomicBoolean disconnected = new AtomicBoolean(true);

        ProxyResources<SimpleProxyFactory.All> resources = TestUtils.createProxyRootResources(environment);
        ClientComms comms = new ClientComms(resources, "localhost", 65433);
        resources = new ProxyResources<SimpleProxyFactory.All>(resources.getLog(), resources.getProperties(),
                comms, resources.getObjectFactory(), resources.getRegexMatcherFactory());
        final SimpleProxyObject.Root root = new SimpleProxyObject.Root(resources, resources);
        comms.connect(new UsernamePassword(false, "admin", "admin", false), new AuthenticationResponseHandler() {
            @Override
            public void responseReceived(Root.AuthenticationResponse response) {
                root.connect(new UsernamePassword(false, "admin", "admin", false), new AuthenticationResponseHandler() {
                    @Override
                    public void responseReceived(Root.AuthenticationResponse response) {
                        synchronized (disconnected) {
                            disconnected.set(false);
                            disconnected.notify();
                        }
                        RemoteClient client = environment.getGeneralResources().getAuthenticationController().getClient(
                                Arrays.asList("2", "0"));
                        assertNotNull(client);
                        client.addDisconnectListener(new DisconnectListener() {
                            @Override
                            public void disconnected(RemoteClient client) {
                                synchronized (disconnected) {
                                    disconnected.set(true);
                                    disconnected.notify();
                                }
                            }
                        });
                    }
                });
            }
        });

        synchronized (disconnected) {
            disconnected.wait(); // wait for messages to be processed
            assertFalse(disconnected.get());
        }
        server.close();
        accepter.interrupt();
        accepter.join();
        synchronized (disconnected) {
            disconnected.wait(); // wait for messages to be processed
            assertTrue(disconnected.get());
        }
    }

    private class StreamBridge extends Thread {

        private final InputStream in;
        private final OutputStream out;

        private StreamBridge(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int numRead;
            try {
                while(!isInterrupted() && (numRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, numRead);
                    out.flush();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
