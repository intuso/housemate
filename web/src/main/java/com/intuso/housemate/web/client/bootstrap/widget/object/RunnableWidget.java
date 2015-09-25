package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.intuso.housemate.client.v1_0.proxy.api.ProxyRunnable;
import com.intuso.housemate.web.client.bootstrap.extensions.CommandToggleSwitch;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyValue;

public class RunnableWidget extends CommandToggleSwitch {

    private ProxyRunnable<GWTProxyCommand, GWTProxyValue> object;

    public RunnableWidget() {
        setOnText("Running");
        setOffText("Stopped");
    }

    public RunnableWidget(ProxyRunnable<GWTProxyCommand, GWTProxyValue> object) {
        this();
        setObject(object);
    }

    public void setObject(ProxyRunnable<GWTProxyCommand, GWTProxyValue> object) {

        this.object = object;

        GWTProxyValue isRunning = object.getRunningValue();
        if(isRunning != null)
            setValue(isRunning);
    }

    @Override
    protected boolean isTrue() {
        return object.isRunning();
    }

    @Override
    protected GWTProxyCommand getTrueCommand() {
        return object.getStartCommand();
    }

    @Override
    protected GWTProxyCommand getFalseCommand() {
        return object.getStopCommand();
    }
}
