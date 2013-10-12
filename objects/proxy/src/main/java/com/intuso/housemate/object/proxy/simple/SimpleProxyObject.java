package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyAutomation;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.object.proxy.ProxyCondition;
import com.intuso.housemate.object.proxy.ProxyDevice;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.housemate.object.proxy.ProxyParameter;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.proxy.ProxySubType;
import com.intuso.housemate.object.proxy.ProxyTask;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.housemate.object.proxy.ProxyUser;
import com.intuso.housemate.object.proxy.ProxyValue;

/**
 * Container class for a simple implementation of all the proxy objects
 */
public final class SimpleProxyObject {

    public final static class Automation extends ProxyAutomation<
            SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SimpleProxyResources<?>,
            Command,
            Value,
            Condition,
            List<ConditionData, Condition>, Task, List<TaskData, Task>, Automation> {
        public Automation(SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                          SimpleProxyResources<?> childResources,
                          AutomationData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Command extends ProxyCommand<SimpleProxyResources<SimpleProxyFactory.List<ParameterData, Parameter>>,
                SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, ParameterData, Parameter>>,
            Parameter,
                List<ParameterData, Parameter>,
                Command> {
        public Command(SimpleProxyResources<SimpleProxyFactory.List<ParameterData, Parameter>> resources,
                       SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, ParameterData, Parameter>> childResources,
                       CommandData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Condition extends ProxyCondition<SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                SimpleProxyResources<?>,
                Command, Value,
                List<PropertyData, Property>,
                Condition,
                List<ConditionData, Condition>> {
        public Condition(SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                         SimpleProxyResources<?> childResources,
                         ConditionData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Device extends ProxyDevice<
                SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                SimpleProxyResources<?>,
                Command,
                List<CommandData, Command>, Value,
                List<ValueData, Value>,
                Property,
                List<PropertyData, Property>,
                SimpleProxyFeature,
                Device> {
        public Device(SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                      SimpleProxyResources<?> childResources,
                      DeviceData data) {
            super(resources, childResources, data);
        }
    }

    public final static class List<
                WBL extends HousemateData<?>,
                WR extends ProxyObject<?, ?, ? extends WBL, ?, ?, ?, ?>>
            extends ProxyList<SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, WBL, WR>>,
                SimpleProxyResources<?>,
                WBL,
                WR,
                List<WBL, WR>> {
        public List(SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, WBL, WR>> resources,
                    SimpleProxyResources<?> childResources,
                    ListData<WBL> listData) {
            super(resources, childResources, listData);
        }
    }

    public final static class Option extends ProxyOption<
            SimpleProxyResources<SimpleProxyFactory.List<SubTypeData, SubType>>,
            SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, SubTypeData, SubType>>,
            SubType,
            List<SubTypeData, SubType>,
            Option> {
        public Option(SimpleProxyResources<SimpleProxyFactory.List<SubTypeData, SubType>> resources,
                      SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, SubTypeData, SubType>> childResources,
                      OptionData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Parameter extends ProxyParameter<SimpleProxyResources<NoChildrenProxyObjectFactory>, Type, Parameter> {
        public Parameter(SimpleProxyResources<NoChildrenProxyObjectFactory> resources, ParameterData data) {
            super(resources, data);
        }
    }

    public final static class Property extends ProxyProperty<SimpleProxyResources<SimpleProxyFactory.Command>,
            SimpleProxyResources<?>,
            Type,
            Command,
            Property> {
        public Property(SimpleProxyResources<SimpleProxyFactory.Command> resources,
                        SimpleProxyResources<SimpleProxyFactory.List<ParameterData, Parameter>> childResources,
                        PropertyData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Root extends ProxyRootObject<
            SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SimpleProxyResources<?>,
            User, List<UserData, User>,
            Type, List<TypeData<?>, Type>,
            Device, List<DeviceData, Device>,
            Automation, List<AutomationData, Automation>,
            Command, Root> {
        public Root(SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    SimpleProxyResources<?> childResources) {
            super(resources, childResources);
        }
    }

    public final static class SubType extends ProxySubType<SimpleProxyResources<NoChildrenProxyObjectFactory>, Type, SubType> {
        public SubType(SimpleProxyResources<NoChildrenProxyObjectFactory> resources, SubTypeData data) {
            super(resources, data);
        }
    }

    public final static class Task extends ProxyTask<SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SimpleProxyResources<?>,
            Command,
            Value,
            List<PropertyData, Property>,
            Task> {
        public Task(SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    SimpleProxyResources<?> childResources,
                    TaskData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Type extends ProxyType<
                SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                SimpleProxyResources<?>,
                TypeData<HousemateData<?>>,
                HousemateData<?>,
                ProxyObject<?, ?, ?, ?, ?, ?, ?>,
                Type> {
        public Type(SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    SimpleProxyResources<?> childResources,
                    TypeData data) {
            super(resources, childResources, data);
        }
    }

    public final static class User extends ProxyUser<SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SimpleProxyResources<?>, Command, User> {
        public User(SimpleProxyResources<? extends HousemateObjectFactory<SimpleProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    SimpleProxyResources<?> childResources,
                    UserData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Value extends ProxyValue<SimpleProxyResources<NoChildrenProxyObjectFactory>, Type, Value> {
        public Value(SimpleProxyResources<NoChildrenProxyObjectFactory> resources, ValueData value) {
            super(resources, value);
        }
    }
}
