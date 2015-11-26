package com.intuso.housemate.web.client.comms;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.intuso.housemate.comms.v1_0.api.BaseRouter;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.web.client.NotConnectedException;
import com.intuso.housemate.web.client.service.CommsServiceAsync;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

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
            getLog().e("Failed to send message", throwable);
            disconnect();
        }

        @Override
        public void onSuccess(Void aVoid) {
            getLog().d("Sent message to server");
        }
    };

    private AsyncCallback<Message<Message.Payload>[]> receiveCallback = new AsyncCallback<Message<Message.Payload>[]>() {
        @Override
        public void onFailure(Throwable throwable) {
            if(throwable instanceof NotConnectedException)
                Window.alert("Connection timed out due to inactivity, reconnecting");
            else
                Window.alert("Reconnecting because of unknown error getting messages: " + throwable.getMessage());
            getLog().e("Failed to receive message", throwable);
            disconnect();
        }

        @Override
        public void onSuccess(Message<Message.Payload>[] messages) {
            // pass messages on
            for(Message message : messages) {
                getLog().d("Message received " + message.toString());
                try {
                    messageReceived(message);
                } catch(Throwable t) {
                    getLog().e("Failed to process received message", t);
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
    public GWTRouter(Log log, ListenersFactory listenersFactory, CommsServiceAsync commsService) {
        super(log, listenersFactory);
        this.commsService = commsService;
    }

    @Override
    public void connect() {
        connecting();
        commsService.connectClient(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                getLog().e("Failed to connect", throwable);
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
                getLog().e("Failed to disconnect", throwable);
            }

            @Override
            public void onSuccess(Void result) {
                // do nothing
            }
        });
    }

    @Override
    protected void sendMessageNow(Message<?> message) {
        getLog().d("Sending message " + message.toString());
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
