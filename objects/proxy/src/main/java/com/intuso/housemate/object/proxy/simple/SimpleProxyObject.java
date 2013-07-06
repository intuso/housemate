package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateData;
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
import com.intuso.housemate.object.proxy.ProxyParameter;
import com.intuso.housemate.object.proxy.ProxyAutomation;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.object.proxy.ProxyCondition;
import com.intuso.housemate.object.proxy.ProxyTask;
import com.intuso.housemate.object.proxy.ProxyDevice;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.proxy.ProxySubType;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.housemate.object.proxy.ProxyUser;
import com.intuso.housemate.object.proxy.ProxyValue;

/**
 * Container class for a simple implementation of all the proxy objects
 */
public class SimpleProxyObject {

    public final static class Automation extends ProxyAutomation<
            ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            ProxyResources<?>,
            Command,
            Value,
            Condition,
            List<ConditionData, Condition>, Task, List<TaskData, Task>, Automation> {
        public Automation(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                          ProxyResources<?> childResources,
                          AutomationData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Command extends ProxyCommand<ProxyResources<SimpleProxyFactory.List<ParameterData, Parameter>>,
                ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, ParameterData, Parameter>>,
            Parameter,
                List<ParameterData, Parameter>,
                Command> {
        public Command(ProxyResources<SimpleProxyFactory.List<ParameterData, Parameter>> resources,
                       ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, ParameterData, Parameter>> childResources,
                       CommandData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Condition extends ProxyCondition<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                Value,
                List<PropertyData, Property>,
                Command,
                Condition,
                List<ConditionData, Condition>> {
        public Condition(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                         ProxyResources<?> childResources,
                         ConditionData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Device extends ProxyDevice<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                Command,
                List<CommandData, Command>, Value, List<ValueData, Value>, Property,
                List<PropertyData, Property>, Device> {
        public Device(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                      ProxyResources<?> childResources,
                      DeviceData data) {
            super(resources, childResources, data);
        }
    }

    public final static class List<WBL extends HousemateData<?>, WR extends ProxyObject<?, ?, ? extends WBL, ?, ?, ?, ?>>
            extends ProxyList<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, WBL, WR>>,
                ProxyResources<?>,
                WBL,
                WR,
                List<WBL, WR>> {
        public List(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, WBL, WR>> resources,
                    ProxyResources<?> childResources,
                    ListData<WBL> listWrappable) {
            super(resources, childResources, listWrappable);
        }
    }

    public final static class Option extends ProxyOption<
            ProxyResources<SimpleProxyFactory.List<SubTypeData, SubType>>,
            ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SubTypeData, SubType>>,
            SubType,
            List<SubTypeData, SubType>,
            Option> {
        public Option(ProxyResources<SimpleProxyFactory.List<SubTypeData, SubType>> resources,
                      ProxyResources<SimpleProxyFactory.SubType> childResources,
                      OptionData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Parameter extends ProxyParameter<ProxyResources<NoChildrenProxyObjectFactory>, Type, Parameter> {
        public Parameter(ProxyResources<NoChildrenProxyObjectFactory> resources, ParameterData data) {
            super(resources, data);
        }
    }

    public final static class Property extends ProxyProperty<ProxyResources<SimpleProxyFactory.Command>,
            ProxyResources<?>,
            Type,
            Command,
            Property> {
        public Property(ProxyResources<SimpleProxyFactory.Command> resources,
                        ProxyResources<SimpleProxyFactory.List<ParameterData, Parameter>> childResources,
                        PropertyData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Root extends ProxyRootObject<
            ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            ProxyResources<?>,
            User, List<UserData, User>,
            Type, List<TypeData<?>, Type>,
            Device, List<DeviceData, Device>,
            Automation, List<AutomationData, Automation>,
            Command, Root> {
        public Root(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> childResources) {
            super(resources, childResources);
        }
    }

    public final static class SubType extends ProxySubType<ProxyResources<NoChildrenProxyObjectFactory>, Type, SubType> {
        public SubType(ProxyResources<NoChildrenProxyObjectFactory> resources, SubTypeData data) {
            super(resources, data);
        }
    }

    public final static class Task extends ProxyTask<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            ProxyResources<?>,
            Value,
            List<PropertyData, Property>,
            Task> {
        public Task(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> childResources,
                    TaskData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Type extends ProxyType<
                ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
            TypeData<HousemateData<?>>,
            HousemateData<?>,
                ProxyObject<?, ?, ?, ?, ?, ?, ?>,
                Type> {
        public Type(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> childResources,
                    TypeData data) {
            super(resources, childResources, data);
        }
    }

    public final static class User extends ProxyUser<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            ProxyResources<?>, Command, User> {
        public User(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> childResources,
                    UserData data) {
            super(resources, childResources, data);
        }
    }

    public final static class Value extends ProxyValue<ProxyResources<NoChildrenProxyObjectFactory>, Type, Value> {
        public Value(ProxyResources<NoChildrenProxyObjectFactory> resources, ValueData value) {
            super(resources, value);
        }
    }
}
