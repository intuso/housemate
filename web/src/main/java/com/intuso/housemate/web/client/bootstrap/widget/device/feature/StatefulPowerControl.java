package com.intuso.housemate.web.client.bootstrap.widget.device.feature;

import com.google.common.collect.Sets;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.extensions.ToggleSwitch;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyValue;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

import java.util.Set;

public class StatefulPowerControl
        extends GWTProxyFeature
        implements com.intuso.housemate.api.object.device.feature.StatefulPowerControl<GWTProxyCommand, GWTProxyValue> {

    public StatefulPowerControl(GWTProxyDevice device) {
        super(device);
    }

    @Override
    public GWTProxyCommand getOnCommand() {
        return device.getCommands() != null ? device.getCommands().get(ON_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getOffCommand() {
        return device.getCommands() != null ? device.getCommands().get(OFF_COMMAND) : null;
    }

    @Override
    public GWTProxyValue getIsOnValue() {
        return device.getValues() != null ? device.getValues().get(IS_ON_VALUE) : null;
    }

    @Override
    public boolean isOn() {
        GWTProxyValue value = getIsOnValue();
        return value != null
                && value.getTypeInstances() != null
                && value.getTypeInstances().getFirstValue() != null
                && Boolean.parseBoolean(value.getTypeInstances().getFirstValue());
    }

    @Override
    public Set<String> getCommandIds() {
        return Sets.newHashSet(ON_COMMAND, OFF_COMMAND);
    }

    @Override
    public Set<String> getValueIds() {
        return Sets.newHashSet(IS_ON_VALUE);
    }

    @Override
    public Set<String> getPropertyIds() {
        return Sets.newHashSet(IS_ON_VALUE);
    }

    @Override
    public String getTitle() {
        return "Power";
    }

    @Override
    public Widget getWidget() {
        return new StatefulPowerControlWidget();
    }

    public class StatefulPowerControlWidget extends ToggleSwitch
            implements ValueListener<GWTProxyValue>, CommandListener<GWTProxyCommand>,ValueChangeHandler<Boolean> {

        private StatefulPowerControlWidget() {

            setOnLabel("On");
            setOffLabel("Off");

            GWTProxyValue isOn = getIsOnValue();
            if(isOn != null)
                isOn.addObjectListener(this);
            setValue(isOn());

            if(getOnCommand() != null && getOffCommand() != null) {
                getOnCommand().addObjectListener(this);
                getOffCommand().addObjectListener(this);
                commandEnabled(/* args not used so can be anything */ null, false);
            }

            addValueChangeHandler(this);
        }

        @Override
        public void valueChanging(GWTProxyValue value) {
            // do nothing
        }

        @Override
        public void valueChanged(GWTProxyValue value) {
            setValue(isOn());
            setEnabled(true);
        }

        @Override
        public void commandEnabled(GWTProxyCommand command, boolean enabled) {
            setEnabled(getOnCommand().isEnabled() && getOffCommand().isEnabled());
        }

        @Override
        public void commandStarted(GWTProxyCommand command, String user) {
            // do nothing
        }

        @Override
        public void commandFinished(GWTProxyCommand command) {
            setEnabled(true);
        }

        @Override
        public void commandFailed(GWTProxyCommand command, String error) {
            setEnabled(true);
        }

        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
            setEnabled(false);
            Housemate.INJECTOR.getEventBus().fireEvent(new PerformCommandEvent(event.getValue() ? getOnCommand() : getOffCommand(), null));
        }
    }
}
