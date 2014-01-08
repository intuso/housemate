package com.intuso.housemate.annotations.plugin;

import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.plugin.api.*;
import com.intuso.utilities.log.Log;

import java.lang.reflect.Constructor;

/**
 * Base class for all plugins that wish to use annotations to describe the provided features
 */
public class AnnotatedPluginModule extends PluginModule {

    private final Log log;

    public AnnotatedPluginModule(Log log) {
        this.log = log;
    }

    @Override
    public TypeInfo getTypeInfo() {
        Class<?> clazz = getClass();
        PluginInformation information = getClass().getAnnotation(PluginInformation.class);
        if(information == null)
            throw new HousemateRuntimeException("Annotated plugin " + getClass().getName() + " has no "
                    + PluginInformation.class.getName() + " annotation");
        return new TypeInfo(information.id(), information.name(), information.description());
    }

    @Override
    public void configureTypes(Multibinder<RealType<?, ?, ?>> typeBindings) {
        Types types = getClass().getAnnotation(Types.class);
        if(types != null)
            for(Class<? extends RealType<?, ?, ?>> typeClass : types.value())
                typeBindings.addBinding().to(typeClass);
    }

    @Override
    public void configureComparators(Multibinder<Comparator<?>> comparatorBindings) {
        Comparators comparators = getClass().getAnnotation(Comparators.class);
        if(comparators != null)
            for(Class<? extends Comparator<?>> comparatorsClass : comparators.value())
                comparatorBindings.addBinding().to(comparatorsClass);
    }

    @Override
    public void configureOperators(Multibinder<Operator<?, ?>> operatorBindings) {
        Operators operators = getClass().getAnnotation(Operators.class);
        if(operators != null)
            for(Class<? extends Operator<?, ?>> operatorClass : operators.value())
                operatorBindings.addBinding().to(operatorClass);
    }

    @Override
    public void configureTransformers(Multibinder<Transformer<?, ?>> transformerBindings) {
        Transformers transformers = getClass().getAnnotation(Transformers.class);
        if(transformers != null)
            for(Class<? extends Transformer<?, ?>> transformerClass : transformers.value())
                transformerBindings.addBinding().to(transformerClass);
    }

    @Override
    public void configureDeviceFactories(Multibinder<RealDeviceFactory<?>> deviceFactoryBindings) {
        DeviceFactories deviceFactories = getClass().getAnnotation(DeviceFactories.class);
        if(deviceFactories != null) {
            for(Class<? extends RealDeviceFactory<?>> factoryClass : deviceFactories.value())
                deviceFactoryBindings.addBinding().to(factoryClass);
        }
        Devices devices = getClass().getAnnotation(Devices.class);
        if(devices != null) {
            for(Class<? extends RealDevice> deviceClass : devices.value()) {
                FactoryInformation information = deviceClass.getAnnotation(FactoryInformation.class);
                if(information == null)
                    log.e("Device class " + deviceClass.getName() + " has no " + FactoryInformation.class.getName() + " annotation");
                else {
                    Constructor<? extends RealDevice> constructor;
                    try {
                        constructor = deviceClass.getConstructor(Log.class, String.class, String.class, String.class);
                        deviceFactoryBindings.addBinding().toInstance(new SimpleDeviceFactory(information, constructor));
                    } catch(NoSuchMethodException e) {
                        log.e("Device class " + deviceClass.getName() + " does not have the correct constructor");
                    }
                }
            }
        }
    }

    @Override
    public void configureConditionFactories(Multibinder<ServerConditionFactory<?>> conditionFactoryBindings) {
        ConditionFactories conditionFactories = getClass().getAnnotation(ConditionFactories.class);
        if(conditionFactories != null) {
            for(Class<? extends ServerConditionFactory<?>> factoryClass : conditionFactories.value())
                conditionFactoryBindings.addBinding().to(factoryClass);
        }
        Conditions conditions = getClass().getAnnotation(Conditions.class);
        if(conditions != null) {
            for(Class<? extends ServerRealCondition> conditionClass : conditions.value()) {
                FactoryInformation information = conditionClass.getAnnotation(FactoryInformation.class);
                if(information == null)
                    log.e("Condition class " + conditionClass.getName() + " has no "
                            + FactoryInformation.class.getName() + " annotation");
                else {
                    Constructor<? extends ServerRealCondition> constructor;
                    try {
                        constructor = conditionClass.getConstructor(
                                Log.class, String.class, String.class, String.class,
                                ServerRealConditionOwner.class, LifecycleHandler.class);
                        conditionFactoryBindings.addBinding().toInstance(new SimpleConditionFactory(information, constructor));
                    } catch(NoSuchMethodException e) {
                        log.e("Condition class " + conditionClass.getName() + " does not have the correct constructor");
                    }
                }
            }
        }
    }

    @Override
    public void configureTaskFactories(Multibinder<ServerTaskFactory<?>> taskFactoryBindings) {
        TaskFactories taskFactories = getClass().getAnnotation(TaskFactories.class);
        if(taskFactories != null) {
            for(Class<? extends ServerTaskFactory<?>> factoryClass : taskFactories.value())
                taskFactoryBindings.addBinding().to(factoryClass);
        }
        Tasks tasks = getClass().getAnnotation(Tasks.class);
        if(tasks != null) {
            for(Class<? extends ServerRealTask> taskClass : tasks.value()) {
                FactoryInformation information = taskClass.getAnnotation(FactoryInformation.class);
                if(information == null)
                    log.e("Task class " + taskClass.getName() + " has no "
                            + FactoryInformation.class.getName() + " annotation");
                else {
                    Constructor<? extends ServerRealTask> constructor;
                    try {
                        constructor = taskClass.getConstructor(
                                Log.class, String.class, String.class, String.class, ServerRealTaskOwner.class);
                        taskFactoryBindings.addBinding().toInstance(new SimpleTaskFactory(information, constructor));
                    } catch(NoSuchMethodException e) {
                        log.e("Task class " + taskClass.getName() + " does not have the correct constructor");
                    }
                }
            }
        }
    }
}
