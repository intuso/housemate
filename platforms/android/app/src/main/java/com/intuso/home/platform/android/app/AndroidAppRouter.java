package com.intuso.home.platform.android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.intuso.home.platform.android.common.HousemateService;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/10/13
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class AndroidAppRouter extends Router implements ServiceConnection {

    private final Context context;
    private HousemateService service;

    AndroidAppRouter(Resources resources, Context context) {
        super(resources);
        this.context = context;
    }

    @Override
    public void connect() {
        getLog().d("Connecting service");
        if(service == null)
            context.bindService(new Intent(context, HousemateService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void disconnect() {
        getLog().d("Disconnecting service");
        if(service != null)
            context.unbindService(this);
    }

    @Override
    public void sendMessage(Message<?> message) {
        service.sendMessage(message);
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
        getLog().d("Service connected");
        service = ((HousemateService.Binder)binder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        getLog().d("Service disconnected");
        service = null;
    }
}
