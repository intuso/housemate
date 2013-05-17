package com.intuso.housemate.broker.object.proxy;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.command.CommandFactory;
import com.intuso.housemate.core.object.command.CommandWrappable;
import com.intuso.housemate.core.object.command.argument.ArgumentFactory;
import com.intuso.housemate.core.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.core.object.device.DeviceFactory;
import com.intuso.housemate.core.object.device.DeviceWrappable;
import com.intuso.housemate.core.object.list.ListFactory;
import com.intuso.housemate.core.object.list.ListWrappable;
import com.intuso.housemate.core.object.property.PropertyFactory;
import com.intuso.housemate.core.object.property.PropertyWrappable;
import com.intuso.housemate.core.object.type.TypeFactory;
import com.intuso.housemate.core.object.type.TypeWrappable;
import com.intuso.housemate.core.object.type.option.OptionFactory;
import com.intuso.housemate.core.object.type.option.OptionWrappable;
import com.intuso.housemate.core.object.value.ValueFactory;
import com.intuso.housemate.core.object.value.ValueWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyFactory {

    private final static All allFactory = new All();
    private final static Argument argumentFactory = new Argument();
    private final static Command commandFactory = new Command();
    private final static Device deviceFactory = new Device();
    private final static GenericList listFactory = new GenericList();
    private final static Option optionFactory = new Option();
    private final static Property propertyFactory = new Property();
    private final static Type typeFactory = new Type();
    private final static Value valueFactory = new Value();

    public static class All implements HousemateObjectFactory<BrokerProxyResources<?>, HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>> {
        @Override
        public BrokerProxyObject<?, ?, ?, ?, ?> create(BrokerProxyResources<?> resources, HousemateObjectWrappable<?> wrappable) throws HousemateException {
            if(wrappable instanceof ArgumentWrappable)
                return argumentFactory.create(resources, (ArgumentWrappable) wrappable);
            else if(wrappable instanceof CommandWrappable)
                return commandFactory.create(resources, (CommandWrappable) wrappable);
            else if(wrappable instanceof DeviceWrappable)
                return deviceFactory.create(resources, (DeviceWrappable) wrappable);
            else if(wrappable instanceof ListWrappable)
                return listFactory.create(resources, (ListWrappable<HousemateObjectWrappable<?>>) wrappable);
            else if(wrappable instanceof OptionWrappable)
                return optionFactory.create(resources, (OptionWrappable) wrappable);
            else if(wrappable instanceof PropertyWrappable)
                return propertyFactory.create(resources, (PropertyWrappable) wrappable);
            else if(wrappable instanceof TypeWrappable)
                return typeFactory.create(resources, (TypeWrappable) wrappable);
            else if(wrappable instanceof ValueWrappable)
                return valueFactory.create(resources, (ValueWrappable) wrappable);
            else
                throw new HousemateException("Don't know how to create an object from " + wrappable.getClass().getName());
        }
    }

    public static class Argument implements ArgumentFactory<BrokerProxyResources<?>, BrokerProxyArgument> {
        @Override
        public BrokerProxyArgument create(BrokerProxyResources<?> resources, ArgumentWrappable wrappable) throws HousemateException {
            return new BrokerProxyArgument(noFactoryType(resources), wrappable);
        }
    }

    public static class Command implements CommandFactory<BrokerProxyResources<?>, BrokerProxyCommand> {
        @Override
        public BrokerProxyCommand create(BrokerProxyResources<?> resources, CommandWrappable wrappable) throws HousemateException {
            BrokerProxyResources<List<ArgumentWrappable, BrokerProxyArgument>> r = changeFactoryType(resources, new List<ArgumentWrappable, BrokerProxyArgument>(changeFactoryType(resources, argumentFactory)));
            return new BrokerProxyCommand(r, wrappable);
        }
    }

    public static class Device implements DeviceFactory<BrokerProxyResources<?>, BrokerProxyDevice> {
        @Override
        public BrokerProxyDevice create(BrokerProxyResources<?> resources, DeviceWrappable wrappable) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyDevice(r, wrappable);
        }
    }

    public static class GenericList implements ListFactory<BrokerProxyResources<?>, HousemateObjectWrappable<?>,
            BrokerProxyObject<?, ?, ?, ?, ?>,
            BrokerProxyList<HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>>> {

        @Override
        public BrokerProxyList<HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>> create(BrokerProxyResources<?> resources, ListWrappable<HousemateObjectWrappable<?>> wrappable) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyList<HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>>(r, wrappable);
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
                                                        ListWrappable<SWBL> wrappable) throws HousemateException {
            return new BrokerProxyList<SWBL, SWR>(resources, wrappable);
        }
    }

    public static class Option implements OptionFactory<BrokerProxyResources<?>, BrokerProxyOption> {
        @Override
        public BrokerProxyOption create(BrokerProxyResources<?> resources, OptionWrappable wrappable) throws HousemateException {
            return new BrokerProxyOption(BrokerProxyFactory.<NoChildrenBrokerProxyObjectFactory>changeFactoryType(resources, null), wrappable);
        }
    }

    public static class Property implements PropertyFactory<BrokerProxyResources<?>, BrokerProxyProperty> {
        @Override
        public BrokerProxyProperty create(BrokerProxyResources<?> resources, PropertyWrappable wrappable) throws HousemateException {
            BrokerProxyResources<Command> r = changeFactoryType(resources, commandFactory);
            return new BrokerProxyProperty(r, wrappable);
        }
    }

    public static class Type implements TypeFactory<BrokerProxyResources<?>, BrokerProxyType> {
        @Override
        public BrokerProxyType create(BrokerProxyResources<?> resources,
                                             TypeWrappable<?> wrappable) throws HousemateException {
            BrokerProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new BrokerProxyType(r, wrappable);
        }
    }

    public static class Value implements ValueFactory<BrokerProxyResources<?>, BrokerProxyValue> {
        @Override
        public BrokerProxyValue create(BrokerProxyResources<?> resources, ValueWrappable wrappable) throws HousemateException {
            return new BrokerProxyValue(BrokerProxyFactory.<NoChildrenBrokerProxyObjectFactory>changeFactoryType(resources, null), wrappable);
        }
    }

    public static
    <NF extends HousemateObjectFactory<? extends BrokerProxyResources<?>, ?, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>>
    BrokerProxyResources<NF> changeFactoryType(BrokerProxyResources<?> resources, NF newFactory) {
        BrokerProxyResources<NF> result = new BrokerProxyResources<NF>(resources.getGeneralResources(), newFactory);
        resources.setRoot(result.getRoot());
        return result;
    }

    public static BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> noFactoryType(BrokerProxyResources<?> resources) {
        return changeFactoryType(resources, null);
    }
}
