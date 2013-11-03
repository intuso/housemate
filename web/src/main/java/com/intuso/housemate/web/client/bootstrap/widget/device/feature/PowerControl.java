package com.intuso.housemate.web.client.bootstrap.widget.device.feature;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

import java.util.Set;

public class PowerControl
        extends GWTProxyFeature
        implements com.intuso.housemate.api.object.device.feature.PowerControl<GWTProxyCommand> {

    public PowerControl(GWTProxyDevice device) {
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
    public Set<String> getCommandIds() {
        return Sets.newHashSet(ON_COMMAND, OFF_COMMAND);
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
    public Widget getWidget() {
        return new PowerControlWidget();
    }

    interface PowerControlWidgetUiBinder extends UiBinder<Widget, PowerControlWidget> {}

    private static PowerControlWidgetUiBinder ourUiBinder = GWT.create(PowerControlWidgetUiBinder.class);



    public class PowerControlWidget extends Composite {

        @UiField
        public PerformButton onButton;
        @UiField
        public PerformButton offButton;

        private PowerControlWidget() {

            initWidget(ourUiBinder.createAndBindUi(this));

            onButton.setCommand(getOnCommand());
            offButton.setCommand(getOffCommand());
        }
    }
}

