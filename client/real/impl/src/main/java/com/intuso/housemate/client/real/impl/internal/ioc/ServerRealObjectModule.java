package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.impl.internal.RealServerImpl;
import com.intuso.housemate.client.real.impl.internal.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.real.impl.internal.factory.ioc.RealFactoryModule;

/**
 */
public class ServerRealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealFactoryModule());
        install(new RealAnnotationsModule());
        install(new RealTypesModule());

        bind(RealServer.class).to(RealServerImpl.class);
        bind(RealServerImpl.class).in(Scopes.SINGLETON);

        bind(RealAutomation.Container.class).to(RealServerImpl.class);
        bind(RealDevice.Container.class).to(RealServerImpl.class);
        bind(RealUser.Container.class).to(RealServerImpl.class);
        bind(RealNode.Container.class).to(RealServerImpl.class);
    }
}
