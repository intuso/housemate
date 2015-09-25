package com.intuso.housemate.web.client.bootstrap.widget.device.feature;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

import java.util.Set;

public class PowerControl
        extends GWTProxyFeature
        implements com.intuso.housemate.object.v1_0.api.feature.PowerControl<GWTProxyCommand> {

    public PowerControl(GWTProxyDevice device) {
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
    public Set<String> getCommandIds() {
        return Sets.newHashSet("on", "off");
    }

    @Override
    public Set<String> getValueIds() {
        return Sets.newHashSet();
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
        return new PowerControlWidget(types);
    }

    interface PowerControlWidgetUiBinder extends UiBinder<Widget, PowerControlWidget> {}

    private static PowerControlWidgetUiBinder ourUiBinder = GWT.create(PowerControlWidgetUiBinder.class);



    public class PowerControlWidget extends Composite {

        @UiField
        public PerformButton onButton;
        @UiField
        public PerformButton offButton;

        private PowerControlWidget(GWTProxyList<TypeData<?>, GWTProxyType> types) {

            initWidget(ourUiBinder.createAndBindUi(this));

            onButton.setCommand(types, getOnCommand());
            offButton.setCommand(types, getOffCommand());
        }
    }
}

