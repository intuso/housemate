package com.intuso.housemate.server.comms;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.RealRoot;
import com.intuso.housemate.comms.api.internal.BaseRouter;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.plugin.api.internal.ExternalClientRouter;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.object.general.ServerGeneralRoot;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The main router for the whole app. All comms routers attach to this
 */
public final class MainRouter extends BaseRouter<MainRouter> {

    private final Injector injector;
    private Set<ExternalClientRouter> externalClientRouters;

    private final LinkedBlockingQueue<Message<?>> incomingMessages = new LinkedBlockingQueue<>();
    private final MessageProcessor messageProcessor = new MessageProcessor();

    @Inject
    public MainRouter(Logger logger, ListenersFactory listenersFactory, Injector injector) {
        super(logger, listenersFactory);
        this.injector = injector;
    }
	
	/**
	 * Start accepting connections to the server
	 */
    public final void start() {

        connectionEstablished();

        // start processing all the messages
        messageProcessor.start();

        // register the local client
        injector.getInstance(RealRoot.class).register(Server.INTERNAL_APPLICATION_DETAILS_V1_0, "Internal Client");
    }

    public final void startExternalRouters() {

        // create, start and register all the external routers
        externalClientRouters = injector.getInstance(new Key<Set<ExternalClientRouter>>() {});
        for(ExternalClientRouter externalClientRouter : externalClientRouters) {
            try {
                externalClientRouter.start();
            } catch(Throwable t) {
                throw new HousemateCommsException("Could not start external client router", t);
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
            getLogger().error("Failed to wait for message processor to stop");
        }
    }

    @Override
    public void sendMessage(Message<?> message) {
        incomingMessages.add(message);
    }

    @Override
    public void connect() {
        throw new HousemateCommsException("The main router cannot be connected");
    }

    @Override
    public void disconnect() {
        throw new HousemateCommsException("The main router cannot be disconnected");
    }

    public void sendMessageToClient(Message<Message.Payload> message) {
        getLogger().debug("Sending message " + message.toString());
        // to send a message we tell the outgoing root it is received. Any listeners on the outgoing root
        // will get it. These listeners are all created from the clientHandle and just put messages
        // on their respective clients queues, hence "sending" it to the clients that want it
        messageReceived(message);
	}

    @Override
    protected void sendMessageNow(Message<?> message) {
        getLogger().debug("Message received " + message.toString());
        try {
            RemoteClient client = injector.getInstance(RemoteClientManager.class).getClient(message.getRoute());
            Message.Receiver receiver = getReceiver(client, message);
            // wrap payload in new payload in which we can put the client's id
            message = new Message<Message.Payload>(message.getPath(), message.getType(),
                    new ClientPayload<>(client, message.getPayload()), message.getRoute());
            receiver.messageReceived(message);
        } catch(Throwable t) {
            getLogger().error("Failed to distribute received message", t);
        }
    }

    private Message.Receiver<Message.Payload> getReceiver(RemoteClient client, Message<?> message) {
        // intercept certain messages
        if(message.getPath().length == 1 && ServerGeneralRoot.TYPES.contains(message.getType()))
            return injector.getInstance(ServerGeneralRoot.class);
        // otherwise send it to the route for the client
        else if(client == null)
            throw new UnknownClientRouteException(message.getRoute());
        else if(RemoteClient.TYPES.contains(message.getType()))
            return client;
        else if(!client.isApplicationInstanceAllowed())
            throw new ApplicationInstanceNotAllowedException(client);
        else
            return client.getReceiver();
    }

    private class MessageProcessor extends Thread {
        @Override
        public void run() {

            Message<?> message;

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
                sendMessageNow(message);
            }
        }
    };
}
