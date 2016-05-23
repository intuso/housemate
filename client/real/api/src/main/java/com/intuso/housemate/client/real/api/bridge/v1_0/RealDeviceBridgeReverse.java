package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.*;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.DeviceDriverFactoryMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.DeviceDriverMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.PluginResourceMapper;
import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;
import com.intuso.housemate.plugin.v1_0.api.driver.PluginResource;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealDeviceBridgeReverse
        implements RealDevice<RealCommand<?, ?, ?>,
        RealValue<Boolean, ?, ?>,
        RealValue<String, ?, ?>,
        RealProperty<PluginResource<DeviceDriver.Factory<?>>, ?, ?, ?>,
        RealList<? extends RealProperty<?, ?, ?, ?>, ?>,
        RealList<? extends RealFeature<?, ?, ?>, ?>,
        RealDeviceBridgeReverse> {

    private final com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?> device;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;
    private final PropertyMapper propertyMapper;
    private final FeatureMapper featureMapper;
    private final PluginResourceMapper pluginResourceMapper;
    private final DeviceDriverMapper deviceDriverMapper;
    private final DeviceDriverFactoryMapper deviceDriverFactoryMapper;

    @Inject
    public RealDeviceBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?> device,
                                   ListMapper listMapper,
                                   CommandMapper commandMapper,
                                   ValueMapper valueMapper,
                                   PropertyMapper propertyMapper,
                                   FeatureMapper featureMapper, PluginResourceMapper pluginResourceMapper,
                                   DeviceDriverMapper deviceDriverMapper,
                                   DeviceDriverFactoryMapper deviceDriverFactoryMapper) {
        this.featureMapper = featureMapper;
        this.device = (com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?>) device;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
        this.propertyMapper = propertyMapper;
        this.pluginResourceMapper = pluginResourceMapper;
        this.deviceDriverMapper = deviceDriverMapper;
        this.deviceDriverFactoryMapper = deviceDriverFactoryMapper;
    }

    public com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?> getDevice() {
        return device;
    }

    @Override
    public <TO extends DeviceDriver> TO getDriver() {
        return (TO) deviceDriverMapper.map(device.getDriver());
    }

    @Override
    public boolean isDriverLoaded() {
        return device.isDriverLoaded();
    }

    @Override
    public void setError(String error) {
        device.setError(error);
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
    public RealProperty<PluginResource<DeviceDriver.Factory<?>>, ?, ?, ?> getDriverProperty() {
        Function<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>, DeviceDriver.Factory<?>> ddfConvertFrom
                = deviceDriverFactoryMapper.getToV1_0Function();
        Function<com.intuso.housemate.plugin.api.internal.driver.PluginResource<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>>, PluginResource<DeviceDriver.Factory<?>>> convertFrom
                = pluginResourceMapper.getToV1_0Function(ddfConvertFrom);
        Function<DeviceDriver.Factory<?>, com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>> ddfConvertTo
                = deviceDriverFactoryMapper.getFromV1_0Function();
        Function<PluginResource<DeviceDriver.Factory<?>>, com.intuso.housemate.plugin.api.internal.driver.PluginResource<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>>> convertTo
                = pluginResourceMapper.getFromV1_0Function(ddfConvertTo);
        return propertyMapper.map(device.getDriverProperty(),
                convertFrom,
                convertTo);
    }

    @Override
    public RealValue<Boolean, ?, ?> getDriverLoadedValue() {
        return valueMapper.map(device.getDriverLoadedValue());
    }

    @Override
    public RealList<RealProperty<?, ?, ?, ?>, ?> getProperties() {
        return listMapper.map((com.intuso.housemate.client.real.api.internal.RealList<com.intuso.housemate.client.real.api.internal.RealProperty<?, ?, ?, ?>, ?>) device.getProperties(),
                propertyMapper.getToV1_0Function(),
                propertyMapper.getFromV1_0Function());
    }

    @Override
    public RealList<RealFeature<?, ?, ?>, ?> getFeatures() {
        return listMapper.map((com.intuso.housemate.client.real.api.internal.RealList<com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?>, ?>) device.getFeatures(),
                featureMapper.getToV1_0Function(),
                featureMapper.getFromV1_0Function());
    }

    public static class Container implements RealDevice.Container<RealDevice<?, ?, ?, ?, ?, ?, ?>, RealList<RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>> {

        private final com.intuso.housemate.client.real.api.internal.RealDevice.Container container;
        private final DeviceMapper deviceMapper;
        private final ListMapper listMapper;

        @Inject
        public Container(com.intuso.housemate.client.real.api.internal.RealDevice.Container container, DeviceMapper deviceMapper, ListMapper listMapper) {
            this.deviceMapper = deviceMapper;
            this.container = container;
            this.listMapper = listMapper;
        }

        @Override
        public void addDevice(RealDevice<?, ?, ?, ?, ?, ?, ?> device) {
            container.addDevice(deviceMapper.map(device));
        }

        @Override
        public void removeDevice(RealDevice<?, ?, ?, ?, ?, ?, ?> device) {
            container.removeDevice(deviceMapper.map(device));
        }

        @Override
        public RealList<RealDevice<?, ?, ?, ?, ?, ?, ?>, ?> getDevices() {
            return listMapper.map((com.intuso.housemate.client.real.api.internal.RealList<com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>) container.getDevices(),
                    deviceMapper.getToV1_0Function(),
                    deviceMapper.getFromV1_0Function());
        }
    }

    public interface Factory {
        RealDeviceBridgeReverse create(com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?> device);
    }
}
