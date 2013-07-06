package com.intuso.housemate.object.broker.proxy;

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
 * Container class for all factories for broker objects that are proxies of those on a client
 */
public class BrokerProxyFactory {

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

    public static class All implements HousemateObjectFactory<BrokerProxyResources<?>, HousemateData<?>, BrokerProxyObject<?, ?, ?, ?, ?>> {
        @Override
        public BrokerProxyObject<?, ?, ?, ?, ?> create(BrokerProxyResources<?> resources, HousemateData<?> data) throws HousemateException {
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

    public static class Parameter implements ParameterFactory<BrokerProxyResources<?>, BrokerProxyParameter> {
        @Override
        public BrokerProxyParameter create(BrokerProxyResources<?> resources, ParameterData data) throws HousemateException {
            return new BrokerProxyParameter(noFactoryType(resources), data);
        }
    }

    public static class Command implements CommandFactory<BrokerProxyResources<?>, BrokerProxyCommand> {
        @Override
        public BrokerProxyCommand create(BrokerProxyResources<?> resources, CommandData data) throws HousemateException {
            BrokerProxyResources<List<ParameterData, BrokerProxyParameter>> r
                    = changeFactoryType(resources,
                    new List<ParameterData, BrokerProxyParameter>(changeFactoryType(resources, parameterFactory)));
            return new BrokerProxyCommand(r, data);
        }
    }

    public static class Device implements DeviceFactory<BrokerProxyResources<?>, BrokerProxyDevice> {
        @Override
        public BrokerProxyDevice create(BrokerProxyResources<?> resources, DeviceData data) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyDevice(r, data);
        }
    }

    public static class GenericList implements ListFactory<BrokerProxyResources<?>, HousemateData<?>,
            BrokerProxyObject<?, ?, ?, ?, ?>,
            BrokerProxyList<HousemateData<?>, BrokerProxyObject<?, ?, ?, ?, ?>>> {

        @Override
        public BrokerProxyList<HousemateData<?>, BrokerProxyObject<?, ?, ?, ?, ?>> create(BrokerProxyResources<?> resources, ListData<HousemateData<?>> data) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyList<HousemateData<?>, BrokerProxyObject<?, ?, ?, ?, ?>>(r, data);
        }
    }

    public static class List<
            SWBL extends HousemateData<?>,
            SWR extends BrokerProxyObject<? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<BrokerProxyResources<?>, SWBL, SWR, BrokerProxyList<SWBL, SWR>> {

        private final BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, SWBL, SWR>> resources;

        public List(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, SWBL, SWR>> resources) {
            this.resources = resources;
        }

        @Override
        public BrokerProxyList<SWBL, SWR> create(BrokerProxyResources<?> r,
                                                        ListData<SWBL> data) throws HousemateException {
            return new BrokerProxyList<SWBL, SWR>(resources, data);
        }
    }

    public static class Option implements OptionFactory<BrokerProxyResources<?>, BrokerProxyOption> {
        @Override
        public BrokerProxyOption create(BrokerProxyResources<?> resources, OptionData data) throws HousemateException {
            BrokerProxyResources<List<SubTypeData, BrokerProxySubType>> r
                    = changeFactoryType(resources,
                    new List<SubTypeData, BrokerProxySubType>(changeFactoryType(resources, subTypeFactory)));
            return new BrokerProxyOption(r, data);
        }
    }

    public static class Property implements PropertyFactory<BrokerProxyResources<?>, BrokerProxyProperty> {
        @Override
        public BrokerProxyProperty create(BrokerProxyResources<?> resources, PropertyData data) throws HousemateException {
            BrokerProxyResources<Command> r = changeFactoryType(resources, commandFactory);
            return new BrokerProxyProperty(r, data);
        }
    }

    public static class SubType implements SubTypeFactory<BrokerProxyResources<?>, BrokerProxySubType> {
        @Override
        public BrokerProxySubType create(BrokerProxyResources<?> resources, SubTypeData data) throws HousemateException {
            return new BrokerProxySubType(noFactoryType(resources), data);
        }
    }

    public static class Type implements TypeFactory<BrokerProxyResources<?>, BrokerProxyType> {
        @Override
        public BrokerProxyType create(BrokerProxyResources<?> resources,
                                             TypeData<?> data) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyType(r, data);
        }
    }

    public static class Value implements ValueFactory<BrokerProxyResources<?>, BrokerProxyValue> {
        @Override
        public BrokerProxyValue create(BrokerProxyResources<?> resources, ValueData data) throws HousemateException {
            return new BrokerProxyValue(noFactoryType(resources), data);
        }
    }

    public static
    <NF extends HousemateObjectFactory<? extends BrokerProxyResources<?>, ?, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>>
                BrokerProxyResources<NF> changeFactoryType(BrokerProxyResources<?> resources, NF newFactory) {
        return resources.<NF>cloneForNewFactory(newFactory);
    }

    public static BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> noFactoryType(BrokerProxyResources<?> resources) {
        return changeFactoryType(resources, null);
    }
}
