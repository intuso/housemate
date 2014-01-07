package com.intuso.housemate.platform.android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.FromRouter;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.RouterRootObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.platform.android.common.MessageCodes;
import com.intuso.housemate.platform.android.common.ParcelableMessage;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class AndroidAppRouter extends Router implements ServiceConnection, RootListener<RouterRootObject> {

    private final Context context;
    private Messenger sender;
    private String id;
    private final Messenger receiver = new Messenger(new MessageHandler());

    AndroidAppRouter(Log log, Context context) {
        super(log);
        this.context = context;
        addObjectListener(this);
    }

    @Override
    public void connect() {
        getLog().d("Connecting service");
        if(sender == null)
            context.bindService(new Intent("com.intuso.housemate.service"), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void disconnect() {
        getLog().d("Disconnecting service");
        if(sender != null)
            context.unbindService(this);
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(sender != null)
            try {
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.SEND_MESSAGE);
                msg.getData().putString("id", id);
                msg.getData().putParcelable("message", new ParcelableMessage(message));
                sender.send(msg);
            } catch (RemoteException e) {
                throw new HousemateRuntimeException("Failed to send message to Housemate service");
            }
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
        getLog().d("Service connected");
        sender = new Messenger(binder);
        try {
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.CONNECT);
            msg.replyTo = receiver;
            sender.send(msg);
        } catch (RemoteException e) {
            getLog().e("Failed to connect to service", e);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        getLog().d("Service disconnected");
        sender = null;
    }

    @Override
    public void connectionStatusChanged(RouterRootObject root, ConnectionStatus status) {
        if(status == ConnectionStatus.Unauthenticated)
            login(new FromRouter());
    }

    @Override
    public void newServerInstance(RouterRootObject root) {
        // TODO
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MessageCodes.SEND_MESSAGE:
                    msg.getData().setClassLoader(Message.class.getClassLoader());
                    try {
                        messageReceived(((ParcelableMessage)msg.getData().getParcelable("message")).getMessage());
                    } catch (HousemateException e) {
                        getLog().e("Failed to process received message", e);
                    }
                    break;
                case MessageCodes.CONNECTED:
                    id = msg.getData().getString("id");
                    setRouterStatus(Status.Connected);
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
