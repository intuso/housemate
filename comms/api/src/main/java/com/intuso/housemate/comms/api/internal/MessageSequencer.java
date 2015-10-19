package com.intuso.housemate.comms.api.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomc on 14/10/15.
 */
public class MessageSequencer implements Message.Receiver<Message.Payload> {

    private final Message.Receiver<Message.Payload> receiver;

    private long nextId = 0;
    private Map<Long, Message<Message.Payload>> queue = new HashMap<>();

    public MessageSequencer(Message.Receiver<Message.Payload> receiver) {
        this.receiver = receiver;
    }

    @Override
    public synchronized void messageReceived(Message<Message.Payload> message) {
        if(message.getSequenceId() == null)
            receiver.messageReceived(message);
        else if(message.getSequenceId() == nextId) {
            receiver.messageReceived(message);
            nextId++;
            while(queue.containsKey(nextId))
                receiver.messageReceived(queue.remove(nextId++));
        } else
            queue.put(message.getSequenceId(), message);
    }
}
