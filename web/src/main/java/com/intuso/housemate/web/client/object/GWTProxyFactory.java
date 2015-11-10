package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.*;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 18:47
* To change this template use File | Settings | File Templates.
*/
public class GWTProxyFactory implements ObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>> {

    private final Log log;
    private final ObjectFactory<ApplicationData, GWTProxyApplication> applicationFactory;
    private final ObjectFactory<ApplicationInstanceData, GWTProxyApplicationInstance> applicationInstanceFactory;
    private final ObjectFactory<AutomationData, GWTProxyAutomation> automationFactory;
    private final ObjectFactory<CommandData, GWTProxyCommand> commandFactory;
    private final ObjectFactory<ConditionData, GWTProxyCondition> conditionFactory;
    private final ObjectFactory<DeviceData, GWTProxyDevice> deviceFactory;
    private final ObjectFactory<FeatureData, GWTProxyFeature> featureFactory;
    private final ObjectFactory<HardwareData, GWTProxyHardware> hardwareFactory;
    private final ObjectFactory<ListData<HousemateData<?>>, GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>> listFactory;
    private final ObjectFactory<OptionData, GWTProxyOption> optionFactory;
    private final ObjectFactory<ParameterData, GWTProxyParameter> parameterFactory;
    private final ObjectFactory<PropertyData, GWTProxyProperty> propertyFactory;
    private final ObjectFactory<ServerData, GWTProxyServer> serverFactory;
    private final ObjectFactory<SubTypeData, GWTProxySubType> subTypeFactory;
    private final ObjectFactory<TaskData,GWTProxyTask> taskFactory;
    private final ObjectFactory<TypeData<HousemateData<?>>, GWTProxyType> typeFactory;
    private final ObjectFactory<UserData, GWTProxyUser> userFactory;
    private final ObjectFactory<ValueData, GWTProxyValue> valueFactory;

    @Inject
    public GWTProxyFactory(Log log,
                           ObjectFactory<ApplicationData, GWTProxyApplication> applicationFactory,
                           ObjectFactory<ApplicationInstanceData, GWTProxyApplicationInstance> applicationInstanceFactory,
                           ObjectFactory<AutomationData, GWTProxyAutomation> automationFactory,
                           ObjectFactory<CommandData, GWTProxyCommand> commandFactory,
                           ObjectFactory<ConditionData, GWTProxyCondition> conditionFactory,
                           ObjectFactory<DeviceData, GWTProxyDevice> deviceFactory,
                           ObjectFactory<FeatureData, GWTProxyFeature> featureFactory,
                           ObjectFactory<HardwareData, GWTProxyHardware> hardwareFactory,
                           ObjectFactory<ListData<HousemateData<?>>, GWTProxyList<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>> listFactory,
                           ObjectFactory<OptionData, GWTProxyOption> optionFactory,
                           ObjectFactory<ParameterData, GWTProxyParameter> parameterFactory,
                           ObjectFactory<PropertyData, GWTProxyProperty> propertyFactory,
                           ObjectFactory<ServerData, GWTProxyServer> serverFactory,
                           ObjectFactory<SubTypeData, GWTProxySubType> subTypeFactory,
                           ObjectFactory<TaskData,GWTProxyTask> taskFactory,
                           ObjectFactory<TypeData<HousemateData<?>>, GWTProxyType> typeFactory,
                           ObjectFactory<UserData, GWTProxyUser> userFactory,
                           ObjectFactory<ValueData, GWTProxyValue> valueFactory) {
        this.log = log;
        this.applicationFactory = applicationFactory;
        this.applicationInstanceFactory = applicationInstanceFactory;
        this.automationFactory = automationFactory;
        this.commandFactory = commandFactory;
        this.conditionFactory = conditionFactory;
        this.deviceFactory = deviceFactory;
        this.featureFactory = featureFactory;
        this.hardwareFactory = hardwareFactory;
        this.listFactory = listFactory;
        this.optionFactory = optionFactory;
        this.parameterFactory = parameterFactory;
        this.propertyFactory = propertyFactory;
        this.serverFactory = serverFactory;
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
        else if(data instanceof FeatureData)
            return featureFactory.create((FeatureData) data);
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
        else if(data instanceof ServerData)
            return serverFactory.create((ServerData) data);
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
