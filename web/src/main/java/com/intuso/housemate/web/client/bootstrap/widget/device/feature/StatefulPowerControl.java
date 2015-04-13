package com.intuso.housemate.web.client.bootstrap.widget.device.feature;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.web.client.bootstrap.extensions.CommandToggleSwitch;
import com.intuso.housemate.web.client.object.*;
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
    public Widget getWidget(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        return new StatefulPowerControlWidget();
    }

    public class StatefulPowerControlWidget extends CommandToggleSwitch {

        private StatefulPowerControlWidget() {
            setOnText("On");
            setOffText("Off");
            setValue(getIsOnValue());
        }

        @Override
        protected boolean isTrue() {
            return isOn();
        }

        @Override
        protected GWTProxyCommand getTrueCommand() {
            return getOnCommand();
        }

        @Override
        protected GWTProxyCommand getFalseCommand() {
            return getOffCommand();
        }
    }
}
