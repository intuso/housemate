package com.intuso.housemate.web.client.bootstrap.widget.feature;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.bootstrap.extensions.CommandToggleSwitch;
import com.intuso.housemate.web.client.object.*;

public class StatefulPowerControl extends CommandToggleSwitch implements FeatureWidget,
        com.intuso.housemate.client.v1_0.proxy.api.object.feature.StatefulPowerControl<GWTProxyCommand, GWTProxyValue> {

    private final GWTProxyFeature feature;

    public StatefulPowerControl(GWTProxyFeature feature) {
        this.feature = feature;
        setOnText("On");
        setOffText("Off");
        setValue(getIsOnValue());
    }

    @Override
    public void setTypes(GWTProxyList<Type.Data<?>, GWTProxyType> types) {

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

    @Override
    public GWTProxyCommand getOnCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("on") : null;
    }

    @Override
    public GWTProxyCommand getOffCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("off") : null;
    }

    @Override
    public GWTProxyValue getIsOnValue() {
        return feature.getValues() != null ? feature.getValues().get("is-on") : null;
    }

    @Override
    public boolean isOn() {
        GWTProxyValue value = getIsOnValue();
        return value != null
                && value.getValue() != null
                && value.getValue().getFirstValue() != null
                && Boolean.parseBoolean(value.getValue().getFirstValue());
    }
}
