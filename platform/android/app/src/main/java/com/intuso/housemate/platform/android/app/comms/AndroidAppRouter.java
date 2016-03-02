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
import com.intuso.housemate.comms.v1_0.api.BaseRouter;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus;
import com.intuso.housemate.platform.android.common.JsonMessage;
import com.intuso.housemate.platform.android.common.MessageCodes;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class AndroidAppRouter extends BaseRouter<AndroidAppRouter> implements ServiceConnection {

    private final static Intent SERVER_INTENT = new Intent("com.intuso.housemate.service");

    private final Context context;
    private BindThread bindThread;
    private RegisterThread registerThread;
    private MessageSender messageSender;
    private Messenger sender;
    private String id;
    private final Messenger receiver;
    private final LinkedBlockingQueue<Message> outputQueue;
    private boolean registered = false;

    private String serverInstanceId;
    private boolean shouldBeConnected = false;

    @Inject
    public AndroidAppRouter(Logger logger, ListenersFactory listenersFactory, Context context) {
        super(logger, listenersFactory);
        this.context = context;
        this.receiver = new Messenger(new MessageHandler());
        this.outputQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public synchronized void connect() {

        getLogger().debug("App Router: connect()");

        if (shouldBeConnected)
            return;
        shouldBeConnected = true;

        checkConnection();
    }

    @Override
    public synchronized final void disconnect() {

        getLogger().debug("App Router: disconnect()");

        if(!shouldBeConnected)
            return;
        shouldBeConnected = false;

        checkConnection();
    }

    @Override
    public void sendMessageNow(Message message) {
        try {
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.SEND_MESSAGE);
            msg.getData().putString("id", id);
            msg.getData().putParcelable("message", new JsonMessage(message));
            sender.send(msg);
        } catch (RemoteException e) {
            throw new HousemateCommsException("Failed to send message to Housemate service");
        }
    }

    @Override
    public void sendMessage(Message message) {
        checkConnection();
        outputQueue.add(message);
    }

    public synchronized void checkConnection() {
        if(shouldBeConnected)
            _ensureConnected();
        else
            _disconnect(false);
    }

    private void _ensureConnected() {
        if(registered || bindThread != null || registerThread != null)
            return;

        connecting();
        bindThread = new BindThread();
        bindThread.start();
    }

    private synchronized void _disconnect(boolean willReconnect) {

        if(!registered && bindThread == null && registerThread == null)
            return;

        getLogger().debug("App Router: Disconnecting from service");

        connectionLost(willReconnect);

        if(bindThread != null) {
            bindThread.interrupt();
            bindThread = null;
        }
        if(registerThread != null) {
            registerThread.interrupt();
            registerThread = null;
        }
        if(messageSender != null) {
            messageSender.interrupt();
            messageSender = null;
        }
        if(sender != null) {
            try {
                getLogger().debug("App Router: Unregistering");
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.UNREGISTER);
                registered = false;
                msg.getData().putString("id", id);
                sender.send(msg);
                id = null;
            } catch (RemoteException e) {
                getLogger().warn("App Router: Failed to send disconnect message from service", e);
            }
            context.unbindService(this);
            registered = false;
            sender = null;
        }

        getLogger().debug("App Router: Disconnected");

        // set the connection status, and check if we should reconnect
        connectionLost(shouldBeConnected);
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {

        getLogger().debug("App Router: Service connected");

        if(bindThread != null) {

            getLogger().debug("App Router: Stopping BindThread");

            bindThread.interrupt();
            bindThread = null;
        }
        sender = new Messenger(binder);
        registerThread = new RegisterThread();

        getLogger().debug("App Router: Starting RegisterThread");

        registerThread.start();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        getLogger().debug("App Router: Service connection lost unexpectedly, trying to re-establish connection");
        _disconnect(true);
        checkConnection();
    }

    private class BindThread extends Thread {
        @Override
        public void run() {

            getLogger().debug("App Router: BindThread.start()");

            connecting();
            while(!isInterrupted() && sender == null) {
                context.startService(SERVER_INTENT);
                context.bindService(SERVER_INTENT, AndroidAppRouter.this, Context.BIND_AUTO_CREATE);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                    getLogger().debug("App Router: BindThread interrupted");

                    break;
                }
            }

            getLogger().debug("App Router: BindThread stopped");
        }
    }

    private class RegisterThread extends Thread {
        @Override
        public void run() {

            getLogger().debug("App Router: RegisterThread.start()");

            while(!isInterrupted()
                    && (getConnectionStatus() == ConnectionStatus.Connecting
                            || getConnectionStatus() == ConnectionStatus.DisconnectedTemporarily)) {
                try {
                    getLogger().debug("App Router: Creating server registration");
                    if(id == null) {
                        android.os.Message msg = android.os.Message.obtain(null, MessageCodes.CREATE_REGISTRATION);
                        msg.replyTo = receiver;
                        sender.send(msg);
                    } else {
                        android.os.Message msg = android.os.Message.obtain(null, MessageCodes.RE_REGISTER);
                        msg.getData().putString("id", id);
                        msg.replyTo = receiver;
                        sender.send(msg);
                    }
                } catch (RemoteException e) {
                    getLogger().error("App Router: Failed to send registration message", e);
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                    getLogger().debug("App Router: RegisterThread interrupted");

                    break;
                }
            }

            getLogger().debug("App Router: RegisterThread stopped");
        }
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MessageCodes.SEND_MESSAGE:
                    msg.getData().setClassLoader(JsonMessage.class.getClassLoader());
                    try {
                        messageReceived(((JsonMessage) msg.getData().getParcelable("message")).getMessage());
                    } catch (Throwable t) {
                        getLogger().error("App Router: Failed to receive message", t);
                        disconnect();
                        checkConnection();
                    }
                    break;
                case MessageCodes.REGISTERED:
                    getLogger().debug("App Router: Registration created");
                    registered = true;
                    if(registerThread != null) {
                        registerThread.interrupt();
                        registerThread = null;
                    }
                    id = msg.getData().getString("id");
                    connectionEstablished();
                    // start the sender thread
                    messageSender = new MessageSender();
                    messageSender.start();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Thread to read messages off the output queue and send them to the server
     * @author Tom Clabon
     *
     */
    private class MessageSender extends Thread {

        @Override
        public void run() {

            getLogger().debug("App Router: Starting the message sender");

            while(!isInterrupted()) {

                Message message;

                try {
                    // get the next message
                    message = outputQueue.take();
                    getLogger().debug("App Router: Sending message " + message.toString());
                    sendMessageNow(message);
                } catch(InterruptedException e) {
                    break;
                } catch(HousemateCommsException e) {
                    getLogger().error("App Router: Failed to send queued message to client", e);
                    _disconnect(true);
                    checkConnection();
                    break;
                }
            }

            getLogger().debug("App Router: Stopped message sender");
        }
    }
}
