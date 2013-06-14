package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.argument.ArgumentWrappable;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyArgument;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.object.proxy.ProxyCondition;
import com.intuso.housemate.object.proxy.ProxyConsequence;
import com.intuso.housemate.object.proxy.ProxyDevice;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.proxy.ProxyRule;
import com.intuso.housemate.object.proxy.ProxySubType;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.housemate.object.proxy.ProxyUser;
import com.intuso.housemate.object.proxy.ProxyValue;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 01/06/12
 * Time: 01:16
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProxyObject {

    public final static class Root extends ProxyRootObject<
                    ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                    ProxyResources<?>,
                    User, List<UserWrappable, User>,
                    Type, List<TypeWrappable<?>, Type>,
                    Device, List<DeviceWrappable, Device>,
                    Rule, List<RuleWrappable, Rule>,
                    Command, Root> {
        public Root(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> subResources) {
            super(resources, subResources);
        }
    }

    public final static class Argument extends ProxyArgument<ProxyResources<NoChildrenProxyObjectFactory>, Type, Argument> {
        public Argument(ProxyResources<NoChildrenProxyObjectFactory> resources, ArgumentWrappable wrappable) {
            super(resources, wrappable);
        }
    }

    public final static class Command extends ProxyCommand<ProxyResources<SimpleProxyFactory.List<ArgumentWrappable, Argument>>,
                ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, ArgumentWrappable, Argument>>,
                Argument,
                List<ArgumentWrappable, Argument>,
                Command> {
        public Command(ProxyResources<SimpleProxyFactory.List<ArgumentWrappable, Argument>> resources,
                       ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, ArgumentWrappable, Argument>> subResources,
                       CommandWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class Condition extends ProxyCondition<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                Value,
                List<PropertyWrappable, Property>,
                Command,
                Condition,
                List<ConditionWrappable, Condition>> {
        public Condition(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                         ProxyResources<?> subResources,
                         ConditionWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class Consequence extends ProxyConsequence<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                Value,
                List<PropertyWrappable, Property>,
                Consequence> {
        public Consequence(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                           ProxyResources<?> subResources,
                           ConsequenceWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class User extends ProxyUser<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>, Command, User> {
        public User(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> subResources,
                    UserWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class Device extends ProxyDevice<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                Command,
                List<CommandWrappable, Command>, Value, List<ValueWrappable, Value>, Property,
                List<PropertyWrappable, Property>, Device> {
        public Device(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                      ProxyResources<?> subResources,
                      DeviceWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class List<WBL extends HousemateObjectWrappable<?>, WR extends ProxyObject<?, ?, ? extends WBL, ?, ?, ?, ?>>
            extends ProxyList<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, WBL, WR>>,
                ProxyResources<?>,
                WBL,
                WR,
                List<WBL, WR>> {
        public List(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, WBL, WR>> resources,
                    ProxyResources<?> subResources,
                    ListWrappable<WBL> listWrappable) {
            super(resources, subResources, listWrappable);
        }
    }

    public final static class Option extends ProxyOption<
            ProxyResources<SimpleProxyFactory.List<SubTypeWrappable, SubType>>,
            ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SubTypeWrappable, SubType>>,
            SubType,
            List<SubTypeWrappable, SubType>,
            Option> {
        public Option(ProxyResources<SimpleProxyFactory.List<SubTypeWrappable, SubType>> resources,
                      ProxyResources<SimpleProxyFactory.SubType> subResources,
                      OptionWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class Property extends ProxyProperty<ProxyResources<SimpleProxyFactory.Command>,
            ProxyResources<?>,
            Type,
            Command,
            Property> {
        public Property(ProxyResources<SimpleProxyFactory.Command> resources,
                        ProxyResources<SimpleProxyFactory.List<ArgumentWrappable, Argument>> subResources,
                        PropertyWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class Rule extends ProxyRule<
                ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                Property,
                Command,
                Value,
                Condition,
                List<ConditionWrappable, Condition>, Consequence, List<ConsequenceWrappable, Consequence>, Rule> {
        public Rule(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> subResources,
                    RuleWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class SubType extends ProxySubType<ProxyResources<NoChildrenProxyObjectFactory>, Type, SubType> {
        public SubType(ProxyResources<NoChildrenProxyObjectFactory> resources, SubTypeWrappable wrappable) {
            super(resources, wrappable);
        }
    }

    public final static class Type extends ProxyType<
                ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                TypeWrappable<HousemateObjectWrappable<?>>,
                HousemateObjectWrappable<?>,
                ProxyObject<?, ?, ?, ?, ?, ?, ?>,
                Type> {
        public Type(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> subResources,
                    TypeWrappable wrappable) {
            super(resources, subResources, wrappable);
        }
    }

    public final static class Value extends ProxyValue<ProxyResources<NoChildrenProxyObjectFactory>, Type, Value> {
        public Value(ProxyResources<NoChildrenProxyObjectFactory> resources, ValueWrappable value) {
            super(resources, value);
        }
    }
}
