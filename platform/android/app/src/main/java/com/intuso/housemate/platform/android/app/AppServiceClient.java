package com.intuso.housemate.platform.android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.messaging.api.MessagingException;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class AppServiceClient implements ServiceConnection, Receiver.Factory, Sender.Factory {

    private final static Intent SERVER_INTENT = new Intent("com.intuso.housemate.service");

    private final Logger logger;
    private final Context context;
    private final Messenger receiver = new Messenger(new MessageHandler());
    private final Map<String, ReceiverImpl> receivers = new ConcurrentHashMap<>();
    private final Map<String, SenderImpl> senders = new ConcurrentHashMap<>();
    private final JsonSerialiser jsonSerialiser = new JsonSerialiser();

    private Messenger messenger;
    private String clientId;

    public AppServiceClient(Logger logger, Context context) {
        this.logger = logger;
        this.context = context;
    }

    private void trySend(Message message) {
        if(messenger == null)
            logger.error("Cannot send message, no messenger. Service is not connected yet");
        else {
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                logger.error("Failed to send message to client", e);
            }
        }
    }

    public void connect() {
        if(messenger == null) {
            logger.debug("Connecting to app service");
            context.startService(SERVER_INTENT);
            context.bindService(SERVER_INTENT, this, Context.BIND_AUTO_CREATE);
        }
    }

    public void disconnect() {
        logger.debug("Disconnecting from app service");
        if(messenger != null) {
            logger.debug("Removing server registration");
            messenger = null;
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.UNREGISTER);
            msg.getData().putString("id", clientId);
            trySend(msg);
            context.unbindService(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
        logger.debug("Service connected");
        messenger = new Messenger(binder);
        logger.debug("Creating server registration");
        android.os.Message msg = android.os.Message.obtain(null, MessageCodes.REGISTER);
        msg.replyTo = receiver;
        trySend(msg);
    }

    @Override
    public void onServiceDisconnected(ComponentName className) {
        if(messenger != null) {
            messenger = null;
            logger.debug("Service connection lost unexpectedly, reconnecting");
            connect();
        }
    }

    @Override
    public <T extends Serializable> Receiver<T> create(Logger logger, String name, Class<T> tClass) {
        return new ReceiverImpl<>(logger, name, tClass);
    }

    @Override
    public Sender create(Logger logger, String name) {
        return new SenderImpl(logger, name);
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            String receiverId, senderId;
            switch (msg.what) {
                case MessageCodes.ON_MESSAGE_RECEIVED:
                    receiverId = msg.getData().getString("receiver-id");
                    msg.getData().setClassLoader(Object.Data.class.getClassLoader());
                    if(receivers.containsKey(receiverId))
                        receivers.get(receiverId).onMessageReceived(
                                msg.getData().getString("json"),
                                msg.getData().getBoolean("persisted", false)
                        );
                    else
                        logger.error("Received message for unknown receiver {}", receiverId);
                    break;
                case MessageCodes.GOT_MESSAGE:
                    receiverId = msg.getData().getString("receiver-id");
                    msg.getData().setClassLoader(Object.Data.class.getClassLoader());
                    if(receivers.containsKey(receiverId))
                        receivers.get(receiverId).gotMessage(msg.getData().getString("json"));
                    else
                        logger.error("Received message for unknown receiver {}", receiverId);
                    break;
                case MessageCodes.GOT_MESSAGES:
                    receiverId = msg.getData().getString("receiver-id");
                    msg.getData().setClassLoader(Object.Data.class.getClassLoader());
                    List<String> jsons = msg.getData().getStringArrayList("jsons");
                    if(receivers.containsKey(receiverId))
                        receivers.get(receiverId).gotMessages(jsons);
                    else
                        logger.error("Received message for unknown receiver {}", receiverId);
                    break;
                case MessageCodes.OPENED_RECEIVER:
                    receiverId = msg.getData().getString("receiver-id");
                    logger.debug("Opened receiver {}", receiverId);
                    if(receivers.containsKey(receiverId))
                        receivers.get(receiverId).opened();
                    else
                        logger.error("Opened unknown receiver {}", receiverId);
                    break;
                case MessageCodes.OPENED_SENDER:
                    senderId = msg.getData().getString("sender-id");
                    logger.debug("Opened sender {}", senderId);
                    if(senders.containsKey(senderId))
                        senders.get(senderId).opened();
                    else
                        logger.error("Opened unknown sender {}", senderId);
                    break;
                case MessageCodes.REGISTERED:
                    logger.debug("Registration created");
                    clientId = msg.getData().getString("id");
                    break;
                case MessageCodes.CONNECTED:
                    logger.debug("Connected");
                    // todo tell some sort of listener
                case MessageCodes.DISCONNECTED:
                    logger.debug("Disconnected");
                    // todo tell some sort of listener
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Created by tomc on 15/03/17.
     */
    public class ReceiverImpl<T extends Serializable> implements Receiver<T> {

        private final Logger logger;
        private final String name;
        private final Class<T> tClass;

        private final String receiverId = UUID.randomUUID().toString();
        private final LinkedBlockingQueue<T> gotMessage = new LinkedBlockingQueue<>();
        private final LinkedBlockingQueue<Iterator<T>> gotMessages = new LinkedBlockingQueue<>();

        private boolean open = false;
        private Listener<T> listener;

        public ReceiverImpl(Logger logger, String name, Class<T> tClass) {
            this.logger = logger;
            this.name = name;
            this.tClass = tClass;
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.OPEN_RECEIVER);
            msg.getData().putString("client-id", clientId);
            msg.getData().putString("receiver-id", receiverId);
            msg.getData().putString("name", name);
            logger.debug("Opening receiver {}, ({})", receiverId, name);
            trySend(msg);
        }

        void opened() {
            this.open = true;
            logger.debug("Opened receiver {}, ({})", receiverId, name);
        }

        @Override
        public void close() {
            open = false;
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.CLOSE_RECEIVER);
            msg.getData().putString("client-id", clientId);
            msg.getData().putString("receiver-id", receiverId);
            logger.debug("Closed receiver {}, ({})", receiverId, name);
            trySend(msg);
        }

        private void onMessageReceived(String json, boolean persisted) {
            try {
                T t = jsonSerialiser.deserialise(json, tClass);
                if (listener != null)
                    listener.onMessage(t, persisted);
            } catch(Throwable t) {
                logger.error("Failed to handle json message for receiver {} ({})", receiverId, name, t);
                logger.debug("Json was:\n{}", json);
            }
        }

        private void gotMessage(String json) {
            try {
                T t = jsonSerialiser.deserialise(json, tClass);
                gotMessage.offer(t);
            } catch(Throwable t) {
                logger.error("Failed to handle json message for receiver {} ({})", receiverId, name, t);
                logger.debug("Json was:\n{}", json);
                // this is required to prevent the caller poll blocking forever
                gotMessage.offer(null);
            }
        }

        private void gotMessages(List<String> jsons) {
            List<T> tMessages = new ArrayList<>();
            for(String json : jsons) {
                try {
                    T t = jsonSerialiser.deserialise(json, tClass);
                    tMessages.add(t);
                } catch(Throwable t) {
                    logger.error("Failed to handle json message for receiver {} ({})", receiverId, name, t);
                    logger.debug("Json was:\n{}", json);
                }
            }
            gotMessages.offer(tMessages.iterator());
        }

        @Override
        public T getMessage() {
            if(!open)
                throw new MessagingException("Receiver is not open");
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.GET_MESSAGE);
            msg.getData().putString("client-id", clientId);
            msg.getData().putString("receiver-id", receiverId);
            logger.debug("Getting message on receiver {}, ({})", receiverId, name);
            trySend(msg);
            return gotMessage.poll();
        }

        @Override
        public Iterator<T> getMessages() {
            if(!open)
                throw new MessagingException("Receiver is not open");
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.GET_MESSAGES);
            msg.getData().putString("client-id", clientId);
            msg.getData().putString("receiver-id", receiverId);
            logger.debug("Getting messages on receiver {}, ({})", receiverId, name);
            trySend(msg);
            return gotMessages.poll();
        }

        @Override
        public void listen(Listener<T> listener) {
            if(!open)
                throw new MessagingException("Receiver is not open");
            this.listener = listener;
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.LISTEN);
            msg.getData().putString("client-id", clientId);
            msg.getData().putString("receiver-id", receiverId);
            logger.debug("Listening for messages on receiver {}, ({})", receiverId, name);
            trySend(msg);
        }
    }

    /**
     * Created by tomc on 15/03/17.
     */
    public class SenderImpl implements Sender {

        private final Logger logger;
        private final String name;

        private final String senderId = UUID.randomUUID().toString();
        private boolean open = false;

        public SenderImpl(Logger logger, String name) {
            this.logger = logger;
            this.name = name;
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.OPEN_RECEIVER);
            msg.getData().putString("client-id", clientId);
            msg.getData().putString("sender-id", senderId);
            msg.getData().putString("name", name);
            logger.debug("Opening sender {}, ({})", senderId, name);
            trySend(msg);
        }

        void opened() {
            this.open = true;
            logger.debug("Opened sender {}, ({})", senderId, name);
        }

        @Override
        public void close() {
            open = false;
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.CLOSE_SENDER);
            msg.getData().putString("client-id", clientId);
            msg.getData().putString("sender-id", senderId);
            logger.debug("Closed sender {}, ({})", senderId, name);
            trySend(msg);
        }

        @Override
        public void send(Serializable object, boolean persistent) {
            if(open) {
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.SEND_MESSAGE);
                msg.getData().putString("client-id", clientId);
                msg.getData().putString("sender-id", senderId);
                msg.getData().putString("message-id", UUID.randomUUID().toString());
                msg.getData().putString("json", jsonSerialiser.serialise(object));
                msg.getData().putBoolean("persistent", persistent);
                logger.debug("Sending message for sender {}, ({})", senderId, name);
                trySend(msg);
            } else
                logger.error("Not sending message, sender is not open");
        }
    }
}
