package com.intuso.housemate.object.proxy.simple;

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
public abstract class SimpleProxyFeature implements ProxyFeature<SimpleProxyFeature, SimpleProxyDevice> {

    protected final SimpleProxyDevice device;

    public SimpleProxyFeature(SimpleProxyDevice device) {
        this.device = device;
    }

    public final SimpleProxyFeature getThis() {
        return this;
    }

    public void load(final FeatureLoadedListener<SimpleProxyDevice, SimpleProxyFeature> listener) {
        List<HousemateObject.TreeLoadInfo> treeInfos = Lists.newArrayList();
        if(getCommandIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.COMMANDS_ID, getCommandIds()));
        if(getValueIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.VALUES_ID, getValueIds()));
        if(getPropertyIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.PROPERTIES_ID, getPropertyIds()));
        device.load(new LoadManager(new LoadManager.Callback() {
            @Override
            public void failed(HousemateObject.TreeLoadInfo failed) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void allLoaded() {
                listener.featureLoaded(device, SimpleProxyFeature.this);
            }
        }, "featureLoader-" + listener.hashCode(), treeInfos));
    }

    private HousemateObject.TreeLoadInfo makeTreeInfo(String objectName, Set<String> childNames) {
        Map<String, HousemateObject.TreeLoadInfo> children = Maps.newHashMap();
        for(String childName : childNames)
            children.put(childName, new HousemateObject.TreeLoadInfo(childName, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
        return new HousemateObject.TreeLoadInfo(objectName, children);
    }

    public final static class PowerControl
            extends SimpleProxyFeature
            implements com.intuso.housemate.api.object.device.feature.PowerControl<SimpleProxyCommand> {

        public PowerControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get(ON_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getOffCommand() {
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
            extends SimpleProxyFeature
            implements com.intuso.housemate.api.object.device.feature.StatefulPowerControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulPowerControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get(ON_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getOffCommand() {
            return device.getCommands() != null ? device.getCommands().get(OFF_COMMAND) : null;
        }

        @Override
        public SimpleProxyValue getIsOnValue() {
            return device.getValues() != null ? device.getValues().get(IS_ON_VALUE) : null;
        }

        @Override
        public boolean isOn() {
            SimpleProxyValue value = getIsOnValue();
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
            extends SimpleProxyFeature
            implements com.intuso.housemate.api.object.device.feature.PlaybackControl<SimpleProxyCommand> {

        public PlaybackControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getPlayCommand() {
            return device.getCommands() != null ? device.getCommands().get(PLAY_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get(PAUSE_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get(STOP_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get(REWIND_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getRewindCommand() {
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
            extends SimpleProxyFeature
            implements com.intuso.housemate.api.object.device.feature.StatefulPlaybackControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulPlaybackControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyValue getIsPlayingValue() {
            return device.getValues() != null ? device.getValues().get(IS_PLAYING_VALUE) : null;
        }

        @Override
        public boolean isPlaying() {
            SimpleProxyValue value = getIsPlayingValue();
            return value != null
                    && value.getTypeInstances() != null
                    && value.getTypeInstances().getFirstValue() != null
                    && Boolean.parseBoolean(value.getTypeInstances().getFirstValue());
        }

        @Override
        public SimpleProxyCommand getPlayCommand() {
            return device.getCommands() != null ? device.getCommands().get(PLAY_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get(PAUSE_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get(STOP_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get(REWIND_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getRewindCommand() {
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
            extends SimpleProxyFeature
            implements com.intuso.housemate.api.object.device.feature.VolumeControl<SimpleProxyCommand> {

        public VolumeControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getMuteCommand() {
            return device.getCommands() != null ? device.getCommands().get(MUTE_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get(VOLUME_UP_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getVolumeDownCommand() {
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
            extends SimpleProxyFeature
            implements com.intuso.housemate.api.object.device.feature.StatefulVolumeControl<SimpleProxyCommand, SimpleProxyValue> {

        public StatefulVolumeControl(SimpleProxyDevice device) {
            super(device);
        }

        @Override
        public SimpleProxyCommand getMuteCommand() {
            return device.getCommands() != null ? device.getCommands().get(MUTE_COMMAND) : null;
        }

        @Override
        public SimpleProxyValue getCurrentVolumeValue() {
            return device.getValues() != null ? device.getValues().get(CURRENT_VOLUME_VALUE) : null;
        }

        @Override
        public SimpleProxyCommand getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get(VOLUME_UP_COMMAND) : null;
        }

        @Override
        public SimpleProxyCommand getVolumeDownCommand() {
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
