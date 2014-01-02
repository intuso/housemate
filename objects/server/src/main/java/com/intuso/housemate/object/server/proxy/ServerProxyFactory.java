package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterFactory;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.option.OptionFactory;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeFactory;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.api.object.value.ValueFactory;

/**
 * Container class for all factories for server objects that are proxies of those on a client
 */
public class ServerProxyFactory {

    private final static All allFactory = new All();
    private final static Command commandFactory = new Command();
    private final static Device deviceFactory = new Device();
    private final static GenericList listFactory = new GenericList();
    private final static Option optionFactory = new Option();
    private final static Parameter parameterFactory = new Parameter();
    private final static Property propertyFactory = new Property();
    private final static SubType subTypeFactory = new SubType();
    private final static Type typeFactory = new Type();
    private final static Value valueFactory = new Value();

    public static class All implements HousemateObjectFactory<ServerProxyResources<?>, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> {
        @Override
        public ServerProxyObject<?, ?, ?, ?, ?> create(ServerProxyResources<?> resources, HousemateData<?> data) throws HousemateException {
            if(data instanceof ParameterData)
                return parameterFactory.create(resources, (ParameterData) data);
            else if(data instanceof CommandData)
                return commandFactory.create(resources, (CommandData) data);
            else if(data instanceof DeviceData)
                return deviceFactory.create(resources, (DeviceData) data);
            else if(data instanceof ListData)
                return listFactory.create(resources, (ListData<HousemateData<?>>) data);
            else if(data instanceof OptionData)
                return optionFactory.create(resources, (OptionData) data);
            else if(data instanceof PropertyData)
                return propertyFactory.create(resources, (PropertyData) data);
            else if(data instanceof SubTypeData)
                return subTypeFactory.create(resources, (SubTypeData) data);
            else if(data instanceof TypeData)
                return typeFactory.create(resources, (TypeData) data);
            else if(data instanceof ValueData)
                return valueFactory.create(resources, (ValueData) data);
            else
                throw new HousemateException("Don't know how to create an object from " + data.getClass().getName());
        }
    }

    public static class Parameter implements ParameterFactory<ServerProxyResources<?>, ServerProxyParameter> {
        @Override
        public ServerProxyParameter create(ServerProxyResources<?> resources, ParameterData data) throws HousemateException {
            return new ServerProxyParameter(noFactoryType(resources), data);
        }
    }

    public static class Command implements CommandFactory<ServerProxyResources<?>, ServerProxyCommand> {
        @Override
        public ServerProxyCommand create(ServerProxyResources<?> resources, CommandData data) throws HousemateException {
            ServerProxyResources<List<ParameterData, ServerProxyParameter>> r
                    = changeFactoryType(resources,
                    new List<ParameterData, ServerProxyParameter>(changeFactoryType(resources, parameterFactory)));
            return new ServerProxyCommand(r, data);
        }
    }

    public static class Device implements DeviceFactory<ServerProxyResources<?>, ServerProxyDevice> {
        @Override
        public ServerProxyDevice create(ServerProxyResources<?> resources, DeviceData data) throws HousemateException {
            ServerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new ServerProxyDevice(r, data);
        }
    }

    public static class GenericList implements ListFactory<ServerProxyResources<?>, HousemateData<?>,
            ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>> {

        @Override
        public ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> create(ServerProxyResources<?> resources, ListData<HousemateData<?>> data) throws HousemateException {
            ServerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>(r, data);
        }
    }

    public static class List<
            SWBL extends HousemateData<?>,
            SWR extends ServerProxyObject<? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<ServerProxyResources<?>, SWBL, SWR, ServerProxyList<SWBL, SWR>> {

        private final ServerProxyResources<? extends HousemateObjectFactory<ServerProxyResources<?>, SWBL, SWR>> resources;

        public List(ServerProxyResources<? extends HousemateObjectFactory<ServerProxyResources<?>, SWBL, SWR>> resources) {
            this.resources = resources;
        }

        @Override
        public ServerProxyList<SWBL, SWR> create(ServerProxyResources<?> r,
                                                        ListData<SWBL> data) throws HousemateException {
            return new ServerProxyList<SWBL, SWR>(resources, data);
        }
    }

    public static class Option implements OptionFactory<ServerProxyResources<?>, ServerProxyOption> {
        @Override
        public ServerProxyOption create(ServerProxyResources<?> resources, OptionData data) throws HousemateException {
            ServerProxyResources<List<SubTypeData, ServerProxySubType>> r
                    = changeFactoryType(resources,
                    new List<SubTypeData, ServerProxySubType>(changeFactoryType(resources, subTypeFactory)));
            return new ServerProxyOption(r, data);
        }
    }

    public static class Property implements PropertyFactory<ServerProxyResources<?>, ServerProxyProperty> {
        @Override
        public ServerProxyProperty create(ServerProxyResources<?> resources, PropertyData data) throws HousemateException {
            ServerProxyResources<Command> r = changeFactoryType(resources, commandFactory);
            return new ServerProxyProperty(r, data);
        }
    }

    public static class SubType implements SubTypeFactory<ServerProxyResources<?>, ServerProxySubType> {
        @Override
        public ServerProxySubType create(ServerProxyResources<?> resources, SubTypeData data) throws HousemateException {
            return new ServerProxySubType(noFactoryType(resources), data);
        }
    }

    public static class Type implements TypeFactory<ServerProxyResources<?>, ServerProxyType> {
        @Override
        public ServerProxyType create(ServerProxyResources<?> resources,
                                             TypeData<?> data) throws HousemateException {
            ServerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new ServerProxyType(r, data);
        }
    }

    public static class Value implements ValueFactory<ServerProxyResources<?>, ServerProxyValue> {
        @Override
        public ServerProxyValue create(ServerProxyResources<?> resources, ValueData data) throws HousemateException {
            return new ServerProxyValue(noFactoryType(resources), data);
        }
    }

    public static
    <NF extends HousemateObjectFactory<? extends ServerProxyResources<?>, ?, ? extends ServerProxyObject<?, ?, ?, ?, ?>>>
    ServerProxyResources<NF> changeFactoryType(ServerProxyResources<?> resources, NF newFactory) {
        return resources.<NF>cloneForNewFactory(newFactory);
    }

    public static ServerProxyResources<NoChildrenServerProxyObjectFactory> noFactoryType(ServerProxyResources<?> resources) {
        return changeFactoryType(resources, null);
    }
}
