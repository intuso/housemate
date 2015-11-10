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

public class VolumeControl extends Composite implements FeatureWidget,
        com.intuso.housemate.client.v1_0.proxy.api.feature.VolumeControl<GWTProxyCommand> {

    interface VolumeWidgetUiBinder extends UiBinder<Widget, VolumeControl> {}

    private static VolumeWidgetUiBinder ourUiBinder = GWT.create(VolumeWidgetUiBinder.class);

    @UiField
    public PerformButton muteButton;
    @UiField
    public PerformButton upButton;
    @UiField
    public PerformButton downButton;

    private final GWTProxyFeature feature;

    public VolumeControl(GWTProxyFeature feature) {
        this.feature = feature;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setTypes(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        muteButton.setCommand(types, getMuteCommand());
        upButton.setCommand(types, getVolumeUpCommand());
        downButton.setCommand(types, getVolumeDownCommand());
    }

    @Override
    public GWTProxyCommand getMuteCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("mute") : null;
    }

    @Override
    public GWTProxyCommand getVolumeUpCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("volume-up") : null;
    }

    @Override
    public GWTProxyCommand getVolumeDownCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("volume-down") : null;
    }
}
