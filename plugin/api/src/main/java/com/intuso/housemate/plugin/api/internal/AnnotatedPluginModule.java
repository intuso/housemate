package com.intuso.housemate.plugin.api.internal;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Base class for all plugins that wish to use annotations to describe the provided features
 */
public abstract class AnnotatedPluginModule extends AbstractModule {

    public void configure() {
        // configure the classloader to be this plugin's class' classloader
        bind(ClassLoader.class).toInstance(getClass().getClassLoader());
    }

    @Provides
    @Singleton
    public TypeInfo getTypeInfo() {
        return getClass().getAnnotation(TypeInfo.class);
    }

    @Provides
    @Singleton
    public Iterable<? extends RealType<?>> getTypes(Injector injector) {
        Types types = getClass().getAnnotation(Types.class);
        if(types == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(types.value()))
                .transform(this.<RealType<?>>makeInstance(injector))
                .toList();
    }

    @Provides
    @Singleton
    public Iterable<PluginResource<? extends Comparator<?>>> getComparators(Injector injector) {
        Comparators comparators = getClass().getAnnotation(Comparators.class);
        if(comparators == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(comparators.value()))
                .transform(this.<Comparator<?>>asResource(injector))
                .toList();
    }

    @Provides
    @Singleton
    public Iterable<PluginResource<? extends Operator<?, ?>>> getOperators(Injector injector) {
        Operators operators = getClass().getAnnotation(Operators.class);
        if(operators == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(operators.value()))
                .transform(this.<Operator<?, ?>>asResource(injector))
                .toList();
    }

    @Provides
    @Singleton
    public Iterable<? extends Transformer<?, ?>> getTransformers(Injector injector) {
        Transformers transformers = getClass().getAnnotation(Transformers.class);
        if(transformers == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(transformers.value()))
                .transform(this.<Transformer<?, ?>>makeInstance(injector))
                .toList();
    }

    @Provides
    @Singleton
    public Iterable<PluginResource<? extends ConditionDriver.Factory<?>>> getConditionFactories(Injector injector) {
        ConditionDrivers conditionDrivers = getClass().getAnnotation(ConditionDrivers.class);
        if(conditionDrivers == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(conditionDrivers.value()))
                .transform(this.<ConditionDriver, ConditionDriver.Factory, ConditionDriver.Factory<?>>asFactoryResource(ConditionDriver.class, ConditionDriver.Factory.class, injector))
                .toList();
    }

    @Provides
    @Singleton
    public Iterable<PluginResource<? extends DeviceDriver.Factory<?>>> getDeviceFactories(Injector injector) {
        DeviceDrivers deviceDrivers = getClass().getAnnotation(DeviceDrivers.class);
        if(deviceDrivers == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(deviceDrivers.value()))
                .transform(this.<DeviceDriver, DeviceDriver.Factory, DeviceDriver.Factory<?>>asFactoryResource(DeviceDriver.class, DeviceDriver.Factory.class, injector))
                .toList();
    }

    @Provides
    @Singleton
    public Iterable<PluginResource<? extends HardwareDriver.Factory<?>>> getHardwareFactories(Injector injector) {
        HardwareDrivers hardwareDrivers = getClass().getAnnotation(HardwareDrivers.class);
        if(hardwareDrivers == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(hardwareDrivers.value()))
                .transform(this.<HardwareDriver, HardwareDriver.Factory, HardwareDriver.Factory<?>>asFactoryResource(HardwareDriver.class, HardwareDriver.Factory.class, injector))
                .toList();
    }

    @Provides
    @Singleton
    public Iterable<PluginResource<? extends TaskDriver.Factory<?>>> getTaskFactories(Injector injector) {
        TaskDrivers taskDrivers = getClass().getAnnotation(TaskDrivers.class);
        if(taskDrivers == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(taskDrivers.value()))
                .transform(this.<TaskDriver, TaskDriver.Factory, TaskDriver.Factory<?>>asFactoryResource(TaskDriver.class, TaskDriver.Factory.class, injector))
                .toList();
    }

    private <O> Function<Class<? extends O>, O> makeInstance(final Injector injector) {
        return new Function<Class<? extends O>, O>() {
            @Override
            public O apply(Class<? extends O> aClass) {
                return injector.getInstance(aClass);
            }
        };
    }

    private <O> Function<Class<? extends O>, PluginResource<? extends O>> asResource(final Injector injector) {
        return new Function<Class<? extends O>, PluginResource<? extends O>>() {
            @Override
            public PluginResource<? extends O> apply(Class<? extends O> resourceInstanceClass) {
                return new PluginResourceImpl<>(
                        getClassAnnotation(resourceInstanceClass, TypeInfo.class),
                        injector.getInstance(resourceInstanceClass));
            }
        };
    }

    private <RESOURCE, FACTORY_RAW, FACTORY_GENERIC> Function<Class<? extends RESOURCE>, PluginResource<? extends FACTORY_GENERIC>>
    asFactoryResource(final Class<RESOURCE> resourceClass, final Class<FACTORY_RAW> factoryClass, final Injector injector) {
        return new Function<Class<? extends RESOURCE>, PluginResource<? extends FACTORY_GENERIC>>() {
            @Override
            public PluginResource<? extends FACTORY_GENERIC> apply(Class<? extends RESOURCE> resourceInstanceClass) {

                // setup the generic type/typeliteral etc
                Type type = com.google.inject.util.Types.newParameterizedTypeWithOwner(resourceClass, factoryClass, resourceInstanceClass);
                TypeLiteral<FACTORY_GENERIC> typeLiteral = (TypeLiteral<FACTORY_GENERIC>) TypeLiteral.get(type);

                return new PluginResourceImpl<>(
                        getClassAnnotation(resourceInstanceClass, TypeInfo.class),
                        injector.createChildInjector(new FactoryModuleBuilder().build(typeLiteral)).getInstance(Key.get(typeLiteral)));
            }
        };
    }

    private <ANNOTATION extends Annotation> ANNOTATION getClassAnnotation(Class<?> annotatedClass, Class<ANNOTATION> annotationClass) {
        ANNOTATION result = annotatedClass.getAnnotation(annotationClass);
        if(result != null)
            return result;
        if(annotatedClass.getSuperclass() != null) {
            result = getClassAnnotation(annotatedClass.getSuperclass(), annotationClass);
            if(result != null)
                return result;
        }
        for(Class<?> implementedInterface : annotatedClass.getInterfaces()) {
            result = getClassAnnotation(implementedInterface, annotationClass);
            if(result != null)
                return result;
        }
        return null;
    }
}
