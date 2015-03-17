package com.intuso.housemate.plugin.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.hardware.HardwareFactory;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealHardware;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealTask;
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
        configureHardwareFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<HardwareFactory<? extends RealHardware>>() {}));
        configureDeviceFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<DeviceFactory<? extends RealDevice>>() {}));
        configureConditionFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<ServerConditionFactory<?>>() {}));
        configureTaskFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<ServerTaskFactory<?>>() {}));
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

    public void configureHardwareFactories(Multibinder<HardwareFactory<? extends RealHardware>> hardwareFactoryBindings) {
        Hardwares hardwares = getClass().getAnnotation(Hardwares.class);
        if(hardwares != null) {
            for(Class<? extends RealHardware> hardwareClass : hardwares.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedType(HardwareFactory.class, hardwareClass);
                TypeLiteral<HardwareFactory<? extends RealHardware>> typeLiteral = (TypeLiteral<HardwareFactory<? extends RealHardware>>) TypeLiteral.get(type);
                hardwareFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureDeviceFactories(Multibinder<DeviceFactory<? extends RealDevice>> deviceFactoryBindings) {
        Devices devices = getClass().getAnnotation(Devices.class);
        if(devices != null) {
            for(Class<? extends RealDevice> deviceClass : devices.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedType(DeviceFactory.class, deviceClass);
                TypeLiteral<DeviceFactory<? extends RealDevice>> typeLiteral = (TypeLiteral<DeviceFactory<? extends RealDevice>>) TypeLiteral.get(type);
                deviceFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureConditionFactories(Multibinder<ServerConditionFactory<?>> conditionFactoryBindings) {
        Conditions conditions = getClass().getAnnotation(Conditions.class);
        if(conditions != null) {
            for(Class<? extends ServerRealCondition> conditionClass : conditions.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedType(ServerConditionFactory.class, conditionClass);
                TypeLiteral<ServerConditionFactory<?>> typeLiteral = (TypeLiteral<ServerConditionFactory<?>>) TypeLiteral.get(type);
                conditionFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }

    public void configureTaskFactories(Multibinder<ServerTaskFactory<?>> taskFactoryBindings) {
        Tasks tasks = getClass().getAnnotation(Tasks.class);
        if(tasks != null) {
            for(Class<? extends ServerRealTask> taskClass : tasks.value()) {
                // add the factory to the multibinding
                Type type = com.google.inject.util.Types.newParameterizedType(ServerTaskFactory.class, taskClass);
                TypeLiteral<ServerTaskFactory<?>> typeLiteral = (TypeLiteral<ServerTaskFactory<?>>) TypeLiteral.get(type);
                taskFactoryBindings.addBinding().to(typeLiteral);
                // provide a factory impl using assisted inject
                install(new FactoryModuleBuilder().build(typeLiteral));
            }
        }
    }
}
