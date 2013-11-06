package com.intuso.housemate.broker;

import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.comms.transport.rest.RestServer;
import com.intuso.housemate.comms.transport.socket.client.SocketClient;
import com.intuso.housemate.comms.transport.socket.server.SocketServer;
import com.intuso.housemate.object.broker.RemoteClient;
import com.intuso.housemate.object.broker.RemoteClientListener;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.proxy.simple.SimpleProxyResources;
import com.intuso.utilities.listener.ListenerRegistration;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.*;

/**
 */
@Ignore
public class TestClientDisconnect {

    private final static BrokerServerEnvironment environment = TestUtils.startBroker(65432, 65433);
    private final static AtomicInteger TEST_NUM = new AtomicInteger(0);

    @Test(timeout = 5000)
    public void testDisconnect() throws InterruptedException {

        final int testNum = TEST_NUM.incrementAndGet();

        final AtomicBoolean disconnected = new AtomicBoolean(true);

        SimpleProxyResources<SimpleProxyFactory.All> resources = TestUtils.createProxyRootResources(environment);
        resources.getProperties().put(SocketClient.BROKER_HOST, "localhost");
        resources.getProperties().put(SocketServer.PORT, "65432");
        resources.getProperties().put(RestServer.PORT, "65433");
        final SocketClient socketClient = new SocketClient(resources);
        resources = new SimpleProxyResources<SimpleProxyFactory.All>(resources.getLog(), resources.getProperties(),
                socketClient, resources.getObjectFactory(), resources.getRegexMatcherFactory());
        final SimpleProxyObject.Root root = new SimpleProxyObject.Root(resources, resources);
        socketClient.addObjectListener(new RootListener<RouterRootObject>() {
            @Override
            public void connectionStatusChanged(RouterRootObject routerRoot, ConnectionStatus status) {
                if(status == ConnectionStatus.Authenticated) {
                    root.addObjectListener(new RootListener<SimpleProxyObject.Root>() {

                        @Override
                        public void connectionStatusChanged(SimpleProxyObject.Root root, ConnectionStatus status) {
                            if(status != ConnectionStatus.Authenticated)
                                return;
                            synchronized(disconnected) {
                                disconnected.set(false);
                                disconnected.notify();
                            }
                            RemoteClient client = environment.getGeneralResources().getRemoteClientManager().getClient(
                                    Arrays.asList(Integer.toString(testNum), "0"));
                            assertNotNull(client);
                            client.addListener(new RemoteClientListener() {
                                @Override
                                public void disconnected(RemoteClient client) {
                                    synchronized(disconnected) {
                                        disconnected.set(true);
                                        disconnected.notify();
                                    }
                                }

                                @Override
                                public void connectionLost(RemoteClient client) {
                                    synchronized(disconnected) {
                                        disconnected.notify();
                                    }
                                }

                                @Override
                                public void reconnected(RemoteClient client) {
                                    synchronized(disconnected) {
                                        disconnected.notify();
                                    }
                                }
                            });
                        }

                        @Override
                        public void brokerInstanceChanged(SimpleProxyObject.Root root) {
                            // do nothing
                        }
                    });
                    root.login(new UsernamePassword("admin", "admin", false));
                } else if(status == ConnectionStatus.Unauthenticated)
                    socketClient.login(new UsernamePassword("admin", "admin", false));
            }

            @Override
            public void brokerInstanceChanged(RouterRootObject root) {
                // do nothing
            }
        });
        socketClient.connect();

        synchronized (disconnected) {
            disconnected.wait(); // wait for messages to be processed
            assertFalse(disconnected.get());
        }
        root.logout();
        synchronized (disconnected) {
            disconnected.wait(); // wait for messages to be processed
            assertTrue(disconnected.get());
        }
    }

    @Test(timeout = 5000)
    public void testConnectionLost() throws InterruptedException, IOException {

        final int testNum = TEST_NUM.incrementAndGet();

        // start a server socket, wait for the client to connect to it, then stream between the two
        final ServerSocket server = new ServerSocket(65434);
        final Accepter accepter = new Accepter(server);
        accepter.start();

        // repeat the same test as the disconnect, but instead of disconnecting, close the server socket
        final AtomicBoolean connectionLost = new AtomicBoolean(true);

        SimpleProxyResources<SimpleProxyFactory.All> resources = TestUtils.createProxyRootResources(environment);
        resources.getProperties().put(SocketClient.BROKER_HOST, "localhost");
        resources.getProperties().put(SocketServer.PORT, "65434");
        final SocketClient comms = new SocketClient(resources);
        resources = new SimpleProxyResources<SimpleProxyFactory.All>(resources.getLog(), resources.getProperties(),
                comms, resources.getObjectFactory(), resources.getRegexMatcherFactory());
        final SimpleProxyObject.Root root = new SimpleProxyObject.Root(resources, resources);
        comms.addObjectListener(new RootListener<RouterRootObject>() {
            @Override
            public void connectionStatusChanged(RouterRootObject routerRoot, ConnectionStatus status) {
                if (status == ConnectionStatus.Authenticated) {
                    root.addObjectListener(new RootListener<SimpleProxyObject.Root>() {

                        @Override
                        public void connectionStatusChanged(SimpleProxyObject.Root root, ConnectionStatus status) {
                            if (status != ConnectionStatus.Authenticated)
                                return;
                            synchronized (connectionLost) {
                                connectionLost.set(false);
                                connectionLost.notify();
                            }
                            RemoteClient client = environment.getGeneralResources().getRemoteClientManager().getClient(
                                    Arrays.asList(Integer.toString(testNum), "0"));
                            assertNotNull(client);
                            client.addListener(new RemoteClientListener() {
                                @Override
                                public void disconnected(RemoteClient client) {
                                    synchronized (connectionLost) {
                                        connectionLost.notify();
                                    }
                                }

                                @Override
                                public void connectionLost(RemoteClient client) {
                                    synchronized (connectionLost) {
                                        connectionLost.set(true);
                                        connectionLost.notify();
                                    }
                                }

                                @Override
                                public void reconnected(RemoteClient client) {
                                    synchronized (connectionLost) {
                                        connectionLost.notify();
                                    }
                                }
                            });
                        }

                        @Override
                        public void brokerInstanceChanged(SimpleProxyObject.Root root) {
                            // do nothing
                        }
                    });
                    root.login(new UsernamePassword("admin", "admin", false));
                } else if (status == ConnectionStatus.Unauthenticated)
                    comms.login(new UsernamePassword("admin", "admin", false));
            }

            @Override
            public void brokerInstanceChanged(RouterRootObject root) {
                // do nothing
            }
        });
        comms.connect();

        synchronized (connectionLost) {
            connectionLost.wait(); // wait for messages to be processed
            assertFalse(connectionLost.get());
        }
        server.close();
        accepter.interrupt();
        accepter.join();
        synchronized (connectionLost) {
            connectionLost.wait(); // wait for messages to be processed
            assertTrue(connectionLost.get());
        }
    }

    @Test(timeout = 5000)
    public void testReconnect() throws InterruptedException, IOException {

        final int testNum = TEST_NUM.incrementAndGet();

        // start a server socket, wait for the client to connect to it, then stream between the two
        ServerSocket server = new ServerSocket(65434);
        Accepter accepter = new Accepter(server);
        accepter.start();

        // repeat the same test as the disconnect, but instead of disconnecting, close the server socket
        final AtomicBoolean connected = new AtomicBoolean(true);

        SimpleProxyResources<SimpleProxyFactory.All> resources = TestUtils.createProxyRootResources(environment);
        resources.getProperties().put(SocketClient.BROKER_HOST, "localhost");
        resources.getProperties().put(SocketServer.PORT, "65434");
        final SocketClient comms = new SocketClient(resources);
        resources = new SimpleProxyResources<SimpleProxyFactory.All>(resources.getLog(), resources.getProperties(),
                comms, resources.getObjectFactory(), resources.getRegexMatcherFactory());
        final SimpleProxyObject.Root root = new SimpleProxyObject.Root(resources, resources);
        ListenerRegistration rootListenerRegistration = root.addObjectListener(new RootListener<SimpleProxyObject.Root>() {

            @Override
            public void connectionStatusChanged(SimpleProxyObject.Root root, ConnectionStatus status) {
                if(status != ConnectionStatus.Authenticated)
                    return;
                synchronized(connected) {
                    connected.set(true);
                    connected.notify();
                }
                RemoteClient client = environment.getGeneralResources().getRemoteClientManager().getClient(
                        Arrays.asList(Integer.toString(testNum), "0"));
                assertNotNull(client);
                client.addListener(new RemoteClientListener() {
                    @Override
                    public void disconnected(RemoteClient client) {
                        synchronized(connected) {
                            connected.notify();
                        }
                    }

                    @Override
                    public void connectionLost(RemoteClient client) {
                        synchronized(connected) {
                            connected.set(false);
                            connected.notify();
                        }
                    }

                    @Override
                    public void reconnected(RemoteClient client) {
                        synchronized(connected) {
                            connected.set(true);
                            connected.notify();
                        }
                    }
                });
            }

            @Override
            public void brokerInstanceChanged(SimpleProxyObject.Root root) {
                // do nothing
            }
        });
        ListenerRegistration commsListenerRegistration = comms.addObjectListener(new RootListener<RouterRootObject>() {
            @Override
            public void connectionStatusChanged(RouterRootObject routerRoot, ConnectionStatus status) {
                if(status == ConnectionStatus.Authenticated) {
                    root.login(new UsernamePassword("admin", "admin", false));
                } else if(status == ConnectionStatus.Unauthenticated)
                    comms.login(new UsernamePassword("admin", "admin", false));
            }

            @Override
            public void brokerInstanceChanged(RouterRootObject root) {
                // do nothing
            }
        });
        comms.connect();

        synchronized (connected) {
            connected.wait(); // wait for messages to be processed, assert that it's connected
            assertTrue(connected.get());
        }
        rootListenerRegistration.removeListener();
        commsListenerRegistration.removeListener();
        server.close();
        accepter.interrupt();
        accepter.join();
        synchronized (connected) {
            connected.wait(); // wait for messages to be processed, assert that the connect is lost
            assertFalse(connected.get());
        }
        server = new ServerSocket(65434);
        accepter = new Accepter(server);
        accepter.start();
        synchronized (connected) {
            connected.wait(); // wait for messages to be processed, assert that the it is reconnected
            assertTrue(connected.get());
        }
        server.close();
        accepter.interrupt();
        accepter.join();
    }

    private class Accepter extends Thread {

        private final ServerSocket server;

        private Accepter(ServerSocket server) {
            this.server = server;
        }

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
