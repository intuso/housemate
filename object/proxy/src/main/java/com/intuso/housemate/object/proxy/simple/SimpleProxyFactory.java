package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.command.argument.ArgumentFactory;
import com.intuso.housemate.api.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.api.object.condition.ConditionFactory;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceFactory;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.rule.RuleFactory;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.type.option.OptionFactory;
import com.intuso.housemate.api.object.type.option.OptionWrappable;
import com.intuso.housemate.api.object.user.UserFactory;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.ValueFactory;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProxyFactory {

    private final static All allFactory = new All();
    private final static Argument argumentFactory = new Argument();
    private final static Command commandFactory = new Command();
    private final static Condition conditionFactory = new Condition();
    private final static User connectionFactory = new User();
    private final static Consequence consequenceFactory = new Consequence();
    private final static Device deviceFactory = new Device();
    private final static GenericList listFactory = new GenericList();
    private final static Option optionFactory = new Option();
    private final static Property propertyFactory = new Property();
    private final static Rule ruleFactory = new Rule();
    private final static Type typeFactory = new Type();
    private final static Value valueFactory = new Value();

    public static class All implements HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
        @Override
        public ProxyObject<?, ?, ?, ?, ?, ?, ?> create(ProxyResources<?> resources, HousemateObjectWrappable<?> wrappable) throws HousemateException {
            if(wrappable instanceof ArgumentWrappable)
                return argumentFactory.create(resources, (ArgumentWrappable) wrappable);
            else if(wrappable instanceof CommandWrappable)
                return commandFactory.create(resources, (CommandWrappable) wrappable);
            else if(wrappable instanceof ConditionWrappable)
                return conditionFactory.create(resources, (ConditionWrappable) wrappable);
            else if(wrappable instanceof UserWrappable)
                return connectionFactory.create(resources, (UserWrappable) wrappable);
            else if(wrappable instanceof ConsequenceWrappable)
                return consequenceFactory.create(resources, (ConsequenceWrappable) wrappable);
            else if(wrappable instanceof DeviceWrappable)
                return deviceFactory.create(resources, (DeviceWrappable) wrappable);
            else if(wrappable instanceof ListWrappable)
                return listFactory.create(resources, (ListWrappable<HousemateObjectWrappable<?>>) wrappable);
            else if(wrappable instanceof OptionWrappable)
                return optionFactory.create(resources, (OptionWrappable) wrappable);
            else if(wrappable instanceof PropertyWrappable)
                return propertyFactory.create(resources, (PropertyWrappable) wrappable);
            else if(wrappable instanceof RuleWrappable)
                return ruleFactory.create(resources, (RuleWrappable) wrappable);
            else if(wrappable instanceof TypeWrappable)
                return typeFactory.create(resources, (TypeWrappable) wrappable);
            else if(wrappable instanceof ValueWrappable)
                return valueFactory.create(resources, (ValueWrappable) wrappable);
            else
                throw new HousemateException("Don't know how to create an object from " + wrappable.getClass().getName());
        }
    }

    public static class Argument implements ArgumentFactory<ProxyResources<?>, SimpleProxyObject.Argument> {
        @Override
        public SimpleProxyObject.Argument create(ProxyResources<?> resources, ArgumentWrappable wrappable) throws HousemateException {
            return new SimpleProxyObject.Argument(noFactoryType(resources), wrappable);
        }
    }

    public static class Command implements CommandFactory<ProxyResources<?>, SimpleProxyObject.Command> {
        @Override
        public SimpleProxyObject.Command create(ProxyResources<?> resources, CommandWrappable wrappable) throws HousemateException {
            ProxyResources<List<ArgumentWrappable, SimpleProxyObject.Argument>> r = changeFactoryType(resources, new List<ArgumentWrappable, SimpleProxyObject.Argument>());
            ProxyResources<Argument> sr = changeFactoryType(resources, argumentFactory);
            return new SimpleProxyObject.Command(r, sr, wrappable);
        }
    }

    public static class Condition implements ConditionFactory<ProxyResources<?>, SimpleProxyObject.Condition> {
        @Override
        public SimpleProxyObject.Condition create(ProxyResources<?> resources, ConditionWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Condition(r, resources, wrappable);
        }
    }

    public static class User implements UserFactory<ProxyResources<?>, SimpleProxyObject.User> {
        @Override
        public SimpleProxyObject.User create(ProxyResources<?> resources, UserWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.User(r, resources, wrappable);
        }
    }

    public static class Consequence implements ConsequenceFactory<ProxyResources<?>, SimpleProxyObject.Consequence> {
        @Override
        public SimpleProxyObject.Consequence create(ProxyResources<?> resources, ConsequenceWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Consequence(r, resources, wrappable);
        }
    }

    public static class Device implements DeviceFactory<ProxyResources<?>, SimpleProxyObject.Device> {
        @Override
        public SimpleProxyObject.Device create(ProxyResources<?> resources, DeviceWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Device(r, resources, wrappable);
        }
    }

    public static class GenericList implements ListFactory<ProxyResources<?>, HousemateObjectWrappable<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> {

        @Override
        public SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> create(ProxyResources<?> resources, ListWrappable<HousemateObjectWrappable<?>> wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>(r, resources, wrappable);
        }
    }

    public static class List<
                SWBL extends HousemateObjectWrappable<?>,
                SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SWBL, SWR>>, SWBL, SWR, SimpleProxyObject.List<SWBL, SWR>> {

        @Override
        public SimpleProxyObject.List<SWBL, SWR> create(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SWBL, SWR>> resources,
                                                        ListWrappable<SWBL> wrappable) throws HousemateException {
            return new SimpleProxyObject.List<SWBL, SWR>(resources, resources, wrappable);
        }
    }

    public static class Option implements OptionFactory<ProxyResources<?>, SimpleProxyObject.Option> {
        @Override
        public SimpleProxyObject.Option create(ProxyResources<?> resources, OptionWrappable wrappable) throws HousemateException {
            return new SimpleProxyObject.Option(noFactoryType(resources), wrappable);
        }
    }

    public static class Property implements PropertyFactory<ProxyResources<?>, SimpleProxyObject.Property> {
        @Override
        public SimpleProxyObject.Property create(ProxyResources<?> resources, PropertyWrappable wrappable) throws HousemateException {
            ProxyResources<Command> r = changeFactoryType(resources, commandFactory);
            ProxyResources<List<ArgumentWrappable, SimpleProxyObject.Argument>> sr = changeFactoryType(resources, new List<ArgumentWrappable, SimpleProxyObject.Argument>());
            return new SimpleProxyObject.Property(r, sr, wrappable);
        }
    }

    public static class Rule implements RuleFactory<
            ProxyResources<?>,
            SimpleProxyObject.Rule> {
        @Override
        public SimpleProxyObject.Rule create(ProxyResources<?> resources, RuleWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Rule(r, resources, wrappable);
        }
    }

    public static class Type implements TypeFactory<ProxyResources<?>, SimpleProxyObject.Type> {
        @Override
        public SimpleProxyObject.Type create(ProxyResources<?> resources,
                                             TypeWrappable<?> wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Type(r, resources, wrappable);
        }
    }

    public static class Value implements ValueFactory<ProxyResources<?>, SimpleProxyObject.Value> {
        @Override
        public SimpleProxyObject.Value create(ProxyResources<?> resources, ValueWrappable wrappable) throws HousemateException {
            return new SimpleProxyObject.Value(noFactoryType(resources), wrappable);
        }
    }

    public static
            <NF extends HousemateObjectFactory<? extends ProxyResources<?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
            ProxyResources<NF> changeFactoryType(ProxyResources<?> resources, NF newFactory) {
        return new ProxyResources<NF>(resources.getLog(), resources.getProperties(), resources.getRouter(),
                newFactory, resources.getRegexMatcherFactory());
    }

    public static ProxyResources<NoChildrenProxyObjectFactory> noFactoryType(ProxyResources<?> resources) {
        return changeFactoryType(resources, null);
    }
}
