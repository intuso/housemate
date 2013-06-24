package com.intuso.housemate.broker.factory;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.RealSingleChoiceType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.housemate.plugin.api.RealDeviceFactory;

import java.util.Arrays;
import java.util.HashMap;
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

    private final BrokerGeneralResources resources;
    private final Map<String, RealDeviceFactory<?>> factories;
    private final DeviceFactoryType type;

    public DeviceFactory(BrokerGeneralResources resources) {
        this.resources = resources;
        factories = new HashMap<String, RealDeviceFactory<?>>();
        type = new DeviceFactoryType(resources.getClientResources());
        resources.addPluginListener(this, true);
    }

    public DeviceFactoryType getType() {
        return type;
    }

    public RealCommand createAddDeviceCommand(String commandId, String commandName, String commandDescription, final RealList<DeviceWrappable, RealDevice> list) {
        return new RealCommand(resources.getClientResources(), commandId, commandName, commandDescription, Arrays.asList(
                new RealParameter<String>(resources.getClientResources(), NAME_PARAMETER_ID, NAME_PARAMETER_NAME, NAME_PARAMETER_DESCRIPTION, new StringType(resources.getClientResources())),
                new RealParameter<String>(resources.getClientResources(), DESCRIPTION_PARAMETER_ID, DESCRIPTION_PARAMETER_NAME, DESCRIPTION_PARAMETER_DESCRIPTION, new StringType(resources.getClientResources())),
                new RealParameter<RealDeviceFactory<?>>(resources.getClientResources(), TYPE_PARAMETER_ID, TYPE_PARAMETER_NAME, TYPE_PARAMETER_DESCRIPTION, type)
        )) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                TypeInstance deviceType = values.get(TYPE_PARAMETER_ID);
                if(deviceType == null || deviceType.getValue() == null)
                    throw new HousemateException("No device type specified");
                TypeInstance name = values.get(NAME_PARAMETER_ID);
                if(name == null || name.getValue() == null)
                    throw new HousemateException("No device name specified");
                TypeInstance description = values.get(DESCRIPTION_PARAMETER_ID);
                if(description == null || description.getValue() == null)
                    throw new HousemateException("No device description specified");
                RealDeviceFactory<?> deviceFactory = type.deserialise(deviceType);
                if(deviceFactory == null)
                    throw new HousemateException("No factory known for device type " + deviceType);
                RealDevice device = deviceFactory.create(resources.getClientResources(), name.getValue(), name.getValue(), description.getValue());
                resources.getAnnotationProcessor().process(device);
                list.add(device);
                resources.getStorage().saveValues(list.getPath(), device.getId(), values);
            }
        };
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(RealDeviceFactory<?> factory : plugin.getDeviceFactories()) {
            resources.getLog().d("Adding new device factory for type " + factory.getTypeId());
            factories.put(factory.getTypeId(), factory);
            type.getOptions().add(new RealOption(resources.getClientResources(), factory.getTypeId(), factory.getTypeName(), factory.getTypeDescription()));
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        for(RealDeviceFactory<?> factory : plugin.getDeviceFactories()) {
            factories.remove(factory.getTypeId());
            type.getOptions().remove(factory.getTypeId());
        }
    }

    private class DeviceFactoryType extends RealSingleChoiceType<RealDeviceFactory<?>> {
        protected DeviceFactoryType(RealResources resources) {
            super(resources, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Arrays.<RealOption>asList());
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
