package com.intuso.housemate.platform.android.service.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import com.intuso.housemate.platform.android.service.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppService extends Service implements ServiceConnection {

    private final static int NOTIFICATION_ID = AppService.class.getName().hashCode();

    private final Messenger messenger = null; // todo, either delete this and apps use tcp to connect to phone's broker, or we find/implement a custom transprt that uses the binder

    private Logger logger;

    public AppService() {
//        this.messenger = new Messenger(new MessageHandler());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, getNotification(/*ConnectionStatus.DisconnectedPermanently*/));
        startConnectionService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(logger == null) {
            logger = null;
            unbindService(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        ConnectionService.Binder binder = (ConnectionService.Binder) iBinder;
        logger = LoggerFactory.getLogger(AppService.class);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        logger = null;
        startConnectionService();
    }

    private void startConnectionService() {
        Intent intent = new Intent(this, ConnectionService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    private Notification getNotification(/*ConnectionStatus connectionStatus*/) {
        Notification.Builder notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Housemate Server Connection");
        /*switch (connectionStatus) {
            case ConnectedToServer:
                notification.setContentText("Connected to server")
                    .setPriority(Notification.PRIORITY_MIN);
                break;
            case ConnectedToRouter:
                notification.setContentText("Connected to router")
                        .setPriority(Notification.PRIORITY_MIN);
                break;
            case Connecting:
                notification.setContentText("Connecting to server")
                        .setPriority(Notification.PRIORITY_MIN);
                break;
            case DisconnectedTemporarily:
                notification.setContentText("Disconnected temporarily - will automatically reconnect")
                        .setPriority(Notification.PRIORITY_MIN);
                break;
            case DisconnectedPermanently:
                notification.setContentText("Disconnected. Tap here to configure the connection settings")
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), HousemateActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                        .setPriority(Notification.PRIORITY_HIGH);
                break;
        }*/
        notification.setContentText("Running")
                .setPriority(Notification.PRIORITY_MIN);
        return notification.build();
    }
}

