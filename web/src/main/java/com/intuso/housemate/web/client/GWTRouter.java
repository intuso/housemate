package com.intuso.housemate.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.web.client.service.CommsService;
import com.intuso.housemate.web.client.service.CommsServiceAsync;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTRouter extends Router {

    private AsyncCallback<Void> sendCallback = new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable throwable) {
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
            Window.alert("Failed to get messages. The error handling of this will be improved in the future. For now, please refresh the page");
            // todo improve this
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

    private final AsyncCallback<Void> connectCallback;
    private final CommsServiceAsync commsService = GWT.create(CommsService.class);

    /**
     * Create a new comms instance
     *
     * @throws com.intuso.housemate.api.HousemateException
     *          if an error occurs creating the comms instance
     */
    protected GWTRouter(Log log, AsyncCallback<Void> connectCallback) {
        super(log);
        this.connectCallback = connectCallback;
    }

    @Override
    public void connect() {
        setRouterStatus(Status.Connecting);
        commsService.connectClient(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                setRouterStatus(Status.Disconnected);
                connectCallback.onFailure(throwable);
            }

            @Override
            public void onSuccess(Void aVoid) {
                setRouterStatus(Status.Connected);
                connectCallback.onSuccess(aVoid);
                // start getting messages
                requestMessages();
            }
        });
    }

    @Override
    public void disconnect() {
        setRouterStatus(Status.Disconnected);
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
