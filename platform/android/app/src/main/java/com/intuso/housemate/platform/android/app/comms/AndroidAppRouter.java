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
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.ServerConnectionStatus;
import com.intuso.housemate.platform.android.common.JsonMessage;
import com.intuso.housemate.platform.android.common.MessageCodes;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

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
    private MessageSender messageSender;
    private Messenger sender;
    private String id;
    private final Messenger receiver;
    private final LinkedBlockingQueue<Message> outputQueue;
    private boolean shouldBeConnected = false;
    private ServerConnectionStatus lastStatus = ServerConnectionStatus.DisconnectedPermanently;

    @Inject
    public AndroidAppRouter(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Context context) {
        super(log, listenersFactory, properties);
        this.context = context;
        this.receiver = new Messenger(new MessageHandler());
        this.outputQueue = new LinkedBlockingQueue<Message>();
    }

    @Override
    public synchronized void connect() {
        if (shouldBeConnected)
            return;
        shouldBeConnected = true;
        getLog().d("Connecting service");
        _ensureConnected();
    }

    private void _ensureConnected() {
        if(bindThread != null || registerThread != null)
            return;
        bindThread = new BindThread();
        bindThread.start();
    }

    @Override
    public synchronized final void disconnect() {

        if(!shouldBeConnected)
            return;
        shouldBeConnected = false;

        _disconnect();
    }

    private void _disconnect() {
        _disconnect(true);
    }

    private synchronized void _disconnect(boolean reconnect) {

        if(bindThread == null && registerThread == null)
            return;

        getLog().d("Disconnecting service");

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
                getLog().d("Removing server registration");
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.UNREGISTER);
                msg.getData().putString("id", id);
                sender.send(msg);
                if(!reconnect)
                    id = null;
            } catch (RemoteException e) {
                getLog().e("Failed to send disconnect message to service", e);
            }
            context.unbindService(this);
            sender = null;
        }

        getLog().d("Disconnected");

        // set the connection status, and check if we should reconnect
        if(shouldBeConnected) {
            lastStatus = getServerConnectionStatus();
            setServerConnectionStatus(ServerConnectionStatus.DisconnectedTemporarily);
            if(reconnect)
                _ensureConnected();
        } else {
            setServerConnectionStatus(ServerConnectionStatus.DisconnectedPermanently);
            getLog().d("Should not be connected, leaving disconnected");
        }
    }

    @Override
    public void sendMessage(Message message) {
        _ensureConnected();
        outputQueue.add(message);
    }

    private void _sendMessage(Message<?> message) throws IOException {
        try {
            android.os.Message msg = android.os.Message.obtain(null, MessageCodes.SEND_MESSAGE);
            msg.getData().putString("id", id);
            msg.getData().putParcelable("message", new JsonMessage(message));
            sender.send(msg);
        } catch (RemoteException e) {
            throw new IOException("Failed to send message to Housemate service");
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
                    messageSender = new MessageSender();
                    if(getServerConnectionStatus() == ServerConnectionStatus.DisconnectedTemporarily)
                        setServerConnectionStatus(lastStatus);
                    // start the sender thread
                    messageSender.start();
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
            if(getServerConnectionStatus() != ServerConnectionStatus.DisconnectedTemporarily)
                setServerConnectionStatus(ServerConnectionStatus.Connecting);
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
                    msg.getData().putString("id", id);
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

    /**
     * Thread to read messages off the output queue and send them to the server
     * @author Tom Clabon
     *
     */
    private class MessageSender extends Thread {

        @Override
        public void run() {

            getLog().d("Starting the message sender");

            while(!isInterrupted()) {

                Message message;

                try {
                    // get the next message
                    message = outputQueue.take();
                    getLog().d("Sending message " + message.toString());
                    _sendMessage(message);
                } catch(InterruptedException e) {
                    break;
                } catch(IOException e) {
                    getLog().e("Error sending message to client", e);
                    _disconnect();
                    break;
                }
            }

            getLog().d("Stopped message sender");
        }
    }
}
