package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionFactory;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionFactory;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterFactory;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeFactory;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.task.TaskFactory;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserFactory;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.api.object.value.ValueFactory;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.log.Log;

/**
 * Container class for factories of all the simple proxy object implementations.
 *
 * @see SimpleProxyObject
 */
public final class SimpleProxyFactory {

    public static class All implements HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> {

        private final Log log;
        private final Automation automationFactory;
        private final Command commandFactory;
        private final Condition conditionFactory;
        private final Device deviceFactory;
        private final GenericList listFactory;
        private final Option optionFactory;
        private final Parameter parameterFactory;
        private final Property propertyFactory;
        private final SubType subTypeFactory;
        private final Task taskFactory;
        private final Type typeFactory;
        private final User userFactory;
        private final Value valueFactory;

        @Inject
        public All(Log log, Automation automationFactory, Command commandFactory, Condition conditionFactory,
                   Device deviceFactory, GenericList listFactory, Option optionFactory, Parameter parameterFactory,
                   Property propertyFactory, SubType subTypeFactory, Task taskFactory, Type typeFactory,
                   User userFactory, Value valueFactory) {
            this.log = log;
            this.automationFactory = automationFactory;
            this.commandFactory = commandFactory;
            this.conditionFactory = conditionFactory;
            this.deviceFactory = deviceFactory;
            this.listFactory = listFactory;
            this.optionFactory = optionFactory;
            this.parameterFactory = parameterFactory;
            this.propertyFactory = propertyFactory;
            this.subTypeFactory = subTypeFactory;
            this.taskFactory = taskFactory;
            this.typeFactory = typeFactory;
            this.userFactory = userFactory;
            this.valueFactory = valueFactory;
        }

        @Override
        public ProxyObject<?, ?, ?, ?, ?> create(HousemateData<?> data) {
            if(data instanceof ParameterData)
                return parameterFactory.create((ParameterData) data);
            else if(data instanceof CommandData)
                return commandFactory.create((CommandData) data);
            else if(data instanceof ConditionData)
                return conditionFactory.create((ConditionData) data);
            else if(data instanceof UserData)
                return userFactory.create((UserData) data);
            else if(data instanceof TaskData)
                return taskFactory.create((TaskData) data);
            else if(data instanceof DeviceData)
                return deviceFactory.create((DeviceData) data);
            else if(data instanceof ListData)
                return listFactory.create((ListData<HousemateData<?>>) data);
            else if(data instanceof OptionData)
                return optionFactory.create((OptionData) data);
            else if(data instanceof PropertyData)
                return propertyFactory.create((PropertyData) data);
            else if(data instanceof AutomationData)
                return automationFactory.create((AutomationData) data);
            else if(data instanceof SubTypeData)
                return subTypeFactory.create((SubTypeData) data);
            else if(data instanceof TypeData)
                return typeFactory.create((TypeData) data);
            else if(data instanceof ValueData)
                return valueFactory.create((ValueData) data);
            log.e("Don't know how to create an object from " + data.getClass().getName());
            return null;
        }
    }

    public interface Automation extends AutomationFactory<SimpleProxyObject.Automation> {}

    public interface Command extends CommandFactory<SimpleProxyObject.Command> {}

    public interface Condition extends ConditionFactory<SimpleProxyObject.Condition> {}

    public interface Device extends DeviceFactory<SimpleProxyObject.Device> {}

    public interface GenericList extends ListFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>,
            SimpleProxyObject.List<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>> {}

    public interface List<SWBL extends HousemateData<?>, SWR extends ProxyObject<? extends SWBL, ?, ?, ?, ?>>
            extends ListFactory<SWBL, SWR, SimpleProxyObject.List<SWBL, SWR>> {}

    public interface Option extends OptionFactory<SimpleProxyObject.Option> {}

    public interface Parameter extends ParameterFactory<SimpleProxyObject.Parameter> {}

    public interface Property extends PropertyFactory<SimpleProxyObject.Property> {}

    public interface SubType extends SubTypeFactory<SimpleProxyObject.SubType> {}

    public interface Task extends TaskFactory<SimpleProxyObject.Task> {}

    public interface Type extends TypeFactory<SimpleProxyObject.Type> {}

    public interface User extends UserFactory<SimpleProxyObject.User> {}

    public interface Value extends ValueFactory<SimpleProxyObject.Value> {}
}
