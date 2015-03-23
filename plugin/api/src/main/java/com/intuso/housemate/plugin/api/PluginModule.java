package com.intuso.housemate.plugin.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.factory.condition.RealConditionFactory;
import com.intuso.housemate.object.real.factory.device.RealDeviceFactory;
import com.intuso.housemate.object.real.factory.hardware.RealHardwareFactory;
import com.intuso.housemate.object.real.factory.task.RealTaskFactory;
import com.intuso.utilities.log.Log;

import java.lang.reflect.Type;

/**
 * Base class for all plugins that wish to use annotations to describe the provided features
 */
public abstract class PluginModule extends AbstractModule {

    public static String MANIFEST_ATTRIBUTE = "Housemate-Plugin";

    private final Log log;

    public PluginModule(Log log) {
        this.log = log;
    }

    public void configure() {

        // configure the classloader to be this plugin's class' classloader
        bind(ClassLoader.class).toInstance(getClass().getClassLoader());

        // configure all the things a plugin can provide
        configureTypes(Multibinder.newSetBinder(binder(), new TypeLiteral<RealType<?, ?, ?>>() {}));
        configureComparators(Multibinder.newSetBinder(binder(), new TypeLiteral<Comparator<?>>() {}));
        configureOperators(Multibinder.newSetBinder(binder(), new TypeLiteral<Operator<?, ?>>() {}));
        configureTransformers(Multibinder.newSetBinder(binder(), new TypeLiteral<Transformer<?, ?>>() {}));
        configureHardwareFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<RealHardwareFactory<?>>() {}));
        configureDeviceFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<RealDeviceFactory<?>>() {}));
        configureConditionFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<RealConditionFactory<?>>() {}));
        configureTaskFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<RealTaskFactory<?>>() {}));
    }

    @Provides
    @Singleton
    public TypeInfo getTypeInfo() {
        return getClass().getAnnotation(TypeInfo.class);
    }

    public void configureTypes(Multibinder<RealType<?, ?, ?>> typeBindings) {
        Types types = getClass().getAnnotation(Types.class);
        if(types != null)
            for(Class<? extends RealType<?, ?, ?>> typeClass : types.value())
                typeBindings.addBinding().to(typeClass);
    }

    public void configureComparators(Multibinder<Comparator<?>> comparatorBindings) {
        Comparators comparators = getClass().getAnnotation(Comparators.class);
        if(comparators != null)
            for(Class<? extends Comparator<?>> comparatorsClass : comparators.value())
                comparatorBindings.addBinding().to(comparatorsClass);
    }

    public void configureOperators(Multibinder<Operator<?, ?>> operatorBindings) {
        Operators operators = getClass().getAnnotation(Operators.class);
        if(operators != null)
            for(Class<? extends Operator<?, ?>> operatorClass : operators.value())
                operatorBindings.addBinding().to(operatorClass);
    }

    public void configureTransformers(Multibinder<Transformer<?, ?>> transformerBindings) {
        Transformers transformers = getClass().getAnnotation(Transformers.class);
        if(transformers != null)
            for(Class<? extends Transformer<?, ?>> transformerClass : transformers.value())
                transformerBindings.addBinding().to(transformerClass);
    }

    public void configureHardwareFactories(Multibinder<RealHardwareFactory<?>> hardwareFactoryBindings) {
        Hardwares hardwares = getClass().getAnnotation(Hardwares.class);
        if(hardwares != null) {
            for(Class<? extends RealHardware> hardwareClass : hardwares.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedType(RealHardwareFactory.class, hardwareClass);
                TypeLiteral<RealHardwareFactory<?>> typeLiteral = (TypeLiteral<RealHardwareFactory<?>>) TypeLiteral.get(type);
                hardwareFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureDeviceFactories(Multibinder<RealDeviceFactory<?>> deviceFactoryBindings) {
        Devices devices = getClass().getAnnotation(Devices.class);
        if(devices != null) {
            for(Class<? extends RealDevice> deviceClass : devices.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedType(RealDeviceFactory.class, deviceClass);
                TypeLiteral<RealDeviceFactory<?>> typeLiteral = (TypeLiteral<RealDeviceFactory<?>>) TypeLiteral.get(type);
                deviceFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureConditionFactories(Multibinder<RealConditionFactory<?>> conditionFactoryBindings) {
        Conditions conditions = getClass().getAnnotation(Conditions.class);
        if(conditions != null) {
            for(Class<? extends RealCondition> conditionClass : conditions.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedType(RealConditionFactory.class, conditionClass);
                TypeLiteral<RealConditionFactory<?>> typeLiteral = (TypeLiteral<RealConditionFactory<?>>) TypeLiteral.get(type);
                conditionFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureTaskFactories(Multibinder<RealTaskFactory<?>> taskFactoryBindings) {
        Tasks tasks = getClass().getAnnotation(Tasks.class);
        if(tasks != null) {
            for(Class<? extends RealTask> taskClass : tasks.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedType(RealTaskFactory.class, taskClass);
                TypeLiteral<RealTaskFactory<?>> typeLiteral = (TypeLiteral<RealTaskFactory<?>>) TypeLiteral.get(type);
                taskFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }
}
