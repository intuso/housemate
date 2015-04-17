package com.intuso.housemate.platform.android.app.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.device.feature.FeatureLoadedListener;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeature;

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
        List<HousemateObject.TreeLoadInfo> treeInfos = Lists.newArrayList();
        if(getCommandIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.COMMANDS_ID, getCommandIds()));
        if(getValueIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.VALUES_ID, getValueIds()));
        if(getPropertyIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.PROPERTIES_ID, getPropertyIds()));
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

    private HousemateObject.TreeLoadInfo makeTreeInfo(String objectName, Set<String> childNames) {
        Map<String, HousemateObject.TreeLoadInfo> children = Maps.newHashMap();
        for(String childName : childNames)
            children.put(childName, new HousemateObject.TreeLoadInfo(childName, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
        return new HousemateObject.TreeLoadInfo(objectName, children);
    }

    public final static class PowerControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.api.object.device.feature.PowerControl<AndroidProxyCommand> {

        public PowerControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get(ON_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getOffCommand() {
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
    }

    public final static class StatefulPowerControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.api.object.device.feature.StatefulPowerControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulPowerControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get(ON_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getOffCommand() {
            return device.getCommands() != null ? device.getCommands().get(OFF_COMMAND) : null;
        }

        @Override
        public AndroidProxyValue getIsOnValue() {
            return device.getValues() != null ? device.getValues().get(IS_ON_VALUE) : null;
        }

        @Override
        public boolean isOn() {
            AndroidProxyValue value = getIsOnValue();
            return value != null
                    && value.getTypeInstances() != null
                    && value.getTypeInstances().getFirstValue() != null
                    && Boolean.parseBoolean(value.getTypeInstances().getFirstValue());
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet(ON_COMMAND, OFF_COMMAND);
        }

        @Override
        public Set<String> getValueIds() {
            return Sets.newHashSet(IS_ON_VALUE);
        }

        @Override
        public Set<String> getPropertyIds() {
            return Sets.newHashSet();
        }
    }

    public final static class PlaybackControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.api.object.device.feature.PlaybackControl<AndroidProxyCommand> {

        public PlaybackControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getPlayCommand() {
            return device.getCommands() != null ? device.getCommands().get(PLAY_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get(PAUSE_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get(STOP_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get(REWIND_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getRewindCommand() {
            return device.getCommands() != null ? device.getCommands().get(FORWARD_COMMAND) : null;
        }

        @Override
        public Set<String> getCommandIds() {
            return Sets.newHashSet(PLAY_COMMAND, PAUSE_COMMAND, STOP_COMMAND, FORWARD_COMMAND, REWIND_COMMAND);
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
            implements com.intuso.housemate.api.object.device.feature.StatefulPlaybackControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulPlaybackControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyValue getIsPlayingValue() {
            return device.getValues() != null ? device.getValues().get(IS_PLAYING_VALUE) : null;
        }

        @Override
        public boolean isPlaying() {
            AndroidProxyValue value = getIsPlayingValue();
            return value != null
                    && value.getTypeInstances() != null
                    && value.getTypeInstances().getFirstValue() != null
                    && Boolean.parseBoolean(value.getTypeInstances().getFirstValue());
        }

        @Override
        public AndroidProxyCommand getPlayCommand() {
            return device.getCommands() != null ? device.getCommands().get(PLAY_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get(PAUSE_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get(STOP_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get(REWIND_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getRewindCommand() {
            return device.getCommands() != null ? device.getCommands().get(FORWARD_COMMAND) : null;
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
    }

    public final static class VolumeControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.api.object.device.feature.VolumeControl<AndroidProxyCommand> {

        public VolumeControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getMuteCommand() {
            return device.getCommands() != null ? device.getCommands().get(MUTE_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get(VOLUME_UP_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getVolumeDownCommand() {
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
    }

    public final static class StatefulVolumeControl
            extends AndroidProxyFeature
            implements com.intuso.housemate.api.object.device.feature.StatefulVolumeControl<AndroidProxyCommand, AndroidProxyValue> {

        public StatefulVolumeControl(AndroidProxyDevice device) {
            super(device);
        }

        @Override
        public AndroidProxyCommand getMuteCommand() {
            return device.getCommands() != null ? device.getCommands().get(MUTE_COMMAND) : null;
        }

        @Override
        public AndroidProxyValue getCurrentVolumeValue() {
            return device.getValues() != null ? device.getValues().get(CURRENT_VOLUME_VALUE) : null;
        }

        @Override
        public AndroidProxyCommand getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get(VOLUME_UP_COMMAND) : null;
        }

        @Override
        public AndroidProxyCommand getVolumeDownCommand() {
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
    }
}
