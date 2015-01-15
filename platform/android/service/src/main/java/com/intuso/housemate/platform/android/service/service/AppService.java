package com.intuso.housemate.platform.android.service.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.platform.android.common.JsonMessage;
import com.intuso.housemate.platform.android.common.MessageCodes;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.UUID;

public class AppService extends Service implements ServiceConnection {

    private final Map<String, Router.Registration> clientReceivers = Maps.newHashMap();
    private final Messenger messenger;

    private Log log;
    private Router router;

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
        Intent intent = new Intent(this, ConnectionService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(log == null) {
            log = null;
            router = null;
            unbindService(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        ConnectionService.Binder binder = (ConnectionService.Binder) iBinder;
        log = binder.getLog();
        router = binder.getRouter();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        log = null;
        router = null;
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {

            if(router == null)
                return;

            Router.Registration registration;
            String id;
            switch (msg.what) {
                case MessageCodes.REGISTER:
                    id = msg.getData().getString("id");
                    if(id == null)
                        id = UUID.randomUUID().toString();
                    while(clientReceivers.containsKey(id))
                        id = UUID.randomUUID().toString();
                    log.d("Registering client " + id);
                    try {
                        android.os.Message reply = android.os.Message.obtain();
                        reply.what = MessageCodes.REGISTERED;
                        reply.getData().putString("id", id);
                        msg.replyTo.send(reply);
                    } catch (RemoteException e) {
                        log.e("Failed to send message to client", e);
                    }
                    clientReceivers.put(id, router.registerReceiver(new ClientReceiver(id, msg.replyTo)));
                    break;
                case MessageCodes.UNREGISTER:
                    id = msg.getData().getString("id");
                    log.d("Unregistering client " + id);
                    registration = clientReceivers.remove(msg.getData().getString("id"));
                    if(registration != null)
                        registration.unregister();
                    else
                        log.e("Could not find client with id " + id);
                    break;
                case MessageCodes.SEND_MESSAGE:
                    msg.getData().setClassLoader(Message.class.getClassLoader());
                    id = msg.getData().getString("id");
                    registration = clientReceivers.get(id);
                    if(registration != null) {
                        Message<?> message;
                        try {
                            message = ((JsonMessage) msg.getData().getParcelable("message")).getMessage();
                        } catch(Throwable t) {
                            log.e("Failed to get message from parcelable");
                            break;
                        }
                        registration.sendMessage(message);
                    } else
                        log.e("Could not find registration for client " + id);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private class ClientReceiver implements Receiver<Message.Payload> {

        private final String id;
        private final Messenger clientReceiver;

        private ClientReceiver(String id, Messenger clientReceiver) {
            this.id = id;
            this.clientReceiver = clientReceiver;
        }

        @Override
        public void messageReceived(Message<Message.Payload> message) throws HousemateException {
            try {
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.SEND_MESSAGE);
                msg.getData().putParcelable("message", new JsonMessage(message));
                clientReceiver.send(msg);
            } catch(DeadObjectException e) {
                log.d("Client no longer connected");
                clientReceivers.get(id).unregister();
            } catch(RemoteException e) {
                log.e("Failed to send message to client", e);
            } catch(Throwable t) {
                log.e("Problem sending message to client", t);
            }
        }
    }
}

