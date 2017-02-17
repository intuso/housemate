package com.intuso.housemate.platform.android.service.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import com.intuso.housemate.platform.android.service.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppService extends Service implements ServiceConnection {

    private final static int NOTIFICATION_ID = AppService.class.getName().hashCode();

    private Logger logger;

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, getNotification());
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
        // todo what do we do with this? Can we find out the port to tell apps to connect to?
//        binder.getConnection();
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

    private Notification getNotification() {
        Notification.Builder notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Housemate Server Connection");
        // todo work out if we're connected or not.
        notification.setContentText("Running")
                .setPriority(Notification.PRIORITY_MIN);
        return notification.build();
    }
}

