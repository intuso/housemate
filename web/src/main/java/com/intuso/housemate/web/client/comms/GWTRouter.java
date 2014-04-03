package com.intuso.housemate.web.client.comms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.web.client.NotConnectedException;
import com.intuso.housemate.web.client.service.CommsService;
import com.intuso.housemate.web.client.service.CommsServiceAsync;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 */
public class GWTRouter extends Router {

    private AsyncCallback<Void> sendCallback = new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable throwable) {
            if(throwable instanceof NotConnectedException)
                Window.alert("Connection timed out due to inactivity, reconnecting");
            else
                Window.alert("Reconnecting because of unknown error sending a message: " + throwable.getMessage());
            setServerConnectionStatus(ServerConnectionStatus.Disconnected);
            getLog().e("Failed to send message", throwable);
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
            setServerConnectionStatus(ServerConnectionStatus.Disconnected);
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

    private final CommsServiceAsync commsService = GWT.create(CommsService.class);

    /**
     * Create a new comms instance
     *
     * @throws com.intuso.housemate.api.HousemateException
     *          if an error occurs creating the comms instance
     */
    @Inject
    public GWTRouter(Log log, ListenersFactory listenersFactory, PropertyRepository properties) {
        super(log, listenersFactory, properties);
    }

    @Override
    public void connect() {
        setServerConnectionStatus(ServerConnectionStatus.Connecting);
        commsService.connectClient(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                setServerConnectionStatus(ServerConnectionStatus.Disconnected);
            }

            @Override
            public void onSuccess(Void aVoid) {
                setServerConnectionStatus(ServerConnectionStatus.ConnectedToRouter);
                // start getting messages
                requestMessages();
            }
        });
    }

    @Override
    public void disconnect() {
        setServerConnectionStatus(ServerConnectionStatus.Disconnected);
        commsService.disconnectClient(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                // do nothing
            }

            @Override
            public void onSuccess(Void result) {
                // do nothing
            }
        });
    }

    @Override
    public void sendMessage(Message message) {
        getLog().d("Sending message " + message.toString());
        commsService.sendMessageToServer(message, sendCallback);
    }

    private void requestMessages() {
        commsService.getMessages(10, 50000L, receiveCallback);
    }
}