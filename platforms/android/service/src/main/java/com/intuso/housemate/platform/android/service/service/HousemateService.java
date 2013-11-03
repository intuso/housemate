package com.intuso.housemate.platform.android.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.platform.android.common.MessageCodes;
import com.intuso.housemate.platform.android.common.ParcelableMessage;

import java.util.Map;
import java.util.UUID;

public class HousemateService extends Service {

    private final Messenger messenger;
    private final AndroidServiceEnvironment environment;

    private final Map<String, Router.Registration> clientReceivers = Maps.newHashMap();

    public HousemateService() {
        messenger = new Messenger(new MessageHandler());
        environment = new AndroidServiceEnvironment();
    }

    public Router getRouter() {
        return environment.getRouter();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        environment.init(this);
        environment.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        environment.stop();
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Router.Registration registration;
            switch (msg.what) {
                case MessageCodes.CONNECT:
                    String id = UUID.randomUUID().toString();
                    clientReceivers.put(id, environment.getRouter().registerReceiver(new ClientReceiver(msg.replyTo)));
                    try {
                        android.os.Message reply = android.os.Message.obtain();
                        reply.what = MessageCodes.CONNECTED;
                        reply.getData().putString("id", id);
                        msg.replyTo.send(reply);
                    } catch (RemoteException e) {
                        environment.getLog().e("Failed to send message to client");
                        environment.getLog().st(e);
                    }
                    break;
                case MessageCodes.DISCONNECT:
                    registration = clientReceivers.remove(msg.getData().getString("id"));
                    if(registration != null)
                        registration.remove();
                    break;
                case MessageCodes.SEND_MESSAGE:
                    msg.getData().setClassLoader(Message.class.getClassLoader());
                    registration = clientReceivers.get(msg.getData().getString("id"));
                    if(registration != null)
                        registration.sendMessage(((ParcelableMessage) msg.getData().getParcelable("message")).getMessage());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private class ClientReceiver implements Receiver<Message.Payload> {

        private final Messenger clientReceiver;

        private ClientReceiver(Messenger clientReceiver) {
            this.clientReceiver = clientReceiver;
        }

        @Override
        public void messageReceived(Message<Message.Payload> message) throws HousemateException {
            try {
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.SEND_MESSAGE);
                msg.getData().putParcelable("message", new ParcelableMessage(message));
                clientReceiver.send(msg);
            } catch(RemoteException e) {
                environment.getLog().e("Failed to send message to client");
                environment.getLog().st(e);
            }
        }
    }
}

