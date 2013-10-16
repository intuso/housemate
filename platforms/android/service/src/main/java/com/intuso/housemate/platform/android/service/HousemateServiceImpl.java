package com.intuso.housemate.platform.android.service;

import android.content.Intent;
import android.os.IBinder;
import com.intuso.home.platform.android.common.HousemateService;
import com.intuso.housemate.api.authentication.UsernamePassword;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.root.Root;

public class HousemateServiceImpl extends HousemateService {

    private final IBinder binder;
    private final AndroidServiceEnvironment environment;

    public HousemateServiceImpl() {
        binder = new HousemateService.Binder(this);
        environment = new AndroidServiceEnvironment();
    }

    AndroidServiceEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
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

    @Override
    public void login(Root<?> router) {
        router.login(new UsernamePassword(
                environment.getResources().getProperties().get(AndroidServiceEnvironment.USERNAME),
                environment.getResources().getProperties().get(AndroidServiceEnvironment.PASSWORD),
                false));
    }

    @Override
    public void sendMessage(Message<?> message) {
        environment.getResources().getRouter().sendMessage(message);
    }
}

