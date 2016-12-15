package com.intuso.housemate.client.real.api.internal.module;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.api.internal.annotations.Id;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.driver.FeatureDriver;
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
    public Id getTypeInfo() {
        return getClass().getAnnotation(Id.class);
    }

    @Provides
    @Singleton
    public Iterable<PluginResource<Class<?>>> getTypes(Injector injector) {
        Types types = getClass().getAnnotation(Types.class);
        if(types == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(types.value()))
                .transform(asResource(injector))
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
    public Iterable<PluginResource<? extends FeatureDriver.Factory<?>>> getDeviceFactories(Injector injector) {
        FeatureDrivers featureDrivers = getClass().getAnnotation(FeatureDrivers.class);
        if(featureDrivers == null)
            return Lists.newArrayList();
        return FluentIterable.from(Lists.newArrayList(featureDrivers.value()))
                .transform(this.<FeatureDriver, FeatureDriver.Factory, FeatureDriver.Factory<?>>asFactoryResource(FeatureDriver.class, FeatureDriver.Factory.class, injector))
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

    private Function<Class<?>, PluginResource<Class<?>>> asResource(final Injector injector) {
        return new Function<Class<?>, PluginResource<Class<?>>>() {
            @Override
            public PluginResource<Class<?>> apply(Class<?> resourceClass) {
                return new PluginResourceImpl<Class<?>>(
                        getClassAnnotation(resourceClass, Id.class),
                        resourceClass);
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
                        getClassAnnotation(resourceInstanceClass, Id.class),
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
