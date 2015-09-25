package com.intuso.housemate.web.client.bootstrap.widget.device.feature;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.extensions.CommandToggleSwitch;
import com.intuso.housemate.web.client.object.*;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

import java.util.Set;

public class StatefulPowerControl
        extends GWTProxyFeature
        implements com.intuso.housemate.object.v1_0.api.feature.StatefulPowerControl<GWTProxyCommand, GWTProxyValue> {

    public StatefulPowerControl(GWTProxyDevice device) {
        super(device);
    }

    @Override
    public GWTProxyCommand getOnCommand() {
        return device.getCommands() != null ? device.getCommands().get("on") : null;
    }

    @Override
    public GWTProxyCommand getOffCommand() {
        return device.getCommands() != null ? device.getCommands().get("off") : null;
    }

    @Override
    public GWTProxyValue getIsOnValue() {
        return device.getValues() != null ? device.getValues().get("is-on") : null;
    }

    @Override
    public boolean isOn() {
        GWTProxyValue value = getIsOnValue();
        return value != null
                && value.getValue() != null
                && value.getValue().getFirstValue() != null
                && Boolean.parseBoolean(value.getValue().getFirstValue());
    }

    @Override
    public Set<String> getCommandIds() {
        return Sets.newHashSet("on", "off");
    }

    @Override
    public Set<String> getValueIds() {
        return Sets.newHashSet("is-on");
    }

    @Override
    public Set<String> getPropertyIds() {
        return Sets.newHashSet();
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
