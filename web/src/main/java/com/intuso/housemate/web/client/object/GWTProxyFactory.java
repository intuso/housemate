package com.intuso.housemate.web.client.object;

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
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyFactory {

    private final static All allFactory = new All();
    private final static Argument argumentFactory = new Argument();
    private final static Command commandFactory = new Command();
    private final static Condition conditionFactory = new Condition();
    private final static Connection connectionFactory = new Connection();
    private final static Consequence consequenceFactory = new Consequence();
    private final static Device deviceFactory = new Device();
    private final static GenericList listFactory = new GenericList();
    private final static Option optionFactory = new Option();
    private final static Property propertyFactory = new Property();
    private final static Rule ruleFactory = new Rule();
    private final static Type typeFactory = new Type();
    private final static Value valueFactory = new Value();

    public static class All implements HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
        @Override
        public ProxyObject<?, ?, ?, ?, ?, ?, ?> create(GWTResources<?> resources, HousemateObjectWrappable<?> wrappable) throws HousemateException {
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

    public static class Argument implements ArgumentFactory<GWTResources<?>, GWTProxyArgument> {
        @Override
        public GWTProxyArgument create(GWTResources<?> resources, ArgumentWrappable wrappable) throws HousemateException {
            return new GWTProxyArgument(noFactoryType(resources), wrappable);
        }
    }

    public static class Command implements CommandFactory<GWTResources<?>, GWTProxyCommand> {
        @Override
        public GWTProxyCommand create(GWTResources<?> resources, CommandWrappable wrappable) throws HousemateException {
            GWTResources<List<ArgumentWrappable, GWTProxyArgument>> r = changeFactoryType(resources, new List<ArgumentWrappable, GWTProxyArgument>());
            GWTResources<Argument> sr = changeFactoryType(resources, argumentFactory);
            return new GWTProxyCommand(r, sr, wrappable);
        }
    }

    public static class Condition implements ConditionFactory<GWTResources<?>, GWTProxyCondition> {
        @Override
        public GWTProxyCondition create(GWTResources<?> resources, ConditionWrappable wrappable) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyCondition(r, resources, wrappable);
        }
    }

    public static class Connection implements UserFactory<GWTResources<?>, GWTProxyUser> {
        @Override
        public GWTProxyUser create(GWTResources<?> resources, UserWrappable wrappable) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyUser(r, resources, wrappable);
        }
    }

    public static class Consequence implements ConsequenceFactory<GWTResources<?>, GWTProxyConsequence> {
        @Override
        public GWTProxyConsequence create(GWTResources<?> resources, ConsequenceWrappable wrappable) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyConsequence(r, resources, wrappable);
        }
    }

    public static class Device implements DeviceFactory<GWTResources<?>, GWTProxyDevice> {
        @Override
        public GWTProxyDevice create(GWTResources<?> resources, DeviceWrappable wrappable) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyDevice(r, resources, wrappable);
        }
    }

    public static class GenericList implements ListFactory<GWTResources<?>, HousemateObjectWrappable<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            GWTProxyList<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> {

        @Override
        public GWTProxyList<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> create(GWTResources<?> resources, ListWrappable<HousemateObjectWrappable<?>> wrappable) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyList<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>(r, resources, wrappable);
        }
    }

    public static class List<
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>>, SWBL, SWR, GWTProxyList<SWBL, SWR>> {

        @Override
        public GWTProxyList<SWBL, SWR> create(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>> resources,
                                                        ListWrappable<SWBL> wrappable) throws HousemateException {
            return new GWTProxyList<SWBL, SWR>(resources, resources, wrappable);
        }
    }

    public static class Option implements OptionFactory<GWTResources<?>, GWTProxyOption> {
        @Override
        public GWTProxyOption create(GWTResources<?> resources, OptionWrappable wrappable) throws HousemateException {
            return new GWTProxyOption(GWTProxyFactory.<NoChildrenProxyObjectFactory>changeFactoryType(resources, null), wrappable);
        }
    }

    public static class Property implements PropertyFactory<GWTResources<?>, GWTProxyProperty> {
        @Override
        public GWTProxyProperty create(GWTResources<?> resources, PropertyWrappable wrappable) throws HousemateException {
            GWTResources<Command> r = changeFactoryType(resources, commandFactory);
            GWTResources<List<ArgumentWrappable, GWTProxyArgument>> sr = changeFactoryType(resources, new List<ArgumentWrappable, GWTProxyArgument>());
            return new GWTProxyProperty(r, sr, wrappable);
        }
    }

    public static class Rule implements RuleFactory<
            GWTResources<?>,
            GWTProxyRule> {
        @Override
        public GWTProxyRule create(GWTResources<?> resources, RuleWrappable wrappable) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyRule(r, resources, wrappable);
        }
    }

    public static class Type implements TypeFactory<GWTResources<?>, GWTProxyType> {
        @Override
        public GWTProxyType create(GWTResources<?> resources,
                                             TypeWrappable<?> wrappable) throws HousemateException {
            GWTResources<All> r = changeFactoryType(resources, allFactory);
            return new GWTProxyType(r, resources, wrappable);
        }
    }

    public static class Value implements ValueFactory<GWTResources<?>, GWTProxyValue> {
        @Override
        public GWTProxyValue create(GWTResources<?> resources, ValueWrappable wrappable) throws HousemateException {
            return new GWTProxyValue(GWTProxyFactory.<NoChildrenProxyObjectFactory>changeFactoryType(resources, null), wrappable);
        }
    }

    public static <NF extends HousemateObjectFactory<? extends ProxyResources<?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
            GWTResources<NF> changeFactoryType(GWTResources<?> resources, NF newFactory) {
        return new GWTResources<NF>(resources.getLog(), resources.getProperties(), resources.getRouter(),
                newFactory, resources.getRegexMatcherFactory());
    }

    public static GWTResources<NoChildrenProxyObjectFactory> noFactoryType(GWTResources<?> resources) {
        return changeFactoryType(resources, null);
    }
}