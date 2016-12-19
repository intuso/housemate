package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.impl.bridge.ioc.RealBridgeModule;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealServerImpl;
import com.intuso.housemate.client.real.impl.internal.annotation.ioc.AnnotationParserInternalModule;
import com.intuso.housemate.client.real.impl.internal.type.ioc.RealTypesModule;
import com.intuso.housemate.client.real.impl.internal.utils.ioc.RealUtilsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ServerRootModule extends AbstractModule {

    @Override
    protected void configure() {

        // everything to do with internal objects
        install(new AnnotationParserInternalModule());
        install(new RealObjectsModule());
        install(new RealTypesModule());
        install(new RealUtilsModule());

        // bridge versioned client to internal
        install(new RealBridgeModule());

        // server bindings
        bind(RealServer.class).to(RealServerImpl.class);
        bind(RealServerImpl.class).in(Scopes.SINGLETON);

        bind(RealAutomation.Container.class).to(RealServerImpl.class);
        bind(RealDevice.Container.class).to(RealServerImpl.class);
        bind(RealUser.Container.class).to(RealServerImpl.class);
        bind(RealNode.Container.class).to(RealServerImpl.class);

        // bind the server service
        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(RealServerImpl.Service.class);
    }

    @Provides
    @Server
    public Logger getServerLogger() {
        return LoggerFactory.getLogger("com.intuso.housemate.objects.real");
    }

    @Provides
    @Types
    public Logger getTypesLogger(@Server Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "nodes", "local", "types");
    }
}
