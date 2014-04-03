package com.intuso.housemate.web.server;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.Sender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 */
public class GWTClientEndpoint implements Receiver, Sender {
    
    private final LinkedBlockingQueue<Message> q = new LinkedBlockingQueue<Message>();
    private long timeout;
    private long lastRequest = Long.MAX_VALUE;
    private final Router.Registration registration;

    public GWTClientEndpoint(Router router) {
        this.registration = router.registerReceiver(this);
    }

    @Override
    public void messageReceived(Message message) {
        if(System.currentTimeMillis() > (lastRequest + (2 * timeout)))
            disconnect();
        else
            q.add(message);
    }

    public void disconnect() {
        q.clear();
        registration.unregister();
    }

    @Override
    public void sendMessage(Message message) {
        registration.sendMessage(message);
    }

    public List<Message> getMessages(int max, long timeout) {

        this.timeout = timeout;
        lastRequest = System.currentTimeMillis();

        // get all the messages
        List<Message> result = new ArrayList<Message>();
        q.drainTo(result, max);
        
        // if there aren't any, wait for one
        if(result.size() == 0) {
            try {
                Message<?> message = q.poll(timeout, TimeUnit.MILLISECONDS);
                if(message != null)
                    result.add(message);
                // don't care, just means we didn't receive a message before the timeout so we return an empty list
            } catch(InterruptedException e) {}
        }

        // return whatever we have
        return result;
    }
}
