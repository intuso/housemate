package com.intuso.housemate.broker.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.message.AuthenticationRequest;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.comms.transport.socket.server.SocketServer;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.housemate.object.broker.RemoteClient;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author tclabon
 */
public final class MainRouter extends Router {

    private final LinkedBlockingQueue<Message<Message.Payload>> incomingMessages = new LinkedBlockingQueue<Message<Message.Payload>>();

    private final MessageProcessor messageProcessor = new MessageProcessor();

    private BrokerGeneralResources resources;

    public MainRouter(BrokerGeneralResources resources) {
        super(resources);
        this.resources = resources;
    }
	
	/**
	 * Start accepting connections to the server socket
	 */
    public final void start() {

		// start the thread that will process incoming messages
		messageProcessor.start();

        try {
            new SocketServer(resources, this).start();
        } catch(HousemateException e) {
            throw new HousemateRuntimeException("Could not start broker comms", e);
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

    private Root<?, ?> getRoot(RemoteClient client, Message<?> message) throws HousemateException {
        if(client == null) {
            if(message.getPayload() instanceof AuthenticationRequest)
                return resources.getRoot();
            else
                throw new UnknownClientRouteException(message.getRoute());
        }
        if(client.getType() != null) {
            // intercept certain messages
            if(message.getPath().length == 1 && message.getType().equals(Root.DISCONNECT))
                return resources.getRoot();
            switch(client.getType()) {
                case Real: // the broker proxy objects are for remote real objects
                    return resources.getProxyResources().getRoot();
                case Proxy: // the broker bridge objects are for remote proxy objects
                    return resources.getBridgeResources().getRoot();
            }
        }
        // all other requests should go to the general root object
        return resources.getRoot();
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
                    RemoteClientImpl client = resources.getRemoteClientManager().getClient(message.getRoute());
                    Root<?, ?> root = getRoot(client, message);
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
