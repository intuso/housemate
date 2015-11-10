package com.intuso.housemate.platform.android.app.object.feature;

import com.intuso.housemate.client.v1_0.proxy.api.feature.ProxyFeature;
import com.intuso.housemate.platform.android.app.object.AndroidProxyCommand;
import com.intuso.housemate.platform.android.app.object.AndroidProxyFeature;
import com.intuso.housemate.platform.android.app.object.AndroidProxyValue;

/**
 * Base and container class for simple proxy features
 */
public abstract class AndroidProxyFeatureImpl implements ProxyFeature {

    protected final AndroidProxyFeature feature;

    public AndroidProxyFeatureImpl(AndroidProxyFeature feature) {
        this.feature = feature;
    }

    public final AndroidProxyFeatureImpl getThis() {
        return this;
    }

    public final static class PowerControl
            extends AndroidProxyFeatureImpl
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.PowerControl<AndroidProxyCommand> {

        public PowerControl(AndroidProxyFeature feature) {
            super(feature);
        }

        @Override
        public AndroidProxyCommand getOnCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("on") : null;
        }

        @Override
        public AndroidProxyCommand getOffCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("off") : null;
        }
    }

    public final static class StatefulPowerControl
            extends AndroidProxyFeatureImpl
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.StatefulPowerControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulPowerControl(AndroidProxyFeature feature) {
            super(feature);
        }

        @Override
        public AndroidProxyCommand getOnCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("on") : null;
        }

        @Override
        public AndroidProxyCommand getOffCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("off") : null;
        }

        @Override
        public AndroidProxyValue getIsOnValue() {
            return feature.getValues() != null ? feature.getValues().get("is-on") : null;
        }

        @Override
        public boolean isOn() {
            AndroidProxyValue value = getIsOnValue();
            return value != null
                    && value.getValue() != null
                    && value.getValue().getFirstValue() != null
                    && Boolean.parseBoolean(value.getValue().getFirstValue());
        }
    }

    public final static class PlaybackControl
            extends AndroidProxyFeatureImpl
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.PlaybackControl<AndroidProxyCommand> {

        public PlaybackControl(AndroidProxyFeature feature) {
            super(feature);
        }

        @Override
        public AndroidProxyCommand getPlayCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("play") : null;
        }

        @Override
        public AndroidProxyCommand getPauseCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("pause") : null;
        }

        @Override
        public AndroidProxyCommand getStopCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("stop") : null;
        }

        @Override
        public AndroidProxyCommand getForwardCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("rewind") : null;
        }

        @Override
        public AndroidProxyCommand getRewindCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("forward") : null;
        }
    }

    public final static class StatefulPlaybackControl
            extends AndroidProxyFeatureImpl
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.StatefulPlaybackControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulPlaybackControl(AndroidProxyFeature feature) {
            super(feature);
        }

        @Override
        public AndroidProxyValue getIsPlayingValue() {
            return feature.getValues() != null ? feature.getValues().get("is-playing") : null;
        }

        @Override
        public boolean isPlaying() {
            AndroidProxyValue value = getIsPlayingValue();
            return value != null
                    && value.getValue() != null
                    && value.getValue().getFirstValue() != null
                    && Boolean.parseBoolean(value.getValue().getFirstValue());
        }

        @Override
        public AndroidProxyCommand getPlayCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("play") : null;
        }

        @Override
        public AndroidProxyCommand getPauseCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("pause") : null;
        }

        @Override
        public AndroidProxyCommand getStopCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("stop") : null;
        }

        @Override
        public AndroidProxyCommand getForwardCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("rewind") : null;
        }

        @Override
        public AndroidProxyCommand getRewindCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("forward") : null;
        }
    }

    public final static class VolumeControl
            extends AndroidProxyFeatureImpl
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.VolumeControl<AndroidProxyCommand> {

        public VolumeControl(AndroidProxyFeature feature) {
            super(feature);
        }

        @Override
        public AndroidProxyCommand getMuteCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("mute") : null;
        }

        @Override
        public AndroidProxyCommand getVolumeUpCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("volume-up") : null;
        }

        @Override
        public AndroidProxyCommand getVolumeDownCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("volume-down") : null;
        }
    }

    public final static class StatefulVolumeControl
            extends AndroidProxyFeatureImpl
            implements com.intuso.housemate.client.v1_0.proxy.api.feature.StatefulVolumeControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulVolumeControl(AndroidProxyFeature feature) {
            super(feature);
        }

        @Override
        public AndroidProxyCommand getMuteCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("mute") : null;
        }

        @Override
        public AndroidProxyValue getCurrentVolumeValue() {
            return feature.getValues() != null ? feature.getValues().get("current-volume") : null;
        }

        @Override
        public AndroidProxyCommand getVolumeUpCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("volume-up") : null;
        }

        @Override
        public AndroidProxyCommand getVolumeDownCommand() {
            return feature.getCommands() != null ? feature.getCommands().get("volume-down") : null;
        }
    }
}
