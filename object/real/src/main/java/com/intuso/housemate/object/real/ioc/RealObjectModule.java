package com.intuso.housemate.object.real.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.*;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class RealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // bind everything as singletons that should be
        // root objects
        bind(RealRoot.class).in(Scopes.SINGLETON);;
        // common types
        bind(ApplicationStatusType.class).in(Scopes.SINGLETON);
        bind(ApplicationInstanceStatusType.class).in(Scopes.SINGLETON);
        bind(BooleanType.class).in(Scopes.SINGLETON);
        bind(DaysType.class).in(Scopes.SINGLETON);
        bind(DoubleType.class).in(Scopes.SINGLETON);
        bind(EmailType.class).in(Scopes.SINGLETON);
        bind(IntegerType.class).in(Scopes.SINGLETON);
        bind(StringType.class).in(Scopes.SINGLETON);
        bind(TimeType.class).in(Scopes.SINGLETON);
        bind(TimeUnitType.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    public RealList<TypeData<?>, RealType<?, ?, ?>> getRealTypes(Log log, ListenersFactory listenersFactory) {
        return new RealList<TypeData<?>, RealType<?, ?, ?>>(log, listenersFactory, Root.TYPES_ID, "Types", "Types");
    }

    @Provides
    @Singleton
    public RealList<DeviceData, RealDevice> getRealDevices(Log log, ListenersFactory listenersFactory) {
        return new RealList<DeviceData, RealDevice>(log, listenersFactory, Root.DEVICES_ID, "Devices", "Devices");
    }
}
