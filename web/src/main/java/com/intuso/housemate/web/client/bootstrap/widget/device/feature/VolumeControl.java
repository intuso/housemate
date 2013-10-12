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

public class VolumeControl
        extends GWTProxyFeature
        implements com.intuso.housemate.api.object.device.feature.VolumeControl<GWTProxyCommand> {

    public VolumeControl(GWTProxyDevice device) {
        super(device);
    }

    @Override
    public GWTProxyCommand getVolumeUpCommand() {
        return device.getCommands() != null ? device.getCommands().get(VOLUME_UP_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getVolumeDownCommand() {
        return device.getCommands() != null ? device.getCommands().get(VOLUME_DOWN_COMMAND) : null;
    }

    @Override
    public Set<String> getCommandIds() {
        return Sets.newHashSet(VOLUME_UP_COMMAND, VOLUME_DOWN_COMMAND);
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
        return "Volume";
    }

    @Override
    public Widget getWidget() {
        return new VolumeWidget();
    }

    interface VolumeWidgetUiBinder extends UiBinder<Widget, VolumeWidget> {
    }

    private static VolumeWidgetUiBinder ourUiBinder = GWT.create(VolumeWidgetUiBinder.class);

    public class VolumeWidget extends Composite {

        @UiField
        public PerformButton upButton;
        @UiField
        public PerformButton downButton;

        private VolumeWidget() {
            initWidget(ourUiBinder.createAndBindUi(this));

            upButton.setCommand(getVolumeUpCommand());
            downButton.setCommand(getVolumeDownCommand());
        }
    }
}
