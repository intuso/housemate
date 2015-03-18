package com.intuso.housemate.realclient.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.impl.type.*;
import com.intuso.housemate.realclient.factory.DeviceFactory;
import com.intuso.housemate.realclient.factory.HardwareFactory;
import com.intuso.housemate.realclient.object.RealClientRoot;

/**
 * Created by tomc on 18/03/15.
 */
public class RealClientModule extends AbstractModule {
    @Override
    protected void configure() {
        // common types
        bind(BooleanType.class).in(Scopes.SINGLETON);
        bind(DaysType.class).in(Scopes.SINGLETON);
        bind(DoubleType.class).in(Scopes.SINGLETON);
        bind(EmailType.class).in(Scopes.SINGLETON);
        bind(IntegerType.class).in(Scopes.SINGLETON);
        bind(RealObjectType.class).in(Scopes.SINGLETON);
        bind(StringType.class).in(Scopes.SINGLETON);
        bind(TimeType.class).in(Scopes.SINGLETON);
        bind(TimeUnitType.class).in(Scopes.SINGLETON);

        // root
        bind(RealClientRoot.class).in(Scopes.SINGLETON);
        bind(RealRoot.class).to(RealClientRoot.class).in(Scopes.SINGLETON);

        // factories
        bind(HardwareFactory.class).in(Scopes.SINGLETON);
        bind(DeviceFactory.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public RealList<TypeData<?>, RealType<?, ?, ?>> getRealTypes(RealClientRoot root) {
        return root.getTypes();
    }

    @Provides
    @Singleton
    public RealList<HardwareData, RealHardware> getRealHardware(RealClientRoot root) {
        return root.getHardwares();
    }

    @Provides
    @Singleton
    public RealList<DeviceData, RealDevice> getRealDevices(RealClientRoot root) {
        return root.getDevices();
    }
}
