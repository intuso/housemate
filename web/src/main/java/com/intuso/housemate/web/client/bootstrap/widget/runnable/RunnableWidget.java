package com.intuso.housemate.web.client.bootstrap.widget.runnable;

import com.intuso.housemate.api.object.Runnable;
import com.intuso.housemate.web.client.bootstrap.extensions.CommandToggleSwitch;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyValue;

public class RunnableWidget extends CommandToggleSwitch {

    private Runnable<GWTProxyCommand, GWTProxyValue> object;

    public RunnableWidget() {
        setTrueLabel("Running");
        setFalseLabel("Stopped");
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
        return object.getStopCommand();
    }

    @Override
    protected GWTProxyCommand getFalseCommand() {
        return object.getStartCommand();
    }
}
