package com.intuso.housemate.web.client.bootstrap.extensions;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 10/02/14
 * Time: 18:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class CommandToggleSwitch
        extends ToggleSwitch
        implements CommandPerformListener<GWTProxyCommand>, ValueListener<GWTProxyValue>, ValueChangeHandler<Boolean> {

    protected final void setValue(GWTProxyValue value) {
        value.addObjectListener(this);
        setValue(!isTrue());
        addValueChangeHandler(this);
    }

    @Override
    public void valueChanging(GWTProxyValue value) {
        // do nothing
    }

    @Override
    public void valueChanged(GWTProxyValue value) {
        setValue(!isTrue());
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
            getTrueCommand().perform(this);
        else
            getFalseCommand().perform(this);
    }

    protected abstract boolean isTrue();
    protected abstract GWTProxyCommand getTrueCommand();
    protected abstract GWTProxyCommand getFalseCommand();
}

