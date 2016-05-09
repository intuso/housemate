package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.*;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.data.api.ObjectFactory;
import org.slf4j.Logger;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 18:47
* To change this template use File | Settings | File Templates.
*/
public class GWTProxyFactory implements ObjectFactory<Object.Data<?>, ProxyObject<?, ?, ?, ?, ?>> {

    private final Logger logger;
    private final ObjectFactory<ApplicationData, GWTProxyApplication> applicationFactory;
    private final ObjectFactory<ApplicationInstanceData, GWTProxyApplicationInstance> applicationInstanceFactory;
    private final ObjectFactory<Automation.Data, GWTProxyAutomation> automationFactory;
    private final ObjectFactory<Command.Data, GWTProxyCommand> commandFactory;
    private final ObjectFactory<Condition.Data, GWTProxyCondition> conditionFactory;
    private final ObjectFactory<Device.Data, GWTProxyDevice> deviceFactory;
    private final ObjectFactory<Feature.Data, GWTProxyFeature> featureFactory;
    private final ObjectFactory<Hardware.Data, GWTProxyHardware> hardwareFactory;
    private final ObjectFactory<List.Data<Object.Data<?>>, GWTProxyList<Object.Data<?>, ProxyObject<?, ?, ?, ?, ?>>> listFactory;
    private final ObjectFactory<Option.Data, GWTProxyOption> optionFactory;
    private final ObjectFactory<Parameter.Data, GWTProxyParameter> parameterFactory;
    private final ObjectFactory<Property.Data, GWTProxyProperty> propertyFactory;
    private final ObjectFactory<Server.Data, GWTProxyServer> serverFactory;
    private final ObjectFactory<SubType.Data, GWTProxySubType> subTypeFactory;
    private final ObjectFactory<Task.Data,GWTProxyTask> taskFactory;
    private final ObjectFactory<Type.Data<Object.Data<?>>, GWTProxyType> typeFactory;
    private final ObjectFactory<User.Data, GWTProxyUser> userFactory;
    private final ObjectFactory<Value.Data, GWTProxyValue> valueFactory;

    @Inject
    public GWTProxyFactory(Logger logger,
                           ObjectFactory<ApplicationData, GWTProxyApplication> applicationFactory,
                           ObjectFactory<ApplicationInstanceData, GWTProxyApplicationInstance> applicationInstanceFactory,
                           ObjectFactory<Automation.Data, GWTProxyAutomation> automationFactory,
                           ObjectFactory<Command.Data, GWTProxyCommand> commandFactory,
                           ObjectFactory<Condition.Data, GWTProxyCondition> conditionFactory,
                           ObjectFactory<Device.Data, GWTProxyDevice> deviceFactory,
                           ObjectFactory<Feature.Data, GWTProxyFeature> featureFactory,
                           ObjectFactory<Hardware.Data, GWTProxyHardware> hardwareFactory,
                           ObjectFactory<List.Data<Object.Data<?>>, GWTProxyList<Object.Data<?>, ProxyObject<?, ?, ?, ?, ?>>> listFactory,
                           ObjectFactory<Option.Data, GWTProxyOption> optionFactory,
                           ObjectFactory<Parameter.Data, GWTProxyParameter> parameterFactory,
                           ObjectFactory<Property.Data, GWTProxyProperty> propertyFactory,
                           ObjectFactory<Server.Data, GWTProxyServer> serverFactory,
                           ObjectFactory<SubType.Data, GWTProxySubType> subTypeFactory,
                           ObjectFactory<Task.Data,GWTProxyTask> taskFactory,
                           ObjectFactory<Type.Data<Object.Data<?>>, GWTProxyType> typeFactory,
                           ObjectFactory<User.Data, GWTProxyUser> userFactory,
                           ObjectFactory<Value.Data, GWTProxyValue> valueFactory) {
        this.logger = logger;
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
    public ProxyObject<?, ?, ?, ?, ?> create(Logger logger, Object.Data<?> data) {
        if(data instanceof Parameter.Data)
            return parameterFactory.create(logger, (Parameter.Data) data);
        else if(data instanceof Command.Data)
            return commandFactory.create(logger, (Command.Data) data);
        else if(data instanceof Condition.Data)
            return conditionFactory.create(logger, (Condition.Data) data);
        else if(data instanceof ApplicationData)
            return applicationFactory.create(logger, (ApplicationData) data);
        else if(data instanceof ApplicationInstanceData)
            return applicationInstanceFactory.create(logger, (ApplicationInstanceData) data);
        else if(data instanceof User.Data)
            return userFactory.create(logger, (User.Data) data);
        else if(data instanceof Task.Data)
            return taskFactory.create(logger, (Task.Data) data);
        else if(data instanceof Device.Data)
            return deviceFactory.create(logger, (Device.Data) data);
        else if(data instanceof Feature.Data)
            return featureFactory.create(logger, (Feature.Data) data);
        else if(data instanceof Hardware.Data)
            return hardwareFactory.create(logger, (Hardware.Data) data);
        else if(data instanceof List.Data)
            return listFactory.create(logger, (List.Data<Object.Data<?>>) data);
        else if(data instanceof Option.Data)
            return optionFactory.create(logger, (Option.Data) data);
        else if(data instanceof Property.Data)
            return propertyFactory.create(logger, (Property.Data) data);
        else if(data instanceof Automation.Data)
            return automationFactory.create(logger, (Automation.Data) data);
        else if(data instanceof Server.Data)
            return serverFactory.create(logger, (Server.Data) data);
        else if(data instanceof SubType.Data)
            return subTypeFactory.create(logger, (SubType.Data) data);
        else if(data instanceof Type.Data)
            return typeFactory.create(logger, (Type.Data) data);
        else if(data instanceof Value.Data)
            return valueFactory.create(logger, (Value.Data) data);
        logger.warn("Don't know how to create an object from " + data.getClass().getName());
        return null;
    }
}
