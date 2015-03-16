package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 18:47
* To change this template use File | Settings | File Templates.
*/
public class SimpleProxyFactory implements HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> {

    private final Log log;
    private final HousemateObjectFactory<ApplicationData, SimpleProxyApplication> applicationFactory;
    private final HousemateObjectFactory<ApplicationInstanceData, SimpleProxyApplicationInstance> applicationInstanceFactory;
    private final HousemateObjectFactory<AutomationData, SimpleProxyAutomation> automationFactory;
    private final HousemateObjectFactory<CommandData, SimpleProxyCommand> commandFactory;
    private final HousemateObjectFactory<ConditionData, SimpleProxyCondition> conditionFactory;
    private final HousemateObjectFactory<DeviceData, SimpleProxyDevice> deviceFactory;
    private final HousemateObjectFactory<HardwareData, SimpleProxyHardware> hardwareFactory;
    private final HousemateObjectFactory<ListData<HousemateData<?>>, SimpleProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>> listFactory;
    private final HousemateObjectFactory<OptionData, SimpleProxyOption> optionFactory;
    private final HousemateObjectFactory<ParameterData, SimpleProxyParameter> parameterFactory;
    private final HousemateObjectFactory<PropertyData, SimpleProxyProperty> propertyFactory;
    private final HousemateObjectFactory<SubTypeData, SimpleProxySubType> subTypeFactory;
    private final HousemateObjectFactory<TaskData,SimpleProxyTask> taskFactory;
    private final HousemateObjectFactory<TypeData<HousemateData<?>>, SimpleProxyType> typeFactory;
    private final HousemateObjectFactory<UserData, SimpleProxyUser> userFactory;
    private final HousemateObjectFactory<ValueData, SimpleProxyValue> valueFactory;

    @Inject
    public SimpleProxyFactory(Log log,
                              HousemateObjectFactory<ApplicationData, SimpleProxyApplication> applicationFactory,
                              HousemateObjectFactory<ApplicationInstanceData, SimpleProxyApplicationInstance> applicationInstanceFactory,
                              HousemateObjectFactory<AutomationData, SimpleProxyAutomation> automationFactory,
                              HousemateObjectFactory<CommandData, SimpleProxyCommand> commandFactory,
                              HousemateObjectFactory<ConditionData, SimpleProxyCondition> conditionFactory,
                              HousemateObjectFactory<DeviceData, SimpleProxyDevice> deviceFactory,
                              HousemateObjectFactory<HardwareData, SimpleProxyHardware> hardwareFactory,
                              HousemateObjectFactory<ListData<HousemateData<?>>, SimpleProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>> listFactory,
                              HousemateObjectFactory<OptionData, SimpleProxyOption> optionFactory,
                              HousemateObjectFactory<ParameterData, SimpleProxyParameter> parameterFactory,
                              HousemateObjectFactory<PropertyData, SimpleProxyProperty> propertyFactory,
                              HousemateObjectFactory<SubTypeData, SimpleProxySubType> subTypeFactory,
                              HousemateObjectFactory<TaskData,SimpleProxyTask> taskFactory,
                              HousemateObjectFactory<TypeData<HousemateData<?>>, SimpleProxyType> typeFactory,
                              HousemateObjectFactory<UserData, SimpleProxyUser> userFactory,
                              HousemateObjectFactory<ValueData, SimpleProxyValue> valueFactory) {
        this.log = log;
        this.applicationFactory = applicationFactory;
        this.applicationInstanceFactory = applicationInstanceFactory;
        this.automationFactory = automationFactory;
        this.commandFactory = commandFactory;
        this.conditionFactory = conditionFactory;
        this.deviceFactory = deviceFactory;
        this.hardwareFactory = hardwareFactory;
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
        else if(data instanceof ApplicationData)
            return applicationFactory.create((ApplicationData) data);
        else if(data instanceof ApplicationInstanceData)
            return applicationInstanceFactory.create((ApplicationInstanceData) data);
        else if(data instanceof UserData)
            return userFactory.create((UserData) data);
        else if(data instanceof TaskData)
            return taskFactory.create((TaskData) data);
        else if(data instanceof DeviceData)
            return deviceFactory.create((DeviceData) data);
        else if(data instanceof HardwareData)
            return hardwareFactory.create((HardwareData) data);
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
