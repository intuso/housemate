package com.intuso.housemate.plugin.api.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;

import java.lang.reflect.Type;

/**
 * Base class for all plugins that wish to use annotations to describe the provided features
 */
public abstract class AnnotatedPluginModule extends AbstractModule {

    public void configure() {

        // configure the classloader to be this plugin's class' classloader
        bind(ClassLoader.class).toInstance(getClass().getClassLoader());

        // configure all the things a plugin can provide
        configureTypes(Multibinder.newSetBinder(binder(), new TypeLiteral<RealType<?>>() {}));
        configureComparators(Multibinder.newSetBinder(binder(), new TypeLiteral<Comparator<?>>() {}));
        configureOperators(Multibinder.newSetBinder(binder(), new TypeLiteral<Operator<?, ?>>() {}));
        configureTransformers(Multibinder.newSetBinder(binder(), new TypeLiteral<Transformer<?, ?>>() {}));
        configureHardwareFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<HardwareDriver.Factory<?>>() {}));
        configureDeviceFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<DeviceDriver.Factory<?>>() {}));
        configureConditionFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<ConditionDriver.Factory<?>>() {}));
        configureTaskFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<TaskDriver.Factory<?>>() {}));
    }

    @Provides
    @Singleton
    public TypeInfo getTypeInfo() {
        return getClass().getAnnotation(TypeInfo.class);
    }

    public void configureTypes(Multibinder<RealType<?>> typeBindings) {
        Types types = getClass().getAnnotation(Types.class);
        if(types != null)
            for(Class<? extends RealType<?>> typeClass : types.value())
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

    public void configureHardwareFactories(Multibinder<HardwareDriver.Factory<?>> hardwareFactoryBindings) {
        HardwareDrivers hardwareDrivers = getClass().getAnnotation(HardwareDrivers.class);
        if(hardwareDrivers != null) {
            for(Class<? extends HardwareDriver> driverClass : hardwareDrivers.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedTypeWithOwner(HardwareDriver.class, HardwareDriver.Factory.class, driverClass);
                TypeLiteral<HardwareDriver.Factory<?>> typeLiteral = (TypeLiteral<HardwareDriver.Factory<?>>) TypeLiteral.get(type);
                hardwareFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureDeviceFactories(Multibinder<DeviceDriver.Factory<?>> deviceFactoryBindings) {
        DeviceDrivers deviceDrivers = getClass().getAnnotation(DeviceDrivers.class);
        if(deviceDrivers != null) {
            for(Class<? extends DeviceDriver> driverClass : deviceDrivers.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedTypeWithOwner(DeviceDriver.class, DeviceDriver.Factory.class, driverClass);
                TypeLiteral<DeviceDriver.Factory<?>> typeLiteral = (TypeLiteral<DeviceDriver.Factory<?>>) TypeLiteral.get(type);
                deviceFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureConditionFactories(Multibinder<ConditionDriver.Factory<?>> conditionFactoryBindings) {
        ConditionDrivers conditionDrivers = getClass().getAnnotation(ConditionDrivers.class);
        if(conditionDrivers != null) {
            for(Class<? extends ConditionDriver> driverClass : conditionDrivers.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedTypeWithOwner(ConditionDriver.class, ConditionDriver.Factory.class, driverClass);
                TypeLiteral<ConditionDriver.Factory<?>> typeLiteral = (TypeLiteral<ConditionDriver.Factory<?>>) TypeLiteral.get(type);
                conditionFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureTaskFactories(Multibinder<TaskDriver.Factory<?>> taskFactoryBindings) {
        TaskDrivers taskDrivers = getClass().getAnnotation(TaskDrivers.class);
        if(taskDrivers != null) {
            for(Class<? extends TaskDriver> driverClass : taskDrivers.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedTypeWithOwner(TaskDriver.class, TaskDriver.Factory.class, driverClass);
                TypeLiteral<TaskDriver.Factory<?>> typeLiteral = (TypeLiteral<TaskDriver.Factory<?>>) TypeLiteral.get(type);
                taskFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }
}
