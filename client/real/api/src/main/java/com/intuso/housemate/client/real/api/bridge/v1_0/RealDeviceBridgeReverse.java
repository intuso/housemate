package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.*;
import com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver;
import com.intuso.housemate.client.v1_0.real.api.driver.PluginResource;
import com.intuso.housemate.comms.api.bridge.v1_0.DataMapper;
import com.intuso.housemate.comms.v1_0.api.payload.DeviceData;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealDeviceBridgeReverse<FROM extends com.intuso.housemate.client.real.api.internal.driver.DeviceDriver, TO extends DeviceDriver> implements RealDevice<TO> {

    private final com.intuso.housemate.client.real.api.internal.RealDevice<FROM> device;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;
    private final PropertyMapper propertyMapper;
    private final FeatureMapper featureMapper;
    private final PluginResourceMapper pluginResourceMapper;
    private final DeviceDriverMapper deviceDriverMapper;
    private final DeviceDriverFactoryMapper deviceDriverFactoryMapper;

    @Inject
    public RealDeviceBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.RealDevice<?> device,
                                   ListMapper listMapper,
                                   CommandMapper commandMapper,
                                   ValueMapper valueMapper,
                                   PropertyMapper propertyMapper,
                                   FeatureMapper featureMapper, PluginResourceMapper pluginResourceMapper,
                                   DeviceDriverMapper deviceDriverMapper,
                                   DeviceDriverFactoryMapper deviceDriverFactoryMapper) {
        this.featureMapper = featureMapper;
        this.device = (com.intuso.housemate.client.real.api.internal.RealDevice<FROM>) device;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
        this.propertyMapper = propertyMapper;
        this.pluginResourceMapper = pluginResourceMapper;
        this.deviceDriverMapper = deviceDriverMapper;
        this.deviceDriverFactoryMapper = deviceDriverFactoryMapper;
    }

    public com.intuso.housemate.client.real.api.internal.RealDevice<FROM> getDevice() {
        return device;
    }

    @Override
    public TO getDriver() {
        return deviceDriverMapper.map(device.getDriver());
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
    public String[] getPath() {
        return device.getPath();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super RealDevice<TO>> listener) {
        return null; //todo
    }

    @Override
    public RealValue<String> getErrorValue() {
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
    public RealValue<Boolean> getRunningValue() {
        return valueMapper.map(device.getRunningValue());
    }

    @Override
    public RealProperty<PluginResource<DeviceDriver.Factory<TO>>> getDriverProperty() {
        Function<com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<FROM>, DeviceDriver.Factory<TO>> ddfConvertFrom
                = deviceDriverFactoryMapper.getToV1_0Function();
        Function<com.intuso.housemate.client.real.api.internal.driver.PluginResource<com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<FROM>>, PluginResource<DeviceDriver.Factory<TO>>> convertFrom
                = pluginResourceMapper.getToV1_0Function(ddfConvertFrom);
        Function<DeviceDriver.Factory<TO>, com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<FROM>> ddfConvertTo
                = deviceDriverFactoryMapper.getFromV1_0Function();
        Function<PluginResource<DeviceDriver.Factory<TO>>, com.intuso.housemate.client.real.api.internal.driver.PluginResource<com.intuso.housemate.client.real.api.internal.driver.DeviceDriver.Factory<FROM>>> convertTo
                = pluginResourceMapper.getFromV1_0Function(ddfConvertTo);
        return propertyMapper.map(device.getDriverProperty(),
                convertFrom,
                convertTo);
    }

    @Override
    public RealValue<Boolean> getDriverLoadedValue() {
        return valueMapper.map(device.getDriverLoadedValue());
    }

    @Override
    public RealList<RealProperty<?>> getProperties() {
        return listMapper.map(device.getProperties(),
                propertyMapper.getToV1_0Function(),
                propertyMapper.getFromV1_0Function());
    }

    @Override
    public RealList<RealFeature> getFeatures() {
        return listMapper.map(device.getFeatures(),
                featureMapper.getToV1_0Function(),
                featureMapper.getFromV1_0Function());
    }

    public static class Container implements RealDevice.Container {

        private final com.intuso.housemate.client.real.api.internal.RealDevice.Container container;
        private final DeviceMapper deviceMapper;
        private final DataMapper dataMapper;
        private final ListMapper listMapper;

        @Inject
        public Container(com.intuso.housemate.client.real.api.internal.RealDevice.Container container, DeviceMapper deviceMapper, DataMapper dataMapper, ListMapper listMapper) {
            this.deviceMapper = deviceMapper;
            this.container = container;
            this.dataMapper = dataMapper;
            this.listMapper = listMapper;
        }

        @Override
        public <DRIVER extends DeviceDriver> RealDevice<DRIVER> createAndAddDevice(DeviceData data) {
            return (RealDevice<DRIVER>) deviceMapper.map(container.createAndAddDevice(dataMapper.map(data)));
        }

        @Override
        public void addDevice(RealDevice<?> device) {
            container.addDevice(deviceMapper.map(device));
        }

        @Override
        public void removeDevice(RealDevice<?> device) {
            container.removeDevice(deviceMapper.map(device));
        }

        @Override
        public RealList<RealDevice<?>> getDevices() {
            return listMapper.map(container.getDevices(), deviceMapper.getToV1_0Function(), deviceMapper.getFromV1_0Function());
        }
    }

    public interface Factory {
        RealDeviceBridgeReverse<?, ?> create(com.intuso.housemate.client.real.api.internal.RealDevice<?> device);
    }
}
