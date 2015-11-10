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

public class PlaybackControl extends Composite implements FeatureWidget,
        com.intuso.housemate.client.v1_0.proxy.api.feature.PlaybackControl<GWTProxyCommand> {


    interface PlaybackWidgetUiBinder extends UiBinder<Widget, PlaybackControl> {}

    private static PlaybackWidgetUiBinder ourUiBinder = GWT.create(PlaybackWidgetUiBinder.class);

    @UiField
    public PerformButton playButton;
    @UiField
    public PerformButton pauseButton;
    @UiField
    public PerformButton stopButton;
    @UiField
    public PerformButton forwardButton;
    @UiField
    public PerformButton rewindButton;

    private final GWTProxyFeature feature;

    public PlaybackControl(GWTProxyFeature feature) {
        this.feature = feature;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setTypes(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        playButton.setCommand(types, getPlayCommand());
        pauseButton.setCommand(types, getPauseCommand());
        stopButton.setCommand(types, getStopCommand());
        forwardButton.setCommand(types, getForwardCommand());
        rewindButton.setCommand(types, getRewindCommand());
    }

    @Override
    public GWTProxyCommand getPlayCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("play") : null;
    }

    @Override
    public GWTProxyCommand getPauseCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("pause") : null;
    }

    @Override
    public GWTProxyCommand getStopCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("stop") : null;
    }

    @Override
    public GWTProxyCommand getForwardCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("forward") : null;
    }

    @Override
    public GWTProxyCommand getRewindCommand() {
        return feature.getCommands() != null ? feature.getCommands().get("rewind") : null;
    }
}
