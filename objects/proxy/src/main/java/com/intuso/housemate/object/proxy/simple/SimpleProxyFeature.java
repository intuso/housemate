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

public abstract class SimpleProxyFeature implements ProxyFeature<SimpleProxyFeature, SimpleProxyObject.Device> {

    protected final SimpleProxyObject.Device device;

    public SimpleProxyFeature(SimpleProxyObject.Device device) {
        this.device = device;
    }

    public final SimpleProxyFeature getThis() {
        return this;
    }

    public void load(final FeatureLoadedListener<SimpleProxyObject.Device, SimpleProxyFeature> listener) {
        List<HousemateObject.TreeLoadInfo> treeInfos = Lists.newArrayList();
        if(getCommandIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.COMMANDS_ID, getCommandIds()));
        if(getValueIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.VALUES_ID, getValueIds()));
        if(getPropertyIds().size() > 0)
            treeInfos.add(makeTreeInfo(Device.PROPERTIES_ID, getPropertyIds()));
        device.load(new LoadManager("featureLoader", treeInfos) {
            @Override
            protected void failed(HousemateObject.TreeLoadInfo failed) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            protected void allLoaded() {
                listener.featureLoaded(device, SimpleProxyFeature.this);
            }
        });
    }

    private HousemateObject.TreeLoadInfo makeTreeInfo(String objectName, Set<String> childNames) {
        Map<String, HousemateObject.TreeLoadInfo> children = Maps.newHashMap();
        for(String childName : childNames)
            children.put(childName, new HousemateObject.TreeLoadInfo(childName, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
        return new HousemateObject.TreeLoadInfo(objectName, children);
    }

    public final static class PowerControl
            extends SimpleProxyFeature
            implements com.intuso.housemate.api.object.device.feature.PowerControl<SimpleProxyObject.Command> {

        public PowerControl(SimpleProxyObject.Device device) {
            super(device);
        }

        @Override
        public SimpleProxyObject.Command getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get(ON_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getOffCommand() {
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
            implements com.intuso.housemate.api.object.device.feature.StatefulPowerControl<SimpleProxyObject.Command, SimpleProxyObject.Value> {

        public StatefulPowerControl(SimpleProxyObject.Device device) {
            super(device);
        }

        @Override
        public SimpleProxyObject.Command getOnCommand() {
            return device.getCommands() != null ? device.getCommands().get(ON_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getOffCommand() {
            return device.getCommands() != null ? device.getCommands().get(OFF_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Value getIsOnValue() {
            return device.getValues() != null ? device.getValues().get(IS_ON_VALUE) : null;
        }

        @Override
        public boolean isOn() {
            SimpleProxyObject.Value value = getIsOnValue();
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
            implements com.intuso.housemate.api.object.device.feature.PlaybackControl<SimpleProxyObject.Command> {

        public PlaybackControl(SimpleProxyObject.Device device) {
            super(device);
        }

        @Override
        public SimpleProxyObject.Command getPlayCommand() {
            return device.getCommands() != null ? device.getCommands().get(PLAY_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get(PAUSE_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get(STOP_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get(REWIND_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getRewindCommand() {
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
            implements com.intuso.housemate.api.object.device.feature.StatefulPlaybackControl<SimpleProxyObject.Command, SimpleProxyObject.Value> {

        public StatefulPlaybackControl(SimpleProxyObject.Device device) {
            super(device);
        }

        @Override
        public SimpleProxyObject.Value getIsPlayingValue() {
            return device.getValues() != null ? device.getValues().get(IS_PLAYING_VALUE) : null;
        }

        @Override
        public boolean isPlaying() {
            SimpleProxyObject.Value value = getIsPlayingValue();
            return value != null
                    && value.getTypeInstances() != null
                    && value.getTypeInstances().getFirstValue() != null
                    && Boolean.parseBoolean(value.getTypeInstances().getFirstValue());
        }

        @Override
        public SimpleProxyObject.Command getPlayCommand() {
            return device.getCommands() != null ? device.getCommands().get(PLAY_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getPauseCommand() {
            return device.getCommands() != null ? device.getCommands().get(PAUSE_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getStopCommand() {
            return device.getCommands() != null ? device.getCommands().get(STOP_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getForwardCommand() {
            return device.getCommands() != null ? device.getCommands().get(REWIND_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getRewindCommand() {
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
            implements com.intuso.housemate.api.object.device.feature.VolumeControl<SimpleProxyObject.Command> {

        public VolumeControl(SimpleProxyObject.Device device) {
            super(device);
        }

        @Override
        public SimpleProxyObject.Command getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get(VOLUME_UP_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getVolumeDownCommand() {
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
            implements com.intuso.housemate.api.object.device.feature.StatefulVolumeControl<SimpleProxyObject.Command, SimpleProxyObject.Value> {

        public StatefulVolumeControl(SimpleProxyObject.Device device) {
            super(device);
        }

        @Override
        public SimpleProxyObject.Value getCurrentVolumeValue() {
            return device.getValues() != null ? device.getValues().get(CURRENT_VOLUME_VALUE) : null;
        }

        @Override
        public SimpleProxyObject.Command getVolumeUpCommand() {
            return device.getCommands() != null ? device.getCommands().get(VOLUME_UP_COMMAND) : null;
        }

        @Override
        public SimpleProxyObject.Command getVolumeDownCommand() {
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
