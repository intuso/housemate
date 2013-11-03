package com.intuso.housemate.web.client.bootstrap.widget.device.feature;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyValue;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

import java.util.Set;

public class StatefulPlaybackControl
        extends GWTProxyFeature
        implements com.intuso.housemate.api.object.device.feature.StatefulPlaybackControl<GWTProxyCommand, GWTProxyValue> {

    public StatefulPlaybackControl(GWTProxyDevice device) {
        super(device);
    }

    @Override
    public GWTProxyValue getIsPlayingValue() {
        return device.getValues() != null ? device.getValues().get(IS_PLAYING_VALUE) : null;
    }

    @Override
    public boolean isPlaying() {
        GWTProxyValue value = getIsPlayingValue();
        return value != null
                && value.getTypeInstances() != null
                && value.getTypeInstances().getFirstValue() != null
                && Boolean.parseBoolean(value.getTypeInstances().getFirstValue());
    }

    @Override
    public GWTProxyCommand getPlayCommand() {
        return device.getCommands() != null ? device.getCommands().get(PLAY_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getPauseCommand() {
        return device.getCommands() != null ? device.getCommands().get(PAUSE_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getStopCommand() {
        return device.getCommands() != null ? device.getCommands().get(STOP_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getForwardCommand() {
        return device.getCommands() != null ? device.getCommands().get(FORWARD_COMMAND) : null;
    }

    @Override
    public GWTProxyCommand getRewindCommand() {
        return device.getCommands() != null ? device.getCommands().get(REWIND_COMMAND) : null;
    }

    @Override
    public Set<String> getCommandIds() {
        return Sets.newHashSet(PLAY_COMMAND, PAUSE_COMMAND, STOP_COMMAND, FORWARD_COMMAND, REWIND_COMMAND);
    }

    @Override
    public Set<String> getValueIds() {
        return Sets.newHashSet(IS_PLAYING_VALUE);
    }

    @Override
    public Set<String> getPropertyIds() {
        return Sets.newHashSet();
    }

    @Override
    public String getTitle() {
        return "Playback";
    }

    @Override
    public Widget getWidget() {
        return new PlaybackWidget();
    }

    interface PlaybackWidgetUiBinder extends UiBinder<Widget, PlaybackWidget> {
    }

    private static PlaybackWidgetUiBinder ourUiBinder = GWT.create(PlaybackWidgetUiBinder.class);

    public class PlaybackWidget extends Composite implements ValueListener<GWTProxyValue> {

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

        private PlaybackWidget() {
            initWidget(ourUiBinder.createAndBindUi(this));

            playButton.setCommand(getPlayCommand());
            pauseButton.setCommand(getPauseCommand());
            stopButton.setCommand(getStopCommand());
            forwardButton.setCommand(getForwardCommand());
            rewindButton.setCommand(getRewindCommand());

            if(getIsPlayingValue() != null)
                getIsPlayingValue().addObjectListener(this);
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
    }
}
