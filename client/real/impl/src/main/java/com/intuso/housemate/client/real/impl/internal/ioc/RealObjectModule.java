package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.impl.internal.RealListImpl;
import com.intuso.housemate.client.real.impl.internal.RealRootImpl;
import com.intuso.housemate.client.real.impl.internal.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.real.impl.internal.factory.ioc.RealFactoryModule;
import com.intuso.housemate.client.real.impl.internal.type.*;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class RealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealFactoryModule());
        install(new RealAnnotationsModule());

        // bind everything as singletons that should be
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
    public RealList<RealType<?>> getTypes(Log log, ListenersFactory listenersFactory) {
        return (RealList)new RealListImpl<>(log, listenersFactory, RealRootImpl.TYPES_ID, "Types", "Types");
    }
}
