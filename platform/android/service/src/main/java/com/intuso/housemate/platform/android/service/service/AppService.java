package com.intuso.housemate.platform.android.service.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.messaging.mqtt.MQTTReceiver;
import com.intuso.housemate.client.v1_0.messaging.mqtt.MQTTSender;
import com.intuso.housemate.platform.android.app.MessageCodes;
import com.intuso.housemate.platform.android.service.comms.MQTTReceiverFactoryImpl;
import com.intuso.housemate.platform.android.service.comms.MQTTSenderFactoryImpl;
import com.intuso.housemate.platform.android.service.R;
import com.intuso.housemate.platform.android.service.activity.HousemateActivity;
import com.intuso.utilities.collection.ManagedCollection;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AppService extends Service implements ServiceConnection, ConnectionService.Listener {

    private final static int NOTIFICATION_ID = AppService.class.getName().hashCode();

    private final Messenger messenger;
    private final Map<String, Client> clients = new ConcurrentHashMap<>();

    private Logger logger;
    private ManagedCollection.Registration routerListenerRegistration;
    private Receiver.Factory receiverFactory;
    private Sender.Factory senderFactory;

    public AppService() {
        this.messenger = new Messenger(new MessageHandler());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, getNotification(false, false, false));
        startConnectionService();
    }

    @Override
    public void onDestroy() {
        for(Client client : clients.values())
            client.uninit();
        if(logger == null) {
            logger = null;
            unbindService(this);
        }
        super.onDestroy();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        ConnectionService.Binder binder = (ConnectionService.Binder) iBinder;
        logger = LoggerFactory.getLogger(AppService.class);
        routerListenerRegistration = binder.addListener(this, true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        routerListenerRegistration.remove();
        onChange(false, false, false);
        logger = null;
        startConnectionService();
    }

    @Override
    public void onChange(boolean clientAvailable, boolean networkAvailable, boolean clientConnected) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, getNotification(clientAvailable, networkAvailable, clientConnected));
    }

    @Override
    public void clientOpened(MqttClient client) {
        this.receiverFactory = new MQTTReceiver.FactoryImpl(new MQTTReceiverFactoryImpl(client));
        this.senderFactory = new MQTTSender.FactoryImpl(new MQTTSenderFactoryImpl(client));
        for(Map.Entry<String, Client> clientEntry : clients.entrySet())
            clientEntry.getValue().clientOpened();
    }

    @Override
    public void clientClosing(MqttClient client) {
        this.receiverFactory = null;
        this.senderFactory = null;
        for(Map.Entry<String, Client> clientEntry : clients.entrySet())
            clientEntry.getValue().clientClosing();
    }

    private void startConnectionService() {
        Intent intent = new Intent(this, ConnectionService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    private Notification getNotification(boolean clientAvailable, boolean networkAvailable, boolean clientConnected) {
        Notification.Builder notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Housemate Server Connection");
        if(!clientAvailable)
            notification.setContentText("Incorrect configuration. Tap to configure")
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), HousemateActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                    .setPriority(Notification.PRIORITY_MIN);
        else if(!networkAvailable)
            notification.setContentText("No internet. Messages will be delayed")
                    .setPriority(Notification.PRIORITY_MIN);
        else if(!clientConnected)
            notification.setContentText("Could not reach server. Tap to configure")
                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), HousemateActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                    .setPriority(Notification.PRIORITY_MIN);
        else
            notification.setContentText("Connected")
                    .setPriority(Notification.PRIORITY_MIN);
        return notification.build();
    }

    private void trySend(Messenger messenger, Message message) {
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            logger.error("Failed to send message to client", e);
        }
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == MessageCodes.REGISTER) {
                String clientId = UUID.randomUUID().toString();
                logger.debug("Registering new client " + clientId);
                Message reply = Message.obtain();
                reply.what = MessageCodes.REGISTERED;
                reply.getData().putString("client-id", clientId);
                trySend(msg.replyTo, reply);
                clients.put(clientId, new Client(msg.replyTo));
                return;
            }

            String clientId = msg.getData().getString("client-id");
            if(clientId == null) {
                // todo reply with no client id in message
                return;
            }

            Client client = clients.get(clientId);
            if(client == null) {
                // todo reply with unknown client
                return;
            }

            String name, receiverId, senderId;
            Message reply;

            switch (msg.what) {
                case MessageCodes.SEND_MESSAGE:
                    senderId = msg.getData().getString("sender-id");
                    String messageId = msg.getData().getString("message-id");
                    if(senderId == null) {
                        // todo tell client the sender id is null
                        return;
                    }
                    if(client.senders.get(senderId) == null) {
                        // todo tell client the sender is unknown
                        return;
                    }
                    msg.getData().setClassLoader(Message.class.getClassLoader());
                    try {
                        String json = msg.getData().getString("json");
                        boolean persistent = msg.getData().getBoolean("persistent", false);
                        client.senders.get(senderId).send(json, persistent);
                        reply = Message.obtain();
                        reply.what = MessageCodes.SENT_MESSAGE;
                        reply.getData().putString("sender-id", senderId);
                        reply.getData().putString("message-id", messageId);
                        trySend(msg.replyTo, reply);
                    } catch (Throwable t) {
                        logger.error("Failed to get message from parcelable");
                    }
                    return;
                case MessageCodes.GET_MESSAGE:
                    receiverId = msg.getData().getString("receiver-id");
                    if(receiverId == null) {
                        // todo tell client the receiver id is null
                        return;
                    }
                    if(client.receivers.get(receiverId) == null) {
                        // todo tell client the receiver is unknown
                        return;
                    }
                    String json = client.receivers.get(receiverId).getMessage();
                    reply = Message.obtain();
                    reply.what = MessageCodes.GOT_MESSAGE;
                    reply.getData().putString("receiver-id", receiverId);
                    if(json != null)
                        msg.getData().putString("json", json);
                    trySend(msg.replyTo, reply);
                    break;
                case MessageCodes.GET_MESSAGES:
                    receiverId = msg.getData().getString("receiver-id");
                    if(receiverId == null) {
                        // todo tell client the receiver id is null
                        return;
                    }
                    if(client.receivers.get(receiverId) == null) {
                        // todo tell client the receiver is unknown
                        return;
                    }
                    Iterator<String> strings = client.receivers.get(receiverId).getMessages();
                    ArrayList<String> jsons = new ArrayList<>();
                    while(strings.hasNext())
                        jsons.add(strings.next());
                    reply = Message.obtain();
                    reply.what = MessageCodes.GOT_MESSAGES;
                    reply.getData().putString("receiver-id", receiverId);
                    msg.getData().putStringArrayList("jsons", jsons);
                    trySend(msg.replyTo, reply);
                    break;
                case MessageCodes.LISTEN:
                    receiverId = msg.getData().getString("receiver-id");
                    if(receiverId == null) {
                        // todo tell client the receiver id is null
                        return;
                    }
                    if(client.receivers.get(receiverId) == null) {
                        // todo tell client the receiver is unknown
                        return;
                    }
                    client.listen(receiverId);
                    break;
                case MessageCodes.UNREGISTER:
                    logger.debug("Unregistering client " + clientId);
                    client.uninit();
                    return;
                case MessageCodes.OPEN_RECEIVER:
                    name = msg.getData().getString("name");
                    if(name == null) {
                        // todo tell client the name is null
                        return;
                    }
                    receiverId = msg.getData().getString("receiver-id");
                    client.openReceiver(receiverId, name);
                    reply = Message.obtain();
                    reply.what = MessageCodes.OPENED_RECEIVER;
                    reply.getData().putString("receiver-id", receiverId);
                    trySend(msg.replyTo, reply);
                    return;
                case MessageCodes.OPEN_SENDER:
                    name = msg.getData().getString("name");
                    if(name == null) {
                        // todo tell client the name is null
                        return;
                    }
                    senderId = msg.getData().getString("sender-id");
                    client.openSender(senderId, name);
                    reply = Message.obtain();
                    reply.what = MessageCodes.OPENED_SENDER;
                    reply.getData().putString("sender-id", senderId);
                    trySend(msg.replyTo, reply);
                    return;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private class Client {

        private final Messenger messenger;
        private final Map<String, Receiver<String>> receivers = new HashMap<>();
        private final Map<String, Sender> senders = new HashMap<>();

        private Client(Messenger messenger) {
            this.messenger = messenger;
        }

        private void clientOpened() {
            Message message = Message.obtain();
            message.what = MessageCodes.CONNECTED;
            trySend(messenger, message);
        }

        private void clientClosing() {
            Message message = Message.obtain();
            message.what = MessageCodes.DISCONNECTED;
            trySend(messenger, message);
            uninit();
        }

        private void uninit() {
            for(Receiver<String> receiver : receivers.values())
                receiver.close();
            for(Sender sender : senders.values())
                sender.close();
        }

        private void openReceiver(String id, String name) {
            receivers.put(id, receiverFactory.create(LoggerFactory.getLogger(logger.getName() + ".client." + id), name, String.class));
        }

        private void openSender(String id, String name) {
            senders.put(id, senderFactory.create(LoggerFactory.getLogger(logger.getName() + ".client." + id), name));
        }

        private void listen(String receiverId) {
            receivers.get(receiverId).listen(new ReceiverListenerImpl(receiverId));
        }

        private class ReceiverListenerImpl implements Receiver.Listener<String> {

            private final String receiverId;

            public ReceiverListenerImpl(String receiverId) {
                this.receiverId = receiverId;
            }

            public void onMessage(String json, boolean persistent) {
                try {
                    Message msg = Message.obtain(null, MessageCodes.ON_MESSAGE_RECEIVED);
                    msg.getData().putString("receiver-id", receiverId);
                    msg.getData().putString("json", json);
                    msg.getData().putBoolean("persistent", persistent);
                    messenger.send(msg);
                } catch(DeadObjectException e) {
                    logger.debug("Client no longer connected");
                    uninit();
                } catch(RemoteException e) {
                    logger.error("Failed to send message to client", e);
                } catch(Throwable t) {
                    logger.error("Problem sending message to client", t);
                }
            }
        }
    }
}

