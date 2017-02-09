package com.intuso.housemate.client.real.impl.internal.type.ioc;

import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.api.internal.plugin.PluginListener;
import com.intuso.housemate.client.api.internal.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.real.impl.internal.ioc.Type;
import com.intuso.housemate.client.real.impl.internal.type.*;
import org.slf4j.Logger;

/**
 * Created by tomc on 13/05/16.
 */
public class RealTypesModule extends AbstractModule {

    @Override
    protected void configure() {

        // other type factories
        install(new FactoryModuleBuilder().build(EnumChoiceType.Factory.class));
        install(new FactoryModuleBuilder().build(RealCompositeType.Factory.class));
        install(new FactoryModuleBuilder().build(RealRegexType.Factory.class));

        // bind singletons
        bind(ConditionDriverType.class).in(Scopes.SINGLETON);
        bind(HardwareDriverType.class).in(Scopes.SINGLETON);
        bind(TaskDriverType.class).in(Scopes.SINGLETON);
        bind(TypeRepository.class).in(Scopes.SINGLETON);
        bind(TypeSerialisersV1_0Repository.class).in(Scopes.SINGLETON);

        // bind implementations
        bind(TypeSerialiser.Repository.class).to(TypeRepository.class);
        bind(com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser.Repository.class).to(TypeSerialisersV1_0Repository.class);

        // bind types plugin listener NB this needs to be bound before the hardware detector so the driver is already available
        bind(TypesInternalPluginsListener.class).in(Scopes.SINGLETON);
        Multibinder.newSetBinder(binder(), PluginListener.class).addBinding().to(TypesInternalPluginsListener.class);
        bind(TypesV1_0PluginsListener.class).in(Scopes.SINGLETON);
        Multibinder.newSetBinder(binder(), com.intuso.housemate.client.v1_0.api.plugin.PluginListener.class).addBinding().to(TypesV1_0PluginsListener.class);
    }

    @Provides
    @com.intuso.housemate.client.v1_0.real.impl.ioc.Type
    public Logger getTypeV1_0Logger(@Type Logger logger) {
        return logger;
    }
}
