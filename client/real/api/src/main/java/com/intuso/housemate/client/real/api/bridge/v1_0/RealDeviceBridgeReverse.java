package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.object.RealDevice;
import com.intuso.housemate.client.real.api.internal.object.RealFeature;
import com.intuso.housemate.client.real.api.internal.object.RealList;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealDeviceBridgeReverse
        implements com.intuso.housemate.client.v1_0.real.api.object.RealDevice<
        com.intuso.housemate.client.v1_0.real.api.object.RealCommand<?, ?, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealValue<Boolean, ?, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealValue<String, ?, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
                RealDeviceBridgeReverse> {

    private final RealDevice<?, ?, ?, ?, ?> device;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;
    private final FeatureMapper featureMapper;

    @Inject
    public RealDeviceBridgeReverse(@Assisted RealDevice<?, ?, ?, ?, ?> device,
                            ListMapper listMapper,
                            CommandMapper commandMapper,
                            ValueMapper valueMapper,
                            FeatureMapper featureMapper) {
        this.device = device;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
        this.featureMapper = featureMapper;
    }

    public RealDevice<?, ?, ?, ?, ?> getDevice() {
        return device;
    }

    @Override
    public String getId() {
        return device.getId();
    }

    @Override
    public String getName() {
        return device.getName();
    }

    @Override
    public String getDescription() {
        return device.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super RealDeviceBridgeReverse> listener) {
        return null; //todo
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealValue<String, ?, ?> getErrorValue() {
        return valueMapper.map(device.getErrorValue());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealCommand getRemoveCommand() {
        return commandMapper.map(device.getRemoveCommand());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealCommand getRenameCommand() {
        return commandMapper.map(device.getRenameCommand());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealCommand getStartCommand() {
        return commandMapper.map(device.getStopCommand());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealCommand getStopCommand() {
        return commandMapper.map(device.getStopCommand());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealValue<Boolean, ?, ?> getRunningValue() {
        return valueMapper.map(device.getRunningValue());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?> getFeatures() {
        return listMapper.map((RealList<RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?>) device.getFeatures(),
                featureMapper.getToV1_0Function(),
                featureMapper.getFromV1_0Function());
    }

    public static class Container implements com.intuso.housemate.client.v1_0.real.api.object.RealDevice.Container<com.intuso.housemate.client.v1_0.real.api.object.RealDevice<?, ?, ?, ?, ?>, com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealDevice<?, ?, ?, ?, ?>, ?>> {

        private final RealDevice.Container<RealDevice<?, ?, ?, ?, ?>, RealList<? extends RealDevice<?, ?, ?, ?, ?>, ?>> container;
        private final DeviceMapper deviceMapper;
        private final ListMapper listMapper;

        @Inject
        public Container(RealDevice.Container<RealDevice<?, ?, ?, ?, ?>, RealList<? extends RealDevice<?, ?, ?, ?, ?>, ?>> container, DeviceMapper deviceMapper, ListMapper listMapper) {
            this.container = container;
            this.deviceMapper = deviceMapper;
            this.listMapper = listMapper;
        }

        @Override
        public void addDevice(com.intuso.housemate.client.v1_0.real.api.object.RealDevice<?, ?, ?, ?, ?> device) {
            container.addDevice(deviceMapper.map(device));
        }

        @Override
        public void removeDevice(com.intuso.housemate.client.v1_0.real.api.object.RealDevice<?, ?, ?, ?, ?> device) {
            container.removeDevice(deviceMapper.map(device));
        }

        @Override
        public com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealDevice<?, ?, ?, ?, ?>, ?> getDevices() {
            return listMapper.map((RealList<RealDevice<?, ?, ?, ?, ?>, ?>) container.getDevices(),
                    deviceMapper.getToV1_0Function(),
                    deviceMapper.getFromV1_0Function());
        }
    }

    public interface Factory {
        RealDeviceBridgeReverse create(RealDevice<?, ?, ?, ?, ?> device);
    }
}
