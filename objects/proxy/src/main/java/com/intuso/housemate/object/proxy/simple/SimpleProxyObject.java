package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.comms.Router;
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
import com.intuso.housemate.object.proxy.*;
import com.intuso.utilities.log.Log;

/**
 * Container class for a simple implementation of all the proxy objects
 */
public final class SimpleProxyObject {

    public final static class Automation extends ProxyAutomation<
            Command,
            Value,
            Condition,
            List<ConditionData, Condition>, Task, List<TaskData, Task>, Automation> {
        @Inject
        public Automation(Log log,
                          Injector injector,
                          @Assisted AutomationData data) {
            super(log, injector, data);
        }
    }

    public final static class Command extends ProxyCommand<
            Parameter,
                List<ParameterData, Parameter>,
                Command> {
        @Inject
        public Command(Log log,
                       Injector injector,
                       @Assisted CommandData data) {
            super(log, injector, data);
        }
    }

    public final static class Condition extends ProxyCondition<
                Command, Value,
                List<PropertyData, Property>,
                Condition,
                List<ConditionData, Condition>> {
        @Inject
        public Condition(Log log,
                         Injector injector,
                         @Assisted ConditionData data) {
            super(log, injector, data);
        }
    }

    public final static class Device extends ProxyDevice<
                Command,
                List<CommandData, Command>, Value,
                List<ValueData, Value>,
                Property,
                List<PropertyData, Property>,
                SimpleProxyFeature,
                Device> {
        @Inject
        public Device(Log log,
                      Injector injector,
                      @Assisted DeviceData data) {
            super(log, injector, data);
        }
    }

    public final static class List<
                WBL extends HousemateData<?>,
                WR extends ProxyObject<? extends WBL, ?, ?, ?, ?>>
            extends ProxyList<
                WBL,
                WR,
                List<WBL, WR>> {
        @Inject
        public List(Log log,
                    Injector injector,
                    @Assisted ListData<WBL> data) {
            super(log, injector, data);
        }
    }

    public final static class Option extends ProxyOption<
            SubType,
            List<SubTypeData, SubType>,
            Option> {
        @Inject
        public Option(Log log,
                      Injector injector,
                      @Assisted OptionData data) {
            super(log, injector, data);
        }
    }

    public final static class Parameter extends ProxyParameter<Type, Parameter> {
        @Inject
        public Parameter(Log log,
                         Injector injector,
                         @Assisted ParameterData data) {
            super(log, injector, data);
        }
    }

    public final static class Property extends ProxyProperty<
            Type,
            Command,
            Property> {
        @Inject
        public Property(Log log,
                        Injector injector,
                        @Assisted PropertyData data) {
            super(log, injector, data);
        }
    }

    public final static class Root extends ProxyRootObject<
            User, List<UserData, User>,
            Type, List<TypeData<?>, Type>,
            Device, List<DeviceData, Device>,
            Automation, List<AutomationData, Automation>,
            Command, Root> {
        @Inject
        public Root(Log log,
                    Injector injector,
                    Router router) {
            super(log, injector, router);
        }
    }

    public final static class SubType extends ProxySubType<Type, SubType> {
        @Inject
        public SubType(Log log,
                       Injector injector,
                       @Assisted SubTypeData data) {
            super(log, injector, data);
        }
    }

    public final static class Task extends ProxyTask<
            Command,
            Value,
            List<PropertyData, Property>,
            Task> {
        @Inject
        public Task(Log log,
                    Injector injector,
                    @Assisted TaskData data) {
            super(log, injector, data);
        }
    }

    public final static class Type extends ProxyType<
                TypeData<HousemateData<?>>,
                HousemateData<?>,
                ProxyObject<?, ?, ?, ?, ?>,
                Type> {
        @Inject
        public Type(Log log,
                    Injector injector,
                    @Assisted TypeData<HousemateData<?>> data) {
            super(log, injector, data);
        }
    }

    public final static class User extends ProxyUser<
            Command,
            User> {
        @Inject
        public User(Log log,
                    Injector injector,
                    @Assisted UserData data) {
            super(log, injector, data);
        }
    }

    public final static class Value extends ProxyValue<Type, Value> {
        @Inject
        public Value(Log log,
                     Injector injector,
                     @Assisted ValueData data) {
            super(log, injector, data);
        }
    }
}
