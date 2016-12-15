package com.intuso.housemate.plugin.main.ioc;

import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.housemate.client.real.api.internal.annotations.Id;
import com.intuso.housemate.client.real.api.internal.module.AnnotatedPluginModule;
import com.intuso.housemate.client.real.api.internal.module.ConditionDrivers;
import com.intuso.housemate.client.real.api.internal.module.FeatureDrivers;
import com.intuso.housemate.client.real.api.internal.module.TaskDrivers;
import com.intuso.housemate.client.real.impl.internal.type.RealObjectType;
import com.intuso.housemate.plugin.main.condition.*;
import com.intuso.housemate.plugin.main.feature.PowerByCommandFeature;
import com.intuso.housemate.plugin.main.task.Delay;
import com.intuso.housemate.plugin.main.task.RandomDelay;

/**
 */
@Id(value = "main-plugin",
        name = "Main plugin",
        description = "Plugin containing the core types and factories")
@FeatureDrivers({PowerByCommandFeature.class})
@ConditionDrivers({And.class,
        Or.class,
        Not.class,
        DayOfTheWeek.class,
        TimeOfTheDay.class})
@TaskDrivers({Delay.class,
        RandomDelay.class})
public class MainPluginModule extends AnnotatedPluginModule {

    @Override
    public void configure() {

        super.configure();

        // bind implementations
        bind(new TypeLiteral<TypeSerialiser<RealObjectType.Reference<?>>>() {}).to(new Key<RealObjectType.Serialiser<?>>() {}).in(Scopes.SINGLETON);
    }
}
