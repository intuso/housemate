package com.intuso.housemate.plugin.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.object.real.RealType;

/**
 * Descriptor of what features a plugin provides
 */
public abstract class PluginModule extends AbstractModule {

    public static String MANIFEST_ATTRIBUTE = "Housemate-Plugin";

    public void configure() {

        // configure all the things a plugin can provide
        configureTypes(Multibinder.newSetBinder(binder(), new TypeLiteral<RealType<?, ?, ?>>() {}));
        configureComparators(Multibinder.newSetBinder(binder(), new TypeLiteral<Comparator<?>>() {}));
        configureOperators(Multibinder.newSetBinder(binder(), new TypeLiteral<Operator<?, ?>>() {
        }));
        configureTransformers(Multibinder.newSetBinder(binder(), new TypeLiteral<Transformer<?, ?>>() {
        }));
        configureDeviceFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<RealDeviceFactory<?>>() {}));
        configureConditionFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<ServerConditionFactory<?>>() {}));
        configureTaskFactories(Multibinder.newSetBinder(binder(), new TypeLiteral<ServerTaskFactory<?>>() {}));
    }

    @Provides
    @Singleton
    public abstract TypeInfo getTypeInfo();

    public void configureTypes(Multibinder<RealType<?, ?, ?>> typeBindings) {}

    public void configureComparators(Multibinder<Comparator<?>> comparatorBindings) {}

    public void configureOperators(Multibinder<Operator<?, ?>> operatorBindings) {}

    public void configureTransformers(Multibinder<Transformer<?, ?>> transformerBindings) {}

    public void configureDeviceFactories(Multibinder<RealDeviceFactory<?>> deviceFactoryBindings) {}

    public void configureConditionFactories(Multibinder<ServerConditionFactory<?>> conditionFactoryBindings) {}

    public void configureTaskFactories(Multibinder<ServerTaskFactory<?>> taskFactoryBindings) {}
}
