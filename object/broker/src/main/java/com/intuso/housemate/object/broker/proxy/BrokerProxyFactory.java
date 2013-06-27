package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.parameter.ParameterFactory;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.OptionFactory;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeFactory;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.value.ValueFactory;
import com.intuso.housemate.api.object.value.ValueWrappable;

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

    public static class All implements HousemateObjectFactory<BrokerProxyResources<?>, HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>> {
        @Override
        public BrokerProxyObject<?, ?, ?, ?, ?> create(BrokerProxyResources<?> resources, HousemateObjectWrappable<?> data) throws HousemateException {
            if(data instanceof ParameterWrappable)
                return parameterFactory.create(resources, (ParameterWrappable) data);
            else if(data instanceof CommandWrappable)
                return commandFactory.create(resources, (CommandWrappable) data);
            else if(data instanceof DeviceWrappable)
                return deviceFactory.create(resources, (DeviceWrappable) data);
            else if(data instanceof ListWrappable)
                return listFactory.create(resources, (ListWrappable<HousemateObjectWrappable<?>>) data);
            else if(data instanceof OptionWrappable)
                return optionFactory.create(resources, (OptionWrappable) data);
            else if(data instanceof PropertyWrappable)
                return propertyFactory.create(resources, (PropertyWrappable) data);
            else if(data instanceof SubTypeWrappable)
                return subTypeFactory.create(resources, (SubTypeWrappable) data);
            else if(data instanceof TypeWrappable)
                return typeFactory.create(resources, (TypeWrappable) data);
            else if(data instanceof ValueWrappable)
                return valueFactory.create(resources, (ValueWrappable) data);
            else
                throw new HousemateException("Don't know how to create an object from " + data.getClass().getName());
        }
    }

    public static class Parameter implements ParameterFactory<BrokerProxyResources<?>, BrokerProxyParameter> {
        @Override
        public BrokerProxyParameter create(BrokerProxyResources<?> resources, ParameterWrappable data) throws HousemateException {
            return new BrokerProxyParameter(noFactoryType(resources), data);
        }
    }

    public static class Command implements CommandFactory<BrokerProxyResources<?>, BrokerProxyCommand> {
        @Override
        public BrokerProxyCommand create(BrokerProxyResources<?> resources, CommandWrappable data) throws HousemateException {
            BrokerProxyResources<List<ParameterWrappable, BrokerProxyParameter>> r
                    = changeFactoryType(resources,
                    new List<ParameterWrappable, BrokerProxyParameter>(changeFactoryType(resources, parameterFactory)));
            return new BrokerProxyCommand(r, data);
        }
    }

    public static class Device implements DeviceFactory<BrokerProxyResources<?>, BrokerProxyDevice> {
        @Override
        public BrokerProxyDevice create(BrokerProxyResources<?> resources, DeviceWrappable data) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyDevice(r, data);
        }
    }

    public static class GenericList implements ListFactory<BrokerProxyResources<?>, HousemateObjectWrappable<?>,
            BrokerProxyObject<?, ?, ?, ?, ?>,
            BrokerProxyList<HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>>> {

        @Override
        public BrokerProxyList<HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>> create(BrokerProxyResources<?> resources, ListWrappable<HousemateObjectWrappable<?>> data) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyList<HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>>(r, data);
        }
    }

    public static class List<
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends BrokerProxyObject<? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<BrokerProxyResources<?>, SWBL, SWR, BrokerProxyList<SWBL, SWR>> {

        private final BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, SWBL, SWR>> resources;

        public List(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, SWBL, SWR>> resources) {
            this.resources = resources;
        }

        @Override
        public BrokerProxyList<SWBL, SWR> create(BrokerProxyResources<?> r,
                                                        ListWrappable<SWBL> data) throws HousemateException {
            return new BrokerProxyList<SWBL, SWR>(resources, data);
        }
    }

    public static class Option implements OptionFactory<BrokerProxyResources<?>, BrokerProxyOption> {
        @Override
        public BrokerProxyOption create(BrokerProxyResources<?> resources, OptionWrappable data) throws HousemateException {
            BrokerProxyResources<List<SubTypeWrappable, BrokerProxySubType>> r
                    = changeFactoryType(resources,
                    new List<SubTypeWrappable, BrokerProxySubType>(changeFactoryType(resources, subTypeFactory)));
            return new BrokerProxyOption(r, data);
        }
    }

    public static class Property implements PropertyFactory<BrokerProxyResources<?>, BrokerProxyProperty> {
        @Override
        public BrokerProxyProperty create(BrokerProxyResources<?> resources, PropertyWrappable data) throws HousemateException {
            BrokerProxyResources<Command> r = changeFactoryType(resources, commandFactory);
            return new BrokerProxyProperty(r, data);
        }
    }

    public static class SubType implements SubTypeFactory<BrokerProxyResources<?>, BrokerProxySubType> {
        @Override
        public BrokerProxySubType create(BrokerProxyResources<?> resources, SubTypeWrappable data) throws HousemateException {
            return new BrokerProxySubType(noFactoryType(resources), data);
        }
    }

    public static class Type implements TypeFactory<BrokerProxyResources<?>, BrokerProxyType> {
        @Override
        public BrokerProxyType create(BrokerProxyResources<?> resources,
                                             TypeWrappable<?> data) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyType(r, data);
        }
    }

    public static class Value implements ValueFactory<BrokerProxyResources<?>, BrokerProxyValue> {
        @Override
        public BrokerProxyValue create(BrokerProxyResources<?> resources, ValueWrappable data) throws HousemateException {
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
