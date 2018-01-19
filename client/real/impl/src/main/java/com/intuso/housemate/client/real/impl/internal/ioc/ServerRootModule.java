package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.api.internal.object.Automation;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.User;
import com.intuso.housemate.client.real.api.internal.RealServer;
import com.intuso.housemate.client.real.impl.bridge.ioc.RealBridgeModule;
import com.intuso.housemate.client.real.impl.internal.*;
import com.intuso.housemate.client.real.impl.internal.annotation.ioc.AnnotationParserInternalModule;
import com.intuso.housemate.client.real.impl.internal.type.ioc.RealTypesModule;
import com.intuso.housemate.client.real.impl.internal.utils.ioc.RealUtilsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.intuso.housemate.client.api.internal.object.Server.NODES_ID;

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

        bind(Automation.Container.class).to(RealServerImpl.class);
        bind(Device.Group.Container.class).to(RealServerImpl.class);
        bind(User.Container.class).to(RealServerImpl.class);
        bind(Node.Container.class).to(RealServerImpl.class);

        // bind hardware detector NB this comes after the types listener binding so the driver should be available already
        bind(HardwareDetectorInternalPluginListener.class).in(Scopes.SINGLETON);
        bind(HardwareDetectorV1_0PluginListener.class).in(Scopes.SINGLETON);

        // bind the server service
        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(RealServerImpl.Service.class);
    }

    @Provides
    @Server
    public Logger getServerLogger() {
        return ChildUtil.logger(LoggerFactory.getLogger("server"));
    }

    @Provides
    @Type
    public Logger getTypesLogger(@Server Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "nodes", "local", "types");
    }

    @Provides
    @Singleton
    public RealNodeImpl getLocalNode(@Server Logger logger,RealNodeImpl.Factory nodeFactory) {
        return nodeFactory.create(ChildUtil.logger(logger, NODES_ID, "local"), "local", "Local", "Local Node");
    }
}
