package com.intuso.housemate.client.real.impl.internal.type.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.util.Types;
import com.intuso.housemate.client.real.impl.internal.type.*;
import com.intuso.housemate.plugin.api.internal.driver.*;
import com.intuso.housemate.plugin.api.internal.type.Email;

/**
 * Created by tomc on 13/05/16.
 */
public class RealTypesModule extends AbstractModule {

    @Override
    protected void configure() {

        // standard java types
        install(new TypeModule(BooleanType.class, Boolean.class));
        install(new TypeModule(DoubleType.class, Double.class));
        install(new TypeModule(IntegerType.class, Integer.class));
        install(new TypeModule(StringType.class, String.class));

        // other common types defined by us
        install(new TypeModule(EmailType.class, Email.class));

        // driver factory types
        install(new TypeModule(ConditionDriverType.class, Types.newParameterizedType(PluginDependency.class, Types.newParameterizedTypeWithOwner(ConditionDriver.class, ConditionDriver.Factory.class, Types.subtypeOf(ConditionDriver.class)))));
        install(new TypeModule(DeviceDriverType.class, Types.newParameterizedType(PluginDependency.class, Types.newParameterizedTypeWithOwner(DeviceDriver.class, DeviceDriver.Factory.class, Types.subtypeOf(DeviceDriver.class)))));
        install(new TypeModule(HardwareDriverType.class, Types.newParameterizedType(PluginDependency.class, Types.newParameterizedTypeWithOwner(HardwareDriver.class, HardwareDriver.Factory.class, Types.subtypeOf(HardwareDriver.class)))));
        install(new TypeModule(TaskDriverType.class, Types.newParameterizedType(PluginDependency.class, Types.newParameterizedTypeWithOwner(TaskDriver.class, TaskDriver.Factory.class, Types.subtypeOf(TaskDriver.class)))));

        // other type factories
        install(new FactoryModuleBuilder().build(EnumChoiceType.Factory.class));
        install(new FactoryModuleBuilder().build(RealCompositeType.Factory.class));
        install(new FactoryModuleBuilder().build(RealRegexType.Factory.class));

        bind(ConditionDriverType.class).in(Scopes.SINGLETON);
        bind(DeviceDriverType.class).in(Scopes.SINGLETON);
        bind(HardwareDriverType.class).in(Scopes.SINGLETON);
        bind(TaskDriverType.class).in(Scopes.SINGLETON);
        bind(RegisteredTypes.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<Iterable<TypeFactories<?>>>() {}).toProvider(SystemTypeFactoriesProvider.class);
    }
}
