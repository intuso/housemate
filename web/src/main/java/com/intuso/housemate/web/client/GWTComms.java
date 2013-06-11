package com.intuso.housemate.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.web.client.event.LoggedInEvent;
import com.intuso.housemate.web.client.service.CommsService;
import com.intuso.housemate.web.client.service.CommsServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 14/03/12
 * Time: 23:28
 * To change this template use File | Settings | File Templates.
 */
public class GWTComms extends Router {

    private AsyncCallback<Void> sendCallback = new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable throwable) {
            getLog().e("Failed to send message");
            getLog().st(throwable);
        }

        @Override
        public void onSuccess(Void aVoid) {
            getLog().d("Sent message to server");
        }
    };

    private AsyncCallback<Message<Message.Payload>[]> receiveCallback = new AsyncCallback<Message<Message.Payload>[]>() {
        @Override
        public void onFailure(Throwable throwable) {
            getLog().e("Failed to get messages");
            Housemate.FACTORY.getEventBus().fireEvent(new LoggedInEvent(false));
        }

        @Override
        public void onSuccess(Message<Message.Payload>[] messages) {
            // pass messages on
            for(Message message : messages) {
                getLog().d("Message received " + message.toString());
                try {
                    messageReceived(message);
                } catch(HousemateException e) {
                    getLog().e("Failed to process received message");
                    getLog().st(e);
                }
            }

            // get more
            requestMessages();
        }
    };
    
    private CommsServiceAsync commsService = GWT.create(CommsService.class);

    /**
     * Create a new comms instance
     *
     * @throws com.intuso.housemate.api.HousemateException
     *          if an error occurs creating the comms instance
     */
    protected GWTComms(Resources resources) {
        super(resources);
    }

    public void connect(AuthenticationMethod method) {
        super.connect(method);

        // start getting messages
        requestMessages();
    }

    @Override
    public void disconnect() {
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
        commsService.sendMessageToBroker(message, sendCallback);
    }

    private void requestMessages() {
        commsService.getMessages(10, 50000L, receiveCallback);
    }
}
