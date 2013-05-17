package com.intuso.housemate.broker.factory;

import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.real.BrokerRealArgument;
import com.intuso.housemate.broker.object.real.BrokerRealCommand;
import com.intuso.housemate.broker.plugin.PluginDescriptor;
import com.intuso.housemate.broker.plugin.RealDeviceFactory;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.type.TypeSerialiser;
import com.intuso.housemate.core.object.device.DeviceWrappable;
import com.intuso.housemate.real.RealDevice;
import com.intuso.housemate.real.RealList;
import com.intuso.housemate.real.RealOption;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.impl.type.RealSingleChoiceType;
import com.intuso.housemate.real.impl.type.StringType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 13/12/12
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */
public final class DeviceFactory implements PluginListener {

    public final static String TYPE_ID = "device-factory";
    public final static String TYPE_NAME = "Device Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new devices";

    public final static String NAME_ARGUMENT_ID = "name";
    public final static String NAME_ARGUMENT_NAME = "Name";
    public final static String NAME_ARGUMENT_DESCRIPTION = "The name of the new device";
    public final static String DESCRIPTION_ARGUMENT_ID = "description";
    public final static String DESCRIPTION_ARGUMENT_NAME = "Description";
    public final static String DESCRIPTION_ARGUMENT_DESCRIPTION = "Description for the new device";
    public final static String TYPE_ARGUMENT_ID = "type";
    public final static String TYPE_ARGUMENT_NAME = "Type";
    public final static String TYPE_ARGUMENT_DESCRIPTION = "The type of the new device";

    private final BrokerGeneralResources resources;
    private final Map<String, RealDeviceFactory<?>> factories;
    private final DeviceFactoryType type;
    private final DeviceFactorySerialiser serialiser;

    public DeviceFactory(BrokerGeneralResources resources) {
        this.resources = resources;
        factories = new HashMap<String, RealDeviceFactory<?>>();
        serialiser = new DeviceFactorySerialiser();
        type = new DeviceFactoryType(resources.getClientResources(), serialiser);
        resources.addPluginListener(this, true);
    }

    public DeviceFactoryType getType() {
        return type;
    }

    public BrokerRealCommand createAddDeviceCommand(String commandId, String commandName, String commandDescription, final RealList<DeviceWrappable, RealDevice> list) {
        return new BrokerRealCommand(resources.getRealResources(), commandId, commandName, commandDescription, Arrays.asList(
                new BrokerRealArgument<String>(resources.getRealResources(), NAME_ARGUMENT_ID, NAME_ARGUMENT_NAME, NAME_ARGUMENT_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealArgument<String>(resources.getRealResources(), DESCRIPTION_ARGUMENT_ID, DESCRIPTION_ARGUMENT_NAME, DESCRIPTION_ARGUMENT_DESCRIPTION, new StringType(resources.getClientResources())),
                new BrokerRealArgument<RealDeviceFactory<?>>(resources.getRealResources(), TYPE_ARGUMENT_ID, TYPE_ARGUMENT_NAME, TYPE_ARGUMENT_DESCRIPTION, type)
        )) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                String type = values.get(TYPE_ARGUMENT_ID);
                if(type == null)
                    throw new HousemateException("No device type specified");
                String name = values.get(NAME_ARGUMENT_ID);
                if(name == null)
                    throw new HousemateException("No device name specified");
                String description = values.get(DESCRIPTION_ARGUMENT_ID);
                if(description == null)
                    throw new HousemateException("No device description specified");
                RealDeviceFactory<?> deviceFactory = serialiser.deserialise(type);
                if(deviceFactory == null)
                    throw new HousemateException("No factory known for device type " + type);
                RealDevice device = deviceFactory.create(resources.getClientResources(), name, name, description);
                list.add(device);
                resources.getStorage().saveDetails(list.getPath(), device.getId(), values);
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
        protected DeviceFactoryType(RealResources resources, DeviceFactorySerialiser serialiser) {
            super(resources, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION, Arrays.<RealOption>asList(), serialiser);
        }
    }

    private class DeviceFactorySerialiser implements TypeSerialiser<RealDeviceFactory<?>> {
        @Override
        public String serialise(RealDeviceFactory<?> o) {
            return o.getTypeId();
        }

        @Override
        public RealDeviceFactory<?> deserialise(String value) {
            return factories.get(value);
        }
    }
}
