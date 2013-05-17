package com.intuso.housemate.broker.comms;

import com.intuso.housemate.broker.client.RemoteClient;
import com.intuso.housemate.broker.comms.socket.SocketServer;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.HousemateRuntimeException;
import com.intuso.housemate.core.comms.Comms;
import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.object.root.Root;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author tclabon
 */
public final class ServerComms extends Comms {

    private final LinkedBlockingQueue<Message<Message.Payload>> incomingMessages = new LinkedBlockingQueue<Message<Message.Payload>>();

    private final MessageProcessor messageProcessor = new MessageProcessor();

    private BrokerGeneralResources resources;

    public ServerComms(BrokerGeneralResources resources) {
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
    public void disconnect() {
        throw new HousemateRuntimeException("The ServerComms cannot be disconnected");
    }

    public void sendMessageToClient(String[] path, String type, Message.Payload payload, RemoteClient client) throws HousemateException {
        Message<?> message = new Message<Message.Payload>(path, type, payload,
                resources.getAuthenticationController().getClientRoute(client.getConnectionId()));
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
            if(message.getPayload() instanceof Root.AuthenticationRequest)
                return resources.getRoot();
            else
                throw new UnknownClientRouteException(message.getRoute());
        }
        if(client.getType() != null) {
            switch(client.getType()) {
                case REAL: // the broker proxy objects are for remote real objects
                    return resources.getProxyResources().getRoot();
                case PROXY: // the broker bridge objects are for remote proxy objects
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
                    RemoteClient client = resources.getAuthenticationController().getClient(message.getRoute());
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
