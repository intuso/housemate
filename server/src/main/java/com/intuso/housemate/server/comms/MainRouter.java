package com.intuso.housemate.server.comms;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.message.AuthenticationRequest;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.object.server.proxy.ServerProxyRootObject;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
import com.intuso.housemate.server.object.general.ServerGeneralRootObject;
import com.intuso.housemate.comms.transport.rest.RestServer;
import com.intuso.housemate.comms.transport.socket.server.SocketServer;
import com.intuso.housemate.object.server.ClientPayload;
import com.intuso.housemate.object.server.RemoteClient;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * The main router for the whole app. All comms routers attach to this
 */
@Singleton
public final class MainRouter extends Router {

    private final Injector injector;
    private final Resources resources;

    private final LinkedBlockingQueue<Message<Message.Payload>> incomingMessages = new LinkedBlockingQueue<Message<Message.Payload>>();
    private final MessageProcessor messageProcessor = new MessageProcessor();

    @Inject
    public MainRouter(Injector injector, Resources resources) {
        super(resources);
        this.injector = injector;
        this.resources = resources;
        setRouterStatus(Status.Connected);
        login(new InternalAuthentication());
    }
	
	/**
	 * Start accepting connections to the server
	 */
    public final void start() {

		// start the thread that will process incoming messages
		messageProcessor.start();

        // start the socket server
        try {
            new SocketServer(resources, this).start();
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Could not start server comms", e);
        }

        // start the rest server
        try {
            new RestServer(resources, this).start();
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Could not start server comms", e);
        }
	}

    @Override
    public void sendMessage(Message message) {
        incomingMessages.add(message);
    }

    @Override
    public void connect() {
        throw new HousemateRuntimeException("The main router cannot be connected");
    }

    @Override
    public void disconnect() {
        throw new HousemateRuntimeException("The main router cannot be disconnected");
    }

    public void sendMessageToClient(String[] path, String type, Message.Payload payload, RemoteClientImpl client) throws HousemateException {
        Message<?> message = new Message<Message.Payload>(path, type, payload, client.getRoute());
        getLog().d("Sending message " + message.toString());
        // to send a message we tell the outgoing root it is received. Any listeners on the outgoing root
        // will get it. These listeners are all created from the clientHandle and just put messages
        // on their respective clients queues, hence "sending" it to the clients that want it
        try {
            messageReceived(message);
        } catch(HousemateException e) {
            getLog().e("Failed to send message to client");
            getLog().st(e);
            throw e;
        }
	}

    private Root<?> getRoot(RemoteClient client, Message<?> message) throws HousemateException {
        if(client == null) {
            if(message.getPayload() instanceof AuthenticationRequest)
                return injector.getInstance(ServerGeneralRootObject.class);
            else
                throw new UnknownClientRouteException(message.getRoute());
        }
        if(client.getType() != null) {
            // intercept certain messages
            if(message.getPath().length == 1 && message.getType().equals(Root.DISCONNECT_TYPE))
                return injector.getInstance(ServerGeneralRootObject.class);
            switch(client.getType()) {
                case Real: // the server proxy objects are for remote real objects
                    return injector.getInstance(ServerProxyRootObject.class);
                case Proxy: // the server bridge objects are for remote proxy objects
                    return injector.getInstance(RootObjectBridge.class);
            }
        }
        // all other requests should go to the general root object
        return injector.getInstance(ServerGeneralRootObject.class);
    }

    private class MessageProcessor extends Thread {
        @Override
        public void run() {

            Message<Message.Payload> message;

            /**
             * While we shouldn't stop
             */
            while(!isInterrupted()) {

                // get the next message
                try {
                    message = incomingMessages.take();
                } catch(InterruptedException e) {
                    break;
                }
                getLog().d("Message received " + message.toString());
                try {
                    RemoteClientImpl client = injector.getInstance(RemoteClientManager.class).getClient(message.getRoute());
                    Root<?> root = getRoot(client, message);
                    // wrap payload in new payload in which we can put the client's id
                    message = new Message<Message.Payload>(message.getPath(), message.getType(),
                            new ClientPayload<Message.Payload>(client, message.getPayload()), message.getRoute());
                    root.messageReceived(message);
                } catch(Throwable t) {
                    getLog().e("Failed to distribute received message");
                    getLog().st(t);
                }
            }
        }
    };
}
