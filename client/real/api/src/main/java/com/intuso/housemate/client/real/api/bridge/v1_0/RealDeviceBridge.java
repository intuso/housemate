package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.comms.api.bridge.v1_0.DataMapper;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created by tomc on 03/11/15.
 */
public class RealDeviceBridge<FROM extends com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver, TO extends DeviceDriver> implements RealDevice<TO> {

    private final com.intuso.housemate.client.v1_0.real.api.RealDevice<FROM> device;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;
    private final PropertyMapper propertyMapper;
    private final PluginResourceMapper pluginResourceMapper;
    private final DeviceDriverMapper deviceDriverMapper;
    private final DeviceDriverFactoryMapper deviceDriverFactoryMapper;

    @Inject
    public RealDeviceBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.RealDevice<?> device,
                            ListMapper listMapper,
                            CommandMapper commandMapper,
                            ValueMapper valueMapper,
                            PropertyMapper propertyMapper,
                            PluginResourceMapper pluginResourceMapper,
                            DeviceDriverMapper deviceDriverMapper,
                            DeviceDriverFactoryMapper deviceDriverFactoryMapper) {
        this.device = (com.intuso.housemate.client.v1_0.real.api.RealDevice<FROM>) device;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
        this.propertyMapper = propertyMapper;
        this.pluginResourceMapper = pluginResourceMapper;
        this.deviceDriverMapper = deviceDriverMapper;
        this.deviceDriverFactoryMapper = deviceDriverFactoryMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.RealDevice<FROM> getDevice() {
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
    public List<String> getFeatureIds() {
        return device.getFeatureIds();
    }

    @Override
    public List<String> getCustomCommandIds() {
        return device.getCustomCommandIds();
    }

    @Override
    public List<String> getCustomValueIds() {
        return device.getCustomValueIds();
    }

    @Override
    public List<String> getCustomPropertyIds() {
        return device.getCustomPropertyIds();
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
    public RealList<RealCommand> getCommands() {
        return listMapper.map(device.getCommands(),
                commandMapper.getFromV1_0Function(),
                commandMapper.getToV1_0Function());
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
        Function<com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Factory<FROM>, DeviceDriver.Factory<TO>> ddfConvertFrom
                = deviceDriverFactoryMapper.getFromV1_0Function();
        Function<com.intuso.housemate.client.v1_0.real.api.driver.PluginResource<com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Factory<FROM>>, PluginResource<DeviceDriver.Factory<TO>>> convertFrom
                = pluginResourceMapper.getFromV1_0Function(ddfConvertFrom);
        Function<DeviceDriver.Factory<TO>, com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Factory<FROM>> ddfConvertTo
                = deviceDriverFactoryMapper.getToV1_0Function();
        Function<PluginResource<DeviceDriver.Factory<TO>>, com.intuso.housemate.client.v1_0.real.api.driver.PluginResource<com.intuso.housemate.client.v1_0.real.api.driver.DeviceDriver.Factory<FROM>>> convertTo
                = pluginResourceMapper.getToV1_0Function(ddfConvertTo);
        return propertyMapper.map(device.getDriverProperty(),
                convertFrom,
                convertTo);
    }

    @Override
    public RealValue<Boolean> getDriverLoadedValue() {
        return valueMapper.map(device.getDriverLoadedValue());
    }

    @Override
    public RealList<RealValue<?>> getValues() {
        return listMapper.map(device.getValues(),
                valueMapper.getFromV1_0Function(),
                valueMapper.getToV1_0Function());
    }

    @Override
    public RealList<RealProperty<?>> getProperties() {
        return listMapper.map(device.getProperties(),
                propertyMapper.getFromV1_0Function(),
                propertyMapper.getToV1_0Function());
    }

    public static class Container implements RealDevice.Container {

        private final com.intuso.housemate.client.v1_0.real.api.RealDevice.Container container;
        private final DeviceMapper deviceMapper;
        private final DataMapper dataMapper;
        private final ListMapper listMapper;

        @Inject
        public Container(com.intuso.housemate.client.v1_0.real.api.RealDevice.Container container, DeviceMapper deviceMapper, DataMapper dataMapper, ListMapper listMapper) {
            this.container = container;
            this.deviceMapper = deviceMapper;
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
            return listMapper.map(container.getDevices(), deviceMapper.getFromV1_0Function(), deviceMapper.getToV1_0Function());
        }
    }

    public interface Factory {
        RealDeviceBridge<?, ?> create(com.intuso.housemate.client.v1_0.real.api.RealDevice<?> device);
    }
}
