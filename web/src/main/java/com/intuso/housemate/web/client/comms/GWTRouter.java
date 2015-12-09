package com.intuso.housemate.web.client.comms;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.intuso.housemate.comms.v1_0.api.BaseRouter;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.web.client.NotConnectedException;
import com.intuso.housemate.web.client.service.CommsServiceAsync;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTRouter extends BaseRouter<GWTRouter> {

    private AsyncCallback<Void> sendCallback = new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable throwable) {
            if(throwable instanceof NotConnectedException)
                Window.alert("Connection timed out due to inactivity, reconnecting");
            else
                Window.alert("Reconnecting because of unknown error sending a message: " + throwable.getMessage());
            getLogger().error("Failed to send message", throwable);
            disconnect();
        }

        @Override
        public void onSuccess(Void aVoid) {
            getLogger().debug("Sent message to server");
        }
    };

    private AsyncCallback<Message<Message.Payload>[]> receiveCallback = new AsyncCallback<Message<Message.Payload>[]>() {
        @Override
        public void onFailure(Throwable throwable) {
            if(throwable instanceof NotConnectedException)
                Window.alert("Connection timed out due to inactivity, reconnecting");
            else
                Window.alert("Reconnecting because of unknown error getting messages: " + throwable.getMessage());
            getLogger().error("Failed to receive message", throwable);
            disconnect();
        }

        @Override
        public void onSuccess(Message<Message.Payload>[] messages) {
            // pass messages on
            for(Message message : messages) {
                getLogger().debug("Message received " + message.toString());
                try {
                    messageReceived(message);
                } catch(Throwable t) {
                    getLogger().error("Failed to process received message", t);
                }
            }

            // get more
            requestMessages();
        }
    };

    private final CommsServiceAsync commsService;

    /**
     * Create a new comms instance
     */
    @Inject
    public GWTRouter(Logger logger, ListenersFactory listenersFactory, CommsServiceAsync commsService) {
        super(logger, listenersFactory);
        this.commsService = commsService;
    }

    @Override
    public void connect() {
        connecting();
        commsService.connectClient(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                getLogger().error("Failed to connect", throwable);
                disconnect();
            }

            @Override
            public void onSuccess(Void aVoid) {
                connectionEstablished();
                // start getting messages
                requestMessages();
            }
        });
    }

    @Override
    public void disconnect() {
        connectionLost(false);
        commsService.disconnectClient(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                getLogger().error("Failed to disconnect", throwable);
            }

            @Override
            public void onSuccess(Void result) {
                // do nothing
            }
        });
    }

    @Override
    protected void sendMessageNow(Message<?> message) {
        getLogger().debug("Sending message " + message.toString());
        commsService.sendMessageToServer(message, sendCallback);
    }

    @Override
    public void sendMessage(Message message) {
        sendMessageNow(message);
    }

    private void requestMessages() {
        commsService.getMessages(10, 50000L, receiveCallback);
    }
}
