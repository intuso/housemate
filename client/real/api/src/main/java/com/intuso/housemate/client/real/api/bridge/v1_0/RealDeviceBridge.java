package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.housemate.client.v1_0.real.api.RealFeature;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.housemate.client.v1_0.real.api.RealProperty;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.DeviceDriverFactoryMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.DeviceDriverMapper;
import com.intuso.housemate.plugin.api.bridge.v1_0.driver.PluginResourceMapper;
import com.intuso.housemate.plugin.v1_0.api.driver.DeviceDriver;
import com.intuso.housemate.plugin.v1_0.api.driver.PluginDependency;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealDeviceBridge
        implements com.intuso.housemate.client.real.api.internal.RealDevice<com.intuso.housemate.client.real.api.internal.RealCommand<?, ?, ?>,
        com.intuso.housemate.client.real.api.internal.RealValue<Boolean, ?, ?>,
        com.intuso.housemate.client.real.api.internal.RealValue<String, ?, ?>,
        com.intuso.housemate.client.real.api.internal.RealProperty<com.intuso.housemate.plugin.api.internal.driver.PluginDependency<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>>, ?, ?, ?>,
        com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealProperty<?, ?, ?, ?>, ?>,
        com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?>, ?>,
        RealDeviceBridge> {

    private final RealDevice<?, ?, ?, ?, ?, ?, ?> device;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;
    private final PropertyMapper propertyMapper;
    private final FeatureMapper featureMapper;
    private final PluginResourceMapper pluginResourceMapper;
    private final DeviceDriverMapper deviceDriverMapper;
    private final DeviceDriverFactoryMapper deviceDriverFactoryMapper;

    @Inject
    public RealDeviceBridge(@Assisted RealDevice<?, ?, ?, ?, ?, ?, ?> device,
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

    public RealDevice<?, ?, ?, ?, ?, ?, ?> getDevice() {
        return device;
    }

    @Override
    public <TO extends com.intuso.housemate.plugin.api.internal.driver.DeviceDriver> TO getDriver() {
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
    public com.intuso.housemate.client.real.api.internal.RealValue<String, ?, ?> getErrorValue() {
        return valueMapper.map(device.getErrorValue());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealCommand getRemoveCommand() {
        return commandMapper.map(device.getRemoveCommand());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealCommand getRenameCommand() {
        return commandMapper.map(device.getRenameCommand());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealCommand getStartCommand() {
        return commandMapper.map(device.getStopCommand());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealCommand getStopCommand() {
        return commandMapper.map(device.getStopCommand());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealValue<Boolean, ?, ?> getRunningValue() {
        return valueMapper.map(device.getRunningValue());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealProperty<com.intuso.housemate.plugin.api.internal.driver.PluginDependency<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>>, ?, ?, ?> getDriverProperty() {
        Function<DeviceDriver.Factory<?>, com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>> ddfConvertFrom
                = deviceDriverFactoryMapper.getFromV1_0Function();
        Function<PluginDependency<DeviceDriver.Factory<?>>, com.intuso.housemate.plugin.api.internal.driver.PluginDependency<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>>> convertFrom
                = pluginResourceMapper.getFromV1_0Function(ddfConvertFrom);
        Function<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>, DeviceDriver.Factory<?>> ddfConvertTo
                = deviceDriverFactoryMapper.getToV1_0Function();
        Function<com.intuso.housemate.plugin.api.internal.driver.PluginDependency<com.intuso.housemate.plugin.api.internal.driver.DeviceDriver.Factory<?>>, PluginDependency<DeviceDriver.Factory<?>>> convertTo
                = pluginResourceMapper.getToV1_0Function(ddfConvertTo);
        return propertyMapper.map(device.getDriverProperty(),
                convertFrom,
                convertTo);
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealValue<Boolean, ?, ?> getDriverLoadedValue() {
        return valueMapper.map(device.getDriverLoadedValue());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealProperty<?, ?, ?, ?>, ?> getProperties() {
        return listMapper.map((RealList<RealProperty<?, ?, ?, ?>, ?>) device.getProperties(),
                propertyMapper.getFromV1_0Function(),
                propertyMapper.getToV1_0Function());
    }

    @Override
    public com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealFeature<?, ?, ?>, ?> getFeatures() {
        return listMapper.map((RealList<RealFeature<?, ?, ?>, ?>) device.getFeatures(),
                featureMapper.getFromV1_0Function(),
                featureMapper.getToV1_0Function());
    }

    public static class Container implements com.intuso.housemate.client.real.api.internal.RealDevice.Container<com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?>, com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>> {

        private final RealDevice.Container<RealDevice<?, ?, ?, ?, ?, ?, ?>, RealList<? extends RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>> container;
        private final DeviceMapper deviceMapper;
        private final ListMapper listMapper;

        @Inject
        public Container(RealDevice.Container<RealDevice<?, ?, ?, ?, ?, ?, ?>, RealList<? extends RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>> container, DeviceMapper deviceMapper, ListMapper listMapper) {
            this.container = container;
            this.deviceMapper = deviceMapper;
            this.listMapper = listMapper;
        }

        @Override
        public void addDevice(com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?> device) {
            container.addDevice(deviceMapper.map(device));
        }

        @Override
        public void removeDevice(com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?> device) {
            container.removeDevice(deviceMapper.map(device));
        }

        @Override
        public com.intuso.housemate.client.real.api.internal.RealList<? extends com.intuso.housemate.client.real.api.internal.RealDevice<?, ?, ?, ?, ?, ?, ?>, ?> getDevices() {
            return listMapper.map((RealList<RealDevice<?, ?, ?, ?, ?, ?, ?>, ?>) container.getDevices(),
                    deviceMapper.getFromV1_0Function(),
                    deviceMapper.getToV1_0Function());
        }
    }

    public interface Factory {
        RealDeviceBridge create(RealDevice<?, ?, ?, ?, ?, ?, ?> device);
    }
}
