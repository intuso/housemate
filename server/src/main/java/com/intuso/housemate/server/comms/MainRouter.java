package com.intuso.housemate.server.comms;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.plugin.api.ExternalClientRouter;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.object.general.ServerGeneralRoot;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The main router for the whole app. All comms routers attach to this
 */
public final class MainRouter extends Router {

    private final Injector injector;
    private Set<ExternalClientRouter> externalClientRouters;

    private final LinkedBlockingQueue<Message<Message.Payload>> incomingMessages = new LinkedBlockingQueue<>();
    private final MessageProcessor messageProcessor = new MessageProcessor();

    @Inject
    public MainRouter(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Injector injector) {
        super(log, listenersFactory, WriteableMapPropertyRepository.newEmptyRepository(listenersFactory, properties));
        this.injector = injector;
        setServerConnectionStatus(ServerConnectionStatus.ConnectedToServer);
    }
	
	/**
	 * Start accepting connections to the server
	 */
    public final void start() {

        // register the main router. This will cause the connection manager to send a message (put a it on our queue)
        // so we then need to get it and process it to finish the registration. We need this to block so it happens
        // before we create the external client routers so do this before starting the normal thread
        register(Server.INTERNAL_APPLICATION);
        processMessage(incomingMessages.poll());
        // start processing all the messages
        messageProcessor.start();

        // register the local client
        injector.getInstance(RealRoot.class).register(Server.INTERNAL_APPLICATION);
    }

    public final void startExternalRouters() {

        // create, start and register all the external routers
        externalClientRouters = injector.getInstance(new Key<Set<ExternalClientRouter>>() {});
        for(ExternalClientRouter externalClientRouter : externalClientRouters) {
            try {
                externalClientRouter.start();
                externalClientRouter.register(Server.INTERNAL_APPLICATION);
            } catch(HousemateException e) {
                throw new HousemateRuntimeException("Could not start external client router", e);
            }
        }
	}

    /**
     * Stop accepting connections to the server
     */
    public final void stop() {

        // start the thread that will process incoming messages
        messageProcessor.interrupt();

        for(ExternalClientRouter externalClientRouter : externalClientRouters)
            externalClientRouter.stop();

        try {
            messageProcessor.join();
        } catch(InterruptedException e) {
            getLog().e("Failed to wait for message processor to stop");
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

    public void sendMessageToClient(String[] path, String type, Message.Payload payload, RemoteClient client) throws HousemateException {
        Message<?> message = new Message<>(path, type, payload, client.getRoute());
        getLog().d("Sending message " + message.toString());
        // to send a message we tell the outgoing root it is received. Any listeners on the outgoing root
        // will get it. These listeners are all created from the clientHandle and just put messages
        // on their respective clients queues, hence "sending" it to the clients that want it
        messageReceived(message);
	}

    private void processMessage(Message<Message.Payload> message) {
        getLog().d("Message received " + message.toString());
        try {
            RemoteClient client = injector.getInstance(RemoteClientManager.class).getClient(message.getRoute());
            Root<?> root = getRoot(client, message);
            // wrap payload in new payload in which we can put the client's id
            message = new Message<Message.Payload>(message.getPath(), message.getType(),
                    new ClientPayload<>(client, message.getPayload()), message.getRoute());
            root.messageReceived(message);
        } catch(Throwable t) {
            getLog().e("Failed to distribute received message", t);
        }
    }

    private Root<?> getRoot(RemoteClient client, Message<?> message) throws HousemateException {
        // intercept certain messages
        if(message.getPath().length == 1 &&
                (message.getType().equals(Root.APPLICATION_REGISTRATION_TYPE)
                    || message.getType().equals(Root.APPLICATION_UNREGISTRATION_TYPE)))
            return injector.getInstance(ServerGeneralRoot.class);
        // otherwise send it to the route for the client
        if(client == null)
            throw new UnknownClientRouteException(message.getRoute());
        else if(!client.isApplicationInstanceAllowed())
            throw new ApplicationInstanceNotAllowedException(client);
        else
            return client.getRoot();
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
                processMessage(message);
            }
        }
    };
}
