package com.intuso.housemate.web.client.bootstrap.extensions;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.object.v1_0.api.Value;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
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
        implements Command.PerformListener<GWTProxyCommand>, Value.Listener<GWTProxyValue>, ValueChangeHandler<Boolean> {

    protected final void setValue(GWTProxyValue value) {
        value.addObjectListener(this);
        setState(isTrue(), true);
        addValueChangeHandler(this);
    }

    @Override
    public void valueChanging(GWTProxyValue value) {
        // do nothing
    }

    @Override
    public void valueChanged(GWTProxyValue value) {
        setState(isTrue(), true);
    }

    @Override
    public void commandStarted(GWTProxyCommand command) {
        // do nothing
    }

    @Override
    public void commandFinished(GWTProxyCommand command) {
        setReadonly(false);
        setIndeterminate(false);
        setState(isTrue(), true);
    }

    @Override
    public void commandFailed(GWTProxyCommand command, String error) {
        Window.alert("Command " + command.getName() + " failed to complete: " + error);
        commandFinished(command);
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        setReadonly(true);
        setIndeterminate(true);
        Housemate.INJECTOR.getEventBus().fireEvent(new PerformCommandEvent(event.getValue() ? getTrueCommand() : getFalseCommand(), null, this));
    }

    protected abstract boolean isTrue();
    protected abstract GWTProxyCommand getTrueCommand();
    protected abstract GWTProxyCommand getFalseCommand();
}

