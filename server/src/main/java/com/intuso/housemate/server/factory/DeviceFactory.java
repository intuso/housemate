package com.intuso.housemate.server.factory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.annotations.processor.AnnotationProcessor;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.intuso.housemate.server.plugin.PluginListener;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Map;

/**
 */
public final class DeviceFactory implements PluginListener {

    public final static String TYPE_ID = "device-factory";
    public final static String TYPE_NAME = "Device Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new devices";

    public final static String NAME_PARAMETER_ID = "name";
    public final static String NAME_PARAMETER_NAME = "Name";
    public final static String NAME_PARAMETER_DESCRIPTION = "The name of the new device";
    public final static String DESCRIPTION_PARAMETER_ID = "description";
    public final static String DESCRIPTION_PARAMETER_NAME = "Description";
    public final static String DESCRIPTION_PARAMETER_DESCRIPTION = "Description for the new device";
    public final static String TYPE_PARAMETER_ID = "type";
    public final static String TYPE_PARAMETER_NAME = "Type";
    public final static String TYPE_PARAMETER_DESCRIPTION = "The type of the new device";

    private final Log log;
    private final Storage storage;
    private final AnnotationProcessor annotationProcessor;

    private final Map<String, RealDeviceFactory<?>> factories = Maps.newHashMap();
    private final DeviceFactoryType type;

    @Inject
    public DeviceFactory(Log log, Storage storage,
                         AnnotationProcessor annotationProcessor, PluginManager pluginManager) {
        this.log = log;
        this.storage = storage;
        this.annotationProcessor = annotationProcessor;
        type = new DeviceFactoryType(log);
        pluginManager.addPluginListener(this, true);
    }

    public DeviceFactoryType getType() {
        return type;
    }

    public RealCommand createAddDeviceCommand(String commandId, String commandName, String commandDescription,
                                              final RealList<TypeData<?>, RealType<?, ?, ?>> types,
                                              final RealList<DeviceData, RealDevice> devices) {
        return new RealCommand(log, commandId, commandName, commandDescription, Arrays.asList(
                new RealParameter<String>(log, NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(log)),
                new RealParameter<String>(log, DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(log)),
                new RealParameter<RealDeviceFactory<?>>(log, TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                TypeInstances deviceType = values.get(TYPE_PARAMETER_ID);
                if(deviceType == null || deviceType.getFirstValue() == null)
                    throw new HousemateException("No device type specified");
                TypeInstances name = values.get(NAME_PARAMETER_ID);
                if(name == null || name.getFirstValue() == null)
                    throw new HousemateException("No device name specified");
                TypeInstances description = values.get(DESCRIPTION_PARAMETER_ID);
                if(description == null || description.getFirstValue() == null)
                    throw new HousemateException("No device description specified");
                RealDeviceFactory<?> deviceFactory = type.deserialise(deviceType.get(0));
                if(deviceFactory == null)
                    throw new HousemateException("No factory known for device type " + deviceType);
                RealDevice device = deviceFactory.create(log, name.getFirstValue(), name.getFirstValue(), description.getFirstValue());
                annotationProcessor.process(types, device);
                devices.add(device);
                storage.saveValues(devices.getPath(), device.getId(), values);
            }
        };
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(RealDeviceFactory<?> factory : plugin.getDeviceFactories()) {
            log.d("Adding new device factory for type " + factory.getTypeId());
            factories.put(factory.getTypeId(), factory);
            type.getOptions().add(new RealOption(log, factory.getTypeId(), factory.getTypeName(), factory.getTypeDescription()));
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        for(RealDeviceFactory<?> factory : plugin.getDeviceFactories()) {
            factories.remove(factory.getTypeId());
            type.getOptions().remove(factory.getTypeId());
        }
    }

    private class DeviceFactoryType extends RealChoiceType<RealDeviceFactory<?>> {
        protected DeviceFactoryType(Log log) {
            super(log, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, 1, 1, Arrays.<RealOption>asList());
        }

        @Override
        public TypeInstance serialise(RealDeviceFactory<?> o) {
            return new TypeInstance(o.getTypeId());
        }

        @Override
        public RealDeviceFactory<?> deserialise(TypeInstance value) {
            return value != null && value.getValue() != null ? factories.get(value.getValue()) : null;
        }
    }
}
