package com.intuso.housemate.client.real.impl.internal.type.ioc;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.client.api.internal.driver.*;
import com.intuso.housemate.client.api.internal.type.Email;
import com.intuso.housemate.client.real.impl.internal.type.TypeFactories;

/**
 * Created by tomc on 23/05/16.
 */
public class SystemTypeFactoriesProvider implements Provider<Iterable<TypeFactories<?>>> {

    // standard java types
    private final TypeFactories<Boolean> booleanTypeFactories;
    private final TypeFactories<Double> doubleTypeFactories;
    private final TypeFactories<Integer> integerTypeFactories;
    private final TypeFactories<String> stringTypeFactories;

    // other common types defined by us
    private final TypeFactories<Email> emailTypeFactories;

    // driver factory types
    private final TypeFactories<PluginDependency<ConditionDriver.Factory<?>>> conditionDriverTypeFactories;
    private final TypeFactories<PluginDependency<FeatureDriver.Factory<?>>> deviceDriverTypeFactories;
    private final TypeFactories<PluginDependency<HardwareDriver.Factory<?>>> hardwareDriverTypeFactories;
    private final TypeFactories<PluginDependency<TaskDriver.Factory<?>>> taskDriverTypeFactories;

    @Inject
    public SystemTypeFactoriesProvider(TypeFactories<Boolean> booleanTypeFactories,
                                       TypeFactories<Double> doubleTypeFactories,
                                       TypeFactories<Integer> integerTypeFactories,
                                       TypeFactories<String> stringTypeFactories,
                                       TypeFactories<Email> emailTypeFactories,
                                       TypeFactories<PluginDependency<ConditionDriver.Factory<? extends ConditionDriver>>> conditionDriverTypeFactories,
                                       TypeFactories<PluginDependency<FeatureDriver.Factory<? extends FeatureDriver>>> deviceDriverTypeFactories,
                                       TypeFactories<PluginDependency<HardwareDriver.Factory<? extends HardwareDriver>>> hardwareDriverTypeFactories,
                                       TypeFactories<PluginDependency<TaskDriver.Factory<? extends TaskDriver>>> taskDriverTypeFactories) {
        this.booleanTypeFactories = booleanTypeFactories;
        this.doubleTypeFactories = doubleTypeFactories;
        this.integerTypeFactories = integerTypeFactories;
        this.stringTypeFactories = stringTypeFactories;
        this.emailTypeFactories = emailTypeFactories;
        this.conditionDriverTypeFactories = conditionDriverTypeFactories;
        this.deviceDriverTypeFactories = deviceDriverTypeFactories;
        this.hardwareDriverTypeFactories = hardwareDriverTypeFactories;
        this.taskDriverTypeFactories = taskDriverTypeFactories;
    }

    @Override
    public Iterable<TypeFactories<?>> get() {
        return Lists.newArrayList(
                booleanTypeFactories,
                doubleTypeFactories,
                integerTypeFactories,
                stringTypeFactories,

                emailTypeFactories,

                conditionDriverTypeFactories,
                deviceDriverTypeFactories,
                hardwareDriverTypeFactories,
                taskDriverTypeFactories
        );
    }
}
