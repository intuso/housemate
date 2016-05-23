package com.intuso.housemate.server.plugin.main.ioc;

import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.housemate.client.real.impl.internal.type.RealObjectType;
import com.intuso.housemate.plugin.api.internal.annotations.TypeInfo;
import com.intuso.housemate.plugin.api.internal.module.AnnotatedPluginModule;
import com.intuso.housemate.plugin.api.internal.module.ConditionDrivers;
import com.intuso.housemate.plugin.api.internal.module.DeviceDrivers;
import com.intuso.housemate.plugin.api.internal.module.TaskDrivers;
import com.intuso.housemate.server.plugin.main.condition.*;
import com.intuso.housemate.server.plugin.main.device.PowerByCommandDevice;
import com.intuso.housemate.server.plugin.main.task.Delay;
import com.intuso.housemate.server.plugin.main.task.RandomDelay;

/**
 */
@TypeInfo(id = "main-plugin",
        name = "Main plugin",
        description = "Plugin containing the core types and factories")
@DeviceDrivers({PowerByCommandDevice.class})
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
