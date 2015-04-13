package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.intuso.housemate.api.object.Runnable;
import com.intuso.housemate.web.client.bootstrap.extensions.CommandToggleSwitch;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyValue;

public class RunnableWidget extends CommandToggleSwitch {

    private Runnable<GWTProxyCommand, GWTProxyValue> object;

    public RunnableWidget() {
        setOnText("Running");
        setOffText("Stopped");
    }

    public RunnableWidget(Runnable<GWTProxyCommand, GWTProxyValue> object) {
        this();
        setObject(object);
    }

    public void setObject(Runnable<GWTProxyCommand, GWTProxyValue> object) {

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
