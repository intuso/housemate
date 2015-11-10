package com.intuso.housemate.web.client.bootstrap.widget.feature;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyFeature;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

public class PowerControl extends Composite implements FeatureWidget,
        com.intuso.housemate.client.v1_0.proxy.api.feature.PowerControl<GWTProxyCommand> {

    interface PowerControlWidgetUiBinder extends UiBinder<Widget, PowerControl> {}

    private static PowerControlWidgetUiBinder ourUiBinder = GWT.create(PowerControlWidgetUiBinder.class);

    @UiField
    public PerformButton onButton;
    @UiField
    public PerformButton offButton;

    private final GWTProxyFeature feature;

    public PowerControl(GWTProxyFeature feature) {
        this.feature = feature;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setTypes(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        onButton.setCommand(types, getOnCommand());
        offButton.setCommand(types, getOffCommand());
    }

    @Override
    public GWTProxyCommand getOnCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("on") : null;
    }

    @Override
    public GWTProxyCommand getOffCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("off") : null;
    }
}

