package com.intuso.housemate.platform.android.app.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.FeatureLoadedListener;
import com.intuso.housemate.client.v1_0.proxy.api.device.feature.ProxyFeature;
import com.intuso.housemate.comms.v1_0.api.RemoteObject;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base and container class for simple proxy features
 */
public abstract class AndroidProxyFeature implements ProxyFeature<AndroidProxyFeature, AndroidProxyDevice> {

    protected final AndroidProxyDevice device;

    public AndroidProxyFeature(AndroidProxyDevice device) {
        this.device = device;
    }

    public final AndroidProxyFeature getThis() {
        return this;
    }

    public void load(final FeatureLoadedListener<AndroidProxyDevice, AndroidProxyFeature> listener) {
        List<RemoteObject.TreeLoadInfo> treeInfos = Lists.newArrayList();
        if(getCommandIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.COMMANDS_ID, getCommandIds()));
        if(getValueIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.VALUES_ID, getValueIds()));
        if(getPropertyIds().size() > 0)
            treeInfos.add(makeTreeInfo(DeviceData.PROPERTIES_ID, getPropertyIds()));
        device.load(new LoadManager(new LoadManager.Callback() {
            @Override
            public void failed(List<String> errors) {
                listener.loadFailed(device, AndroidProxyFeature.this);
            }

            @Override
            public void succeeded() {
                listener.loadFinished(device, AndroidProxyFeature.this);
            }
        }, treeInfos));
    }

    private RemoteObject.TreeLoadInfo makeTreeInfo(String objectName, Set<String> childNames) {
        Map<String, RemoteObject.TreeLoadInfo> children = Maps.newHashMap();
        for(String childName : childNames)
            children.put(childName, new RemoteObject.TreeLoadInfo(childName, new RemoteObject.TreeLoadInfo(RemoteObject.EVERYTHING_RECURSIVE)));
        return new RemoteObject.TreeLoadInfo(objectName, children);
    }

    public final static class PowerControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.PowerControl<AndroidProxyCommand> {

        public PowerControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get("on") : null;
        }

        @Override
        public AndroidProxyCommand getOffCommand() {
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
    }

    public final static class StatefulPowerControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.StatefulPowerControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulPowerControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get("on") : null;
        }

        @Override
        public AndroidProxyCommand getOffCommand() {
            return device.getCommands() != null ? device.getCommands().get("off") : null;
        }

        @Override
        public AndroidProxyValue getIsOnValue() {
            return device.getValues() != null ? device.getValues().get("is-on") : null;
        }

        @Override
        public boolean isOn() {
            AndroidProxyValue value = getIsOnValue();
            return value != null
                    && value.getValue() != null
                    && value.getValue().getFirstValue() != null
                    && Boolean.parseBoolean(value.getValue().getFirstValue());
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("on", "off");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet("is-on");
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class PlaybackControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.PlaybackControl<AndroidProxyCommand> {

        public PlaybackControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getPlayCommand() {
            return device.getCommands() != null ? device.getCommands().get("play") : null;
        }

        @Override
        public AndroidProxyCommand getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get("pause") : null;
        }

        @Override
        public AndroidProxyCommand getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get("stop") : null;
        }

        @Override
        public AndroidProxyCommand getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get("rewind") : null;
        }

        @Override
        public AndroidProxyCommand getRewindCommand() {
            return device.getCommands() != null ? device.getCommands().get("forward") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("play", "pause", "stop", "forward", "rewind");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet();
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class StatefulPlaybackControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.StatefulPlaybackControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulPlaybackControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyValue getIsPlayingValue() {
            return device.getValues() != null ? device.getValues().get("is-playing") : null;
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
            return device.getCommands() != null ? device.getCommands().get("play") : null;
        }

        @Override
        public AndroidProxyCommand getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get("pause") : null;
        }

        @Override
        public AndroidProxyCommand getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get("stop") : null;
        }

        @Override
        public AndroidProxyCommand getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get("rewind") : null;
        }

        @Override
        public AndroidProxyCommand getRewindCommand() {
            return device.getCommands() != null ? device.getCommands().get("forward") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("play", "pause", "stop", "forward", "rewind");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet("is-playing");
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class VolumeControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.VolumeControl<AndroidProxyCommand> {

        public VolumeControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getMuteCommand() {
            return device.getCommands() != null ? device.getCommands().get("mute") : null;
        }

        @Override
        public AndroidProxyCommand getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get("volume-up") : null;
        }

        @Override
        public AndroidProxyCommand getVolumeDownCommand() {
            return device.getCommands() != null ? device.getCommands().get("volume-down") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("volume-up", "volume-down");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet();
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class StatefulVolumeControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.object.v1_0.api.feature.StatefulVolumeControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulVolumeControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getMuteCommand() {
            return device.getCommands() != null ? device.getCommands().get("mute") : null;
        }

        @Override
        public AndroidProxyValue getCurrentVolumeValue() {
            return device.getValues() != null ? device.getValues().get("current-volume") : null;
        }

        @Override
        public AndroidProxyCommand getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get("volume-up") : null;
        }

        @Override
        public AndroidProxyCommand getVolumeDownCommand() {
            return device.getCommands() != null ? device.getCommands().get("volume-down") : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet("volume-up", "volume-down");
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet("current-volume");
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }
}
