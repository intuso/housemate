package com.intuso.housemate.platform.android.app.comms;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.platform.android.common.JsonMessage;
import com.intuso.housemate.platform.android.common.MessageCodes;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class AndroidAppRouter extends Router implements ServiceConnection {

    private final static Intent SERVER_INTENT = new Intent("com.intuso.housemate.service");

    private final Context context;
    private BindThread bindThread;
    private RegisterThread registerThread;
    private Messenger sender;
    private String id;
    private final Messenger receiver = new Messenger(new MessageHandler());

    @Inject
    public AndroidAppRouter(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Context context) {
        super(log, listenersFactory, properties);
        this.context = context;
    }

    @Override
    public void connect() {
        getLog().d("Connecting service");
        setServerConnectionStatus(ServerConnectionStatus.Connecting);
        bindThread = new BindThread();
        bindThread.start();
    }

    @Override
    public void disconnect() {
        getLog().d("Disconnecting service");
        if(bindThread != null) {
            bindThread.interrupt();
            bindThread = null;
        }
        if(registerThread != null) {
            registerThread.interrupt();
            registerThread = null;
        }
        if(sender != null) {
            try {
                getLog().d("Removing server registration");
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.UNREGISTER);
                msg.getData().putString("id", id);
                sender.send(msg);
            } catch (RemoteException e) {
                getLog().e("Failed to send disconnect message to service", e);
            }
            context.unbindService(this);
            sender = null;
        }
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(sender != null)
            try {
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.SEND_MESSAGE);
                msg.getData().putString("id", id);
                msg.getData().putParcelable("message", new JsonMessage(message));
                sender.send(msg);
            } catch (RemoteException e) {
                throw new HousemateRuntimeException("Failed to send message to Housemate service");
            }
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
        getLog().d("Service connected");
        if(bindThread != null) {
            bindThread.interrupt();
            bindThread = null;
        }
        sender = new Messenger(binder);
        registerThread = new RegisterThread();
        registerThread.start();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        getLog().d("Service connection lost unexpectedly, trying to re-establish connection");
        setServerConnectionStatus(ServerConnectionStatus.DisconnectedPermanently);
        disconnect();
        connect();
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MessageCodes.SEND_MESSAGE:
                    msg.getData().setClassLoader(Message.class.getClassLoader());
                    try {
                        messageReceived(((JsonMessage) msg.getData().getParcelable("message")).getMessage());
                    } catch (HousemateException e) {
                        getLog().e("Failed to receive message", e);
                    }
                    break;
                case MessageCodes.REGISTERED:
                    getLog().d("Registration created");
                    if(registerThread != null) {
                        registerThread.interrupt();
                        registerThread = null;
                    }
                    setServerConnectionStatus(ServerConnectionStatus.ConnectedToRouter);
                    id = msg.getData().getString("id");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private class BindThread extends Thread {
        @Override
        public void run() {
            while(!isInterrupted() && sender == null) {
                context.startService(SERVER_INTENT);
                context.bindService(SERVER_INTENT, AndroidAppRouter.this, Context.BIND_AUTO_CREATE);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    private class RegisterThread extends Thread {
        @Override
        public void run() {
            while(!isInterrupted() && getServerConnectionStatus() == ServerConnectionStatus.Connecting) {
                try {
                    getLog().d("Creating server registration");
                    android.os.Message msg = android.os.Message.obtain(null, MessageCodes.REGISTER);
                    msg.replyTo = receiver;
                    sender.send(msg);
                } catch (RemoteException e) {
                    getLog().e("Failed to connect to service", e);
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
