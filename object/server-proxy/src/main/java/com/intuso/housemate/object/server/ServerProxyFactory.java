package com.intuso.housemate.object.server;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.ApplicationFactory;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceFactory;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.AutomationFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.condition.ConditionFactory;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.hardware.HardwareFactory;
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
import com.intuso.utilities.log.Log;

/**
 * Container class for all factories for server objects that are proxies of those on a client
 */
public class ServerProxyFactory {

    public static class All implements HousemateObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> {

        private final Log log;
        private final ApplicationFactory<ServerProxyApplication> applicationFactory;
        private final ApplicationInstanceFactory<ServerProxyApplicationInstance> applicationInstanceFactory;
        private final AutomationFactory<ServerProxyAutomation> automationFactory;
        private final CommandFactory<ServerProxyCommand> commandFactory;
        private final ConditionFactory<ServerProxyCondition> conditionFactory;
        private final DeviceFactory<ServerProxyDevice> deviceFactory;
        private final HardwareFactory<ServerProxyHardware> hardwareFactory;
        private final ListFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>> listFactory;
        private final OptionFactory<ServerProxyOption> optionFactory;
        private final ParameterFactory<ServerProxyParameter> parameterFactory;
        private final PropertyFactory<ServerProxyProperty> propertyFactory;
        private final SubTypeFactory<ServerProxySubType> subTypeFactory;
        private final TaskFactory<ServerProxyTask> taskFactory;
        private final TypeFactory<ServerProxyType> typeFactory;
        private final UserFactory<ServerProxyUser> userFactory;
        private final ValueFactory<ServerProxyValue> valueFactory;

        @Inject
        public All(Log log, ApplicationFactory<ServerProxyApplication> applicationFactory, ApplicationInstanceFactory<ServerProxyApplicationInstance> applicationInstanceFactory, AutomationFactory<ServerProxyAutomation> automationFactory, CommandFactory<ServerProxyCommand> commandFactory, ConditionFactory<ServerProxyCondition> conditionFactory, HardwareFactory<ServerProxyHardware> hardwareFactory, DeviceFactory<ServerProxyDevice> deviceFactory, ListFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>> listFactory, OptionFactory<ServerProxyOption> optionFactory, ParameterFactory<ServerProxyParameter> parameterFactory, PropertyFactory<ServerProxyProperty> propertyFactory, SubTypeFactory<ServerProxySubType> subTypeFactory, TaskFactory<ServerProxyTask> taskFactory, TypeFactory<ServerProxyType> typeFactory, UserFactory<ServerProxyUser> userFactory, ValueFactory<ServerProxyValue> valueFactory) {
            this.log = log;
            this.applicationFactory = applicationFactory;
            this.applicationInstanceFactory = applicationInstanceFactory;
            this.automationFactory = automationFactory;
            this.commandFactory = commandFactory;
            this.conditionFactory = conditionFactory;
            this.hardwareFactory = hardwareFactory;
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
        public ServerProxyObject<?, ?, ?, ?, ?> create(HousemateData<?> data) {
            if(data instanceof ApplicationData)
                return applicationFactory.create((ApplicationData) data);
            else if(data instanceof ApplicationInstanceData)
                return applicationInstanceFactory.create((ApplicationInstanceData) data);
            else if(data instanceof AutomationData)
                return automationFactory.create((AutomationData) data);
            else if(data instanceof CommandData)
                return commandFactory.create((CommandData) data);
            else if(data instanceof ConditionData)
                return conditionFactory.create((ConditionData) data);
            else if(data instanceof DeviceData)
                return deviceFactory.create((DeviceData) data);
            else if(data instanceof HardwareData)
                return hardwareFactory.create((HardwareData) data);
            else if(data instanceof ListData)
                return listFactory.create((ListData<HousemateData<?>>) data);
            else if(data instanceof OptionData)
                return optionFactory.create((OptionData) data);
            else if(data instanceof ParameterData)
                return parameterFactory.create((ParameterData) data);
            else if(data instanceof PropertyData)
                return propertyFactory.create((PropertyData) data);
            else if(data instanceof SubTypeData)
                return subTypeFactory.create((SubTypeData) data);
            else if(data instanceof TaskData)
                return taskFactory.create((TaskData) data);
            else if(data instanceof TypeData)
                return typeFactory.create((TypeData) data);
            else if(data instanceof UserData)
                return userFactory.create((UserData) data);
            else if(data instanceof ValueData)
                return valueFactory.create((ValueData) data);
            log.w("Don't know how to create an object from " + data.getClass().getName());
            return null;
        }
    }
}
