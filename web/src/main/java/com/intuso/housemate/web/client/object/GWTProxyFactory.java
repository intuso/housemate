package com.intuso.housemate.web.client.object;

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
public class GWTProxyFactory implements HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> {

    private final Log log;
    private final HousemateObjectFactory<ApplicationData, GWTProxyApplication> applicationFactory;
    private final HousemateObjectFactory<ApplicationInstanceData, GWTProxyApplicationInstance> applicationInstanceFactory;
    private final HousemateObjectFactory<AutomationData, GWTProxyAutomation> automationFactory;
    private final HousemateObjectFactory<CommandData, GWTProxyCommand> commandFactory;
    private final HousemateObjectFactory<ConditionData, GWTProxyCondition> conditionFactory;
    private final HousemateObjectFactory<DeviceData, GWTProxyDevice> deviceFactory;
    private final HousemateObjectFactory<HardwareData, GWTProxyHardware> hardwareFactory;
    private final HousemateObjectFactory<ListData<HousemateData<?>>, GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>> listFactory;
    private final HousemateObjectFactory<OptionData, GWTProxyOption> optionFactory;
    private final HousemateObjectFactory<ParameterData, GWTProxyParameter> parameterFactory;
    private final HousemateObjectFactory<PropertyData, GWTProxyProperty> propertyFactory;
    private final HousemateObjectFactory<SubTypeData, GWTProxySubType> subTypeFactory;
    private final HousemateObjectFactory<TaskData,GWTProxyTask> taskFactory;
    private final HousemateObjectFactory<TypeData<HousemateData<?>>, GWTProxyType> typeFactory;
    private final HousemateObjectFactory<UserData, GWTProxyUser> userFactory;
    private final HousemateObjectFactory<ValueData, GWTProxyValue> valueFactory;

    @Inject
    public GWTProxyFactory(Log log,
                           HousemateObjectFactory<ApplicationData, GWTProxyApplication> applicationFactory,
                           HousemateObjectFactory<ApplicationInstanceData, GWTProxyApplicationInstance> applicationInstanceFactory,
                           HousemateObjectFactory<AutomationData, GWTProxyAutomation> automationFactory,
                           HousemateObjectFactory<CommandData, GWTProxyCommand> commandFactory,
                           HousemateObjectFactory<ConditionData, GWTProxyCondition> conditionFactory,
                           HousemateObjectFactory<DeviceData, GWTProxyDevice> deviceFactory,
                           HousemateObjectFactory<HardwareData, GWTProxyHardware> hardwareFactory,
                           HousemateObjectFactory<ListData<HousemateData<?>>, GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>> listFactory,
                           HousemateObjectFactory<OptionData, GWTProxyOption> optionFactory,
                           HousemateObjectFactory<ParameterData, GWTProxyParameter> parameterFactory,
                           HousemateObjectFactory<PropertyData, GWTProxyProperty> propertyFactory,
                           HousemateObjectFactory<SubTypeData, GWTProxySubType> subTypeFactory,
                           HousemateObjectFactory<TaskData,GWTProxyTask> taskFactory,
                           HousemateObjectFactory<TypeData<HousemateData<?>>, GWTProxyType> typeFactory,
                           HousemateObjectFactory<UserData, GWTProxyUser> userFactory,
                           HousemateObjectFactory<ValueData, GWTProxyValue> valueFactory) {
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
        log.w("Don't know how to create an object from " + data.getClass().getName());
        return null;
    }
}
