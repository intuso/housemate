package com.intuso.housemate.web.client.bootstrap.widget.runnable;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.intuso.housemate.api.object.RunnableObject;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyValue;

public class RunnableWidget
        extends ToggleSwitch
        implements CommandListener<GWTProxyCommand>, ValueListener<GWTProxyValue>,ValueChangeHandler<Boolean> {

    private RunnableObject<GWTProxyCommand, GWTProxyValue> object;

    public void setObject(RunnableObject<GWTProxyCommand, GWTProxyValue> object) {

        setOnLabel("start");
        setOffLabel("stop");

        this.object = object;

        GWTProxyValue isRunning = object.getRunningValue();
        if(isRunning != null)
            isRunning.addObjectListener(this);

        setValue(!object.isRunning());

        addValueChangeHandler(this);
    }

    @Override
    public void valueChanging(GWTProxyValue value) {
        // do nothing
    }

    @Override
    public void valueChanged(GWTProxyValue value) {
        setValue(!object.isRunning());
        setEnabled(true);
    }

    @Override
    public void commandStarted(GWTProxyCommand command) {
        // do nothing
    }

    @Override
    public void commandFinished(GWTProxyCommand command) {
        setEnabled(true);
    }

    @Override
    public void commandFailed(GWTProxyCommand command, String error) {
        setEnabled(true);
        // todo notify the failure
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        setEnabled(false);
        if(event.getValue())
            object.getStopCommand().perform(this);
        else
            object.getStartCommand().perform(this);
    }
}
