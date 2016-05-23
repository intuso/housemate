package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.DeviceDriverFactoryMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.DeviceDriverMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.PluginResourceMapper;
import com.intuso.housemate.plugin.api.internal.driver.DeviceDriver;
import com.intuso.housemate.plugin.api.internal.driver.PluginResource;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealDeviceBridge
        implements RealDevice<RealCommand<?, ?, ?>,
        RealValue<Boolean, ?, ?>,
        RealValue<String, ?, ?>,
        RealProperty<PluginResource<DeviceDriver.Factory<?>>, ?, ?, ?>,
        RealList<? extends com.intuso.housemate.client.real.api.internal.RealProperty<?, ?, ?, ?>, ?>,
        RealList<? extends RealFeature<?, ?, ?>, ?>,
        RealDeviceBridge> {

    private final com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?> device;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;
    private final PropertyMapper propertyMapper;
    private final FeatureMapper featureMapper;
    private final PluginResourceMapper pluginResourceMapper;
    private final DeviceDriverMapper deviceDriverMapper;
    private final DeviceDriverFactoryMapper deviceDriverFactoryMapper;

    @Inject
    public RealDeviceBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?> device,
                            ListMapper listMapper,
                            CommandMapper commandMapper,
                            ValueMapper valueMapper,
                            PropertyMapper propertyMapper,
                            FeatureMapper featureMapper,
                            PluginResourceMapper pluginResourceMapper,
                            DeviceDriverMapper deviceDriverMapper,
                            DeviceDriverFactoryMapper deviceDriverFactoryMapper) {
        this.featureMapper = featureMapper;
        this.device = device;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
        this.propertyMapper = propertyMapper;
        this.pluginResourceMapper = pluginResourceMapper;
        this.deviceDriverMapper = deviceDriverMapper;
        this.deviceDriverFactoryMapper = deviceDriverFactoryMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?> getDevice() {
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
    public RealProperty<PluginResource<DeviceDriver.Factory<?>>, ?, ?, ?> getDriverProperty() {
        Function<com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Factory<?>, DeviceDriver.Factory<?>> ddfConvertFrom
                = deviceDriverFactoryMapper.getFromV1_0Function();
        Function<com.intuso.housemate.plugin.v1_0.api.driver.PluginResource<com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Factory<?>>, PluginResource<DeviceDriver.Factory<?>>> convertFrom
                = pluginResourceMapper.getFromV1_0Function(ddfConvertFrom);
        Function<DeviceDriver.Factory<?>, com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Factory<?>> ddfConvertTo
                = deviceDriverFactoryMapper.getToV1_0Function();
        Function<PluginResource<DeviceDriver.Factory<?>>, com.intuso.housemate.plugin.v1_0.api.driver.PluginResource<com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver.Factory<?>>> convertTo
                = pluginResourceMapper.getToV1_0Function(ddfConvertTo);
        return propertyMapper.map(device.getDriverProperty(),
                convertFrom,
                convertTo);
    }

    @Override
    public RealValue<Boolean, ?, ?> getDriverLoadedValue() {
        return valueMapper.map(device.getDriverLoadedValue());
    }

    @Override
    public RealList<? extends RealProperty<?, ?, ?, ?>, ?> getProperties() {
        return listMapper.map((com.intuso.housemate.client.v1_0.real.api.RealList<com.intuso.housemate.client.v1_0.real.api.RealProperty<?, ?, ?, ?>, ?>) device.getProperties(),
                propertyMapper.getFromV1_0Function(),
                propertyMapper.getToV1_0Function());
    }

    @Override
    public RealList<? extends RealFeature<?, ?, ?>, ?> getFeatures() {
        return listMapper.map((com.intuso.housemate.client.v1_0.real.api.RealList<com.intuso.housemate.client.v1_0.real.api.RealFeature<?, ?, ?>, ?>) device.getFeatures(),
                featureMapper.getFromV1_0Function(),
                featureMapper.getToV1_0Function());
    }

    public static class Container implements RealDevice.Container<RealDevice<?, ?, ?, ?, ?, ?, ?>, RealList<? extends RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>> {

        private final com.intuso.housemate.client.v1_0.real.api.RealDevice.Container<com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.v1_0.real.api.RealList<? extends com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>> container;
        private final DeviceMapper deviceMapper;
        private final ListMapper listMapper;

        @Inject
        public Container(com.intuso.housemate.client.v1_0.real.api.RealDevice.Container<com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.v1_0.real.api.RealList<? extends com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>> container, DeviceMapper deviceMapper, ListMapper listMapper) {
            this.container = container;
            this.deviceMapper = deviceMapper;
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
        public RealList<? extends RealDevice<?, ?, ?, ?, ?, ?, ?>, ?> getDevices() {
            return listMapper.map((com.intuso.housemate.client.v1_0.real.api.RealList<com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>) container.getDevices(),
                    deviceMapper.getFromV1_0Function(),
                    deviceMapper.getToV1_0Function());
        }
    }

    public interface Factory {
        RealDeviceBridge create(com.intuso.housemate.client.v1_0.real.api.RealDevice<?, ?, ?, ?, ?, ?, ?> device);
    }
}
