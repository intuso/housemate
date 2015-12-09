package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.utilities.object.ObjectFactory;
import org.slf4j.Logger;

/**
 * Container class for all factories for server objects that are proxies of those on a client
 */
public class ServerProxyFactory {

    public static class All implements ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> {

        private final Logger logger;
        private final Provider<ObjectFactory<ApplicationData, ServerProxyApplication>> applicationFactory;
        private final Provider<ObjectFactory<ApplicationInstanceData, ServerProxyApplicationInstance>> applicationInstanceFactory;
        private final Provider<ObjectFactory<AutomationData, ServerProxyAutomation>> automationFactory;
        private final Provider<ObjectFactory<CommandData, ServerProxyCommand>> commandFactory;
        private final Provider<ObjectFactory<ConditionData, ServerProxyCondition>> conditionFactory;
        private final Provider<ObjectFactory<DeviceData, ServerProxyDevice>> deviceFactory;
        private final Provider<ObjectFactory<FeatureData, ServerProxyFeature>> featureFactory;
        private final Provider<ObjectFactory<HardwareData, ServerProxyHardware>> hardwareFactory;
        private final Provider<ObjectFactory<ListData<HousemateData<?>>, ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>> listFactory;
        private final Provider<ObjectFactory<OptionData, ServerProxyOption>> optionFactory;
        private final Provider<ObjectFactory<ParameterData, ServerProxyParameter>> parameterFactory;
        private final Provider<ObjectFactory<PropertyData, ServerProxyProperty>> propertyFactory;
        private final Provider<ObjectFactory<SubTypeData, ServerProxySubType>> subTypeFactory;
        private final Provider<ObjectFactory<TaskData, ServerProxyTask>> taskFactory;
        private final Provider<ObjectFactory<TypeData<HousemateData<?>>, ServerProxyType>> typeFactory;
        private final Provider<ObjectFactory<UserData, ServerProxyUser>> userFactory;
        private final Provider<ObjectFactory<ValueData, ServerProxyValue>> valueFactory;

        @Inject
        public All(Logger logger, Provider<ObjectFactory<ApplicationData, ServerProxyApplication>> applicationFactory,
                Provider<ObjectFactory<ApplicationInstanceData, ServerProxyApplicationInstance>> applicationInstanceFactory,
                Provider<ObjectFactory<AutomationData, ServerProxyAutomation>> automationFactory,
                Provider<ObjectFactory<CommandData, ServerProxyCommand>> commandFactory,
                Provider<ObjectFactory<ConditionData, ServerProxyCondition>> conditionFactory,
                Provider<ObjectFactory<DeviceData, ServerProxyDevice>> deviceFactory,
                Provider<ObjectFactory<FeatureData, ServerProxyFeature>> featureFactory,
                Provider<ObjectFactory<HardwareData, ServerProxyHardware>> hardwareFactory,
                Provider<ObjectFactory<ListData<HousemateData<?>>, ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>> listFactory,
                Provider<ObjectFactory<OptionData, ServerProxyOption>> optionFactory,
                Provider<ObjectFactory<ParameterData, ServerProxyParameter>> parameterFactory,
                Provider<ObjectFactory<PropertyData, ServerProxyProperty>> propertyFactory,
                Provider<ObjectFactory<SubTypeData, ServerProxySubType>> subTypeFactory,
                Provider<ObjectFactory<TaskData, ServerProxyTask>> taskFactory,
                Provider<ObjectFactory<TypeData<HousemateData<?>>, ServerProxyType>> typeFactory,
                Provider<ObjectFactory<UserData, ServerProxyUser>> userFactory,
                Provider<ObjectFactory<ValueData, ServerProxyValue>> valueFactory) {
            this.logger = logger;
            this.applicationFactory = applicationFactory;
            this.applicationInstanceFactory = applicationInstanceFactory;
            this.automationFactory = automationFactory;
            this.commandFactory = commandFactory;
            this.conditionFactory = conditionFactory;
            this.hardwareFactory = hardwareFactory;
            this.deviceFactory = deviceFactory;
            this.featureFactory = featureFactory;
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
                return applicationFactory.get().create((ApplicationData) data);
            else if(data instanceof ApplicationInstanceData)
                return applicationInstanceFactory.get().create((ApplicationInstanceData) data);
            else if(data instanceof AutomationData)
                return automationFactory.get().create((AutomationData) data);
            else if(data instanceof CommandData)
                return commandFactory.get().create((CommandData) data);
            else if(data instanceof ConditionData)
                return conditionFactory.get().create((ConditionData) data);
            else if(data instanceof DeviceData)
                return deviceFactory.get().create((DeviceData) data);
            else if(data instanceof FeatureData)
                return featureFactory.get().create((FeatureData) data);
            else if(data instanceof HardwareData)
                return hardwareFactory.get().create((HardwareData) data);
            else if(data instanceof ListData)
                return listFactory.get().create((ListData<HousemateData<?>>) data);
            else if(data instanceof OptionData)
                return optionFactory.get().create((OptionData) data);
            else if(data instanceof ParameterData)
                return parameterFactory.get().create((ParameterData) data);
            else if(data instanceof PropertyData)
                return propertyFactory.get().create((PropertyData) data);
            else if(data instanceof SubTypeData)
                return subTypeFactory.get().create((SubTypeData) data);
            else if(data instanceof TaskData)
                return taskFactory.get().create((TaskData) data);
            else if(data instanceof TypeData)
                return typeFactory.get().create((TypeData) data);
            else if(data instanceof UserData)
                return userFactory.get().create((UserData) data);
            else if(data instanceof ValueData)
                return valueFactory.get().create((ValueData) data);
            logger.warn("Don't know how to create an object from " + data.getClass().getName());
            return null;
        }
    }
}
