package com.intuso.housemate.web.client.bootstrap.widget.device.feature;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.*;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

import java.util.Set;

public class StatefulVolumeControl
        extends GWTProxyFeature
        implements com.intuso.housemate.api.object.device.feature.StatefulVolumeControl<GWTProxyCommand, GWTProxyValue> {

    public StatefulVolumeControl(GWTProxyDevice device) {
        super(device);
    }

    @Override
    public GWTProxyValue getCurrentVolumeValue() {
        return device.getValues() != null ? device.getValues().get(CURRENT_VOLUME_VALUE) : null;
    }

    @Override
    public GWTProxyCommand getMuteCommand() {
        return device.getCommands() != null ? device.getCommands().get(MUTE_COMMAND) : null;
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
        return Sets.newHashSet(CURRENT_VOLUME_VALUE);
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
    public Widget getWidget(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        return new VolumeWidget(types);
    }

    interface VolumeWidgetUiBinder extends UiBinder<Widget, VolumeWidget> {
    }

    private static VolumeWidgetUiBinder ourUiBinder = GWT.create(VolumeWidgetUiBinder.class);

    public class VolumeWidget extends Composite {

        @UiField
        public PerformButton muteButton;
        @UiField
        public PerformButton upButton;
        @UiField
        public PerformButton downButton;

        private VolumeWidget(GWTProxyList<TypeData<?>, GWTProxyType> types) {
            initWidget(ourUiBinder.createAndBindUi(this));

            muteButton.setCommand(types, getMuteCommand());
            upButton.setCommand(types, getVolumeUpCommand());
            downButton.setCommand(types, getVolumeDownCommand());
        }
    }
}
