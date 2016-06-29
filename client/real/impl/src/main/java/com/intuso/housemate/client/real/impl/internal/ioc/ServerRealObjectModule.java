package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealServerImpl;
import com.intuso.housemate.client.real.impl.internal.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.real.impl.internal.type.ioc.RealTypesModule;
import com.intuso.housemate.client.real.impl.internal.utils.ioc.RealUtilsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class ServerRealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealAnnotationsModule());
        install(new RealObjectsModule());
        install(new RealTypesModule());
        install(new RealUtilsModule());

        bind(RealServer.class).to(RealServerImpl.class);
        bind(RealServerImpl.class).in(Scopes.SINGLETON);

        bind(RealAutomation.Container.class).to(RealServerImpl.class);
        bind(RealDevice.Container.class).to(RealServerImpl.class);
        bind(RealUser.Container.class).to(RealServerImpl.class);
        bind(RealNode.Container.class).to(RealServerImpl.class);
    }

    @Provides
    @Server
    public Logger getServerLogger() {
        return LoggerFactory.getLogger("com.intuso.housemate.objects");
    }

    @Provides
    @Node
    public Logger getNodeLogger(@Server Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "nodes", "local");
    }

    @Provides
    @Types
    public Logger getTypesLogger(@Node Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "types");
    }
}
