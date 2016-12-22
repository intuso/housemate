package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.RealFeature;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealDeviceBridge
        implements com.intuso.housemate.client.real.api.internal.RealDevice<
                RealCommand<?, ?, ?>,
        RealValue<Boolean, ?, ?>,
        RealValue<String, ?, ?>,
        com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>,
        com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
                        RealDeviceBridge> {

    private final RealDevice<?, ?, ?, RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?, ?> device;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;
    private final FeatureMapper featureMapper;

    @Inject
    public RealDeviceBridge(@Assisted RealDevice<?, ?, ?, ?, ?, ?> device,
                            ListMapper listMapper,
                            CommandMapper commandMapper,
                            ValueMapper valueMapper,
                            FeatureMapper featureMapper) {
        this.device = (RealDevice<?, ?, ?, RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?, ?>) device;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
        this.featureMapper = featureMapper;
    }

    public RealDevice<?, ?, ?, ?, ?, ?> getDevice() {
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
    public ListenerRegistration addObjectListener(Listener<? super RealDeviceBridge> listener) {
        return null; //todo
    }

    @Override
    public RealValue<String, ?, ?> getErrorValue() {
        return valueMapper.map(device.getErrorValue());
    }

    @Override
    public RealCommand getRemoveCommand() {
        return commandMapper.map(device.getRemoveCommand());
    }

    @Override
    public RealCommand getRenameCommand() {
        return commandMapper.map(device.getRenameCommand());
    }

    @Override
    public RealCommand getStartCommand() {
        return commandMapper.map(device.getStopCommand());
    }

    @Override
    public RealCommand getStopCommand() {
        return commandMapper.map(device.getStopCommand());
    }

    @Override
    public RealValue<Boolean, ?, ?> getRunningValue() {
        return valueMapper.map(device.getRunningValue());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?> getFeatures() {
        return listMapper.map((RealList<RealFeature<?, ?, ?, ?, ?, ?, ?, ?>, ?>) device.getFeatures(),
                featureMapper.getFromV1_0Function(),
                featureMapper.getToV1_0Function());
    }

    @Override
    public RealCommand<?, ?, ?> getAddFeatureCommand() {
        return commandMapper.map(device.getAddFeatureCommand());
    }

    @Override
    public void addFeature(com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature) {
        device.addFeature(featureMapper.map(feature));
    }

    @Override
    public void removeFeature(com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature) {
        device.removeFeature(featureMapper.map(feature));
    }

    public static class Container implements com.intuso.housemate.client.real.api.internal.RealDevice.Container<com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>, ?>> {

        private final RealDevice.Container container;
        private final DeviceMapper deviceMapper;
        private final ListMapper listMapper;

        @Inject
        public Container(RealDevice.Container<RealDevice<?, ?, ?, ?, ?, ?>, RealList<? extends RealDevice<?, ?, ?, ?, ?, ?>, ?>> container, DeviceMapper deviceMapper, ListMapper listMapper) {
            this.container = container;
            this.deviceMapper = deviceMapper;
            this.listMapper = listMapper;
        }

        @Override
        public void addDevice(com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?> device) {
            container.addDevice(deviceMapper.map(device));
        }

        @Override
        public void removeDevice(com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?> device) {
            container.removeDevice(deviceMapper.map(device));
        }

        @Override
        public com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?>, ?> getDevices() {
            return listMapper.map((RealList<RealDevice<?, ?, ?, ?, ?, ?>, ?>) container.getDevices(),
                    deviceMapper.getFromV1_0Function(),
                    deviceMapper.getToV1_0Function());
        }
    }

    public interface Factory {
        RealDeviceBridge create(RealDevice<?, ?, ?, ?, ?, ?> device);
    }
}
