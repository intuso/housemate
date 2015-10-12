package com.intuso.housemate.comms.api.internal;

import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomc on 05/10/15.
 */
public class MessageDistributor {

    private final ListenersFactory listenersFactory;
    private final Map<String, Listeners<Message.Receiver<?>>> messageListeners = new HashMap<>();

    public MessageDistributor(ListenersFactory listenersFactory) {
        this.listenersFactory = listenersFactory;
    }

    public ListenerRegistration registerReceiver(String type, Message.Receiver<?> receiver) {
        Listeners<Message.Receiver<?>> listeners = messageListeners.get(type);
        if(listeners == null) {
            listeners = listenersFactory.create();
            messageListeners.put(type, listeners);
        }
        return listeners.addListener(receiver);
    }

    public void distribute(Message<?> message) {
        Listeners<Message.Receiver<?>> listeners = messageListeners.get(message.getType());
        if(listeners == null)
            throw new HousemateCommsException("No listeners known for type \"" + message.getType() + "\" for object " + Arrays.toString(message.getPath()));
        for(Message.Receiver listener : listeners)
            listener.messageReceived(message);
    }
}
