package com.intuso.housemate.web.client.bootstrap.widget.feature;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.*;

public class StatefulPlaybackControl extends Composite implements FeatureWidget, Value.Listener<GWTProxyValue>,
        com.intuso.housemate.client.v1_0.proxy.api.object.feature.StatefulPlaybackControl<GWTProxyCommand, GWTProxyValue> {

    interface PlaybackWidgetUiBinder extends UiBinder<Widget, StatefulPlaybackControl> {}

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

    public StatefulPlaybackControl(GWTProxyFeature feature) {
        this.feature = feature;
        initWidget(ourUiBinder.createAndBindUi(this));
        if(getIsPlayingValue() != null)
            getIsPlayingValue().addObjectListener(this);
    }

    @Override
    public void setTypes(GWTProxyList<Type.Data<?>, GWTProxyType> types) {
        playButton.setCommand(types, getPlayCommand());
        pauseButton.setCommand(types, getPauseCommand());
        stopButton.setCommand(types, getStopCommand());
        forwardButton.setCommand(types, getForwardCommand());
        rewindButton.setCommand(types, getRewindCommand());
    }

    @Override
    public void valueChanging(GWTProxyValue value) {
        // do nothing
    }

    @Override
    public void valueChanged(GWTProxyValue value) {
        if(isPlaying()) {
            playButton.setVisible(false);
            pauseButton.setVisible(true);
            stopButton.setVisible(true);
        } else {
            playButton.setVisible(true);
            pauseButton.setVisible(false);
            stopButton.setVisible(false);
        }
    }

    @Override
    public GWTProxyValue getIsPlayingValue() {
        return feature.getValues() != null ? feature.getValues().get("is-playing") : null;
    }

    @Override
    public boolean isPlaying() {
        GWTProxyValue value = getIsPlayingValue();
        return value != null
                && value.getValue() != null
                && value.getValue().getFirstValue() != null
                && Boolean.parseBoolean(value.getValue().getFirstValue());
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
