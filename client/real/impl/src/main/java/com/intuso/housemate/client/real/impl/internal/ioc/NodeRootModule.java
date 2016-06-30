package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.client.real.api.internal.RealNode;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealNodeRoot;
import com.intuso.housemate.client.real.impl.internal.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.real.impl.internal.type.ioc.RealTypesModule;
import com.intuso.housemate.client.real.impl.internal.utils.ioc.RealUtilsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class NodeRootModule extends AbstractModule {

    private final String nodeId;

    public NodeRootModule(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    protected void configure() {

        // install other required modules
        install(new RealAnnotationsModule());
        install(new RealObjectsModule());
        install(new RealTypesModule());
        install(new RealUtilsModule());

        bind(RealNode.class).to(RealNodeRoot.class);
        bind(RealNodeRoot.class).in(Scopes.SINGLETON);

        bind(RealHardware.Container.class).to(RealNodeRoot.class);

        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(RealNodeRoot.Service.class);
    }

    @Provides
    @Node
    public String getNodeId() {
        return nodeId;
    }

    @Provides
    @Node
    public Logger getRootLogger() {
        return LoggerFactory.getLogger("com.intuso.housemate.objects");
    }

    @Provides
    @Types
    public Logger getTypesLogger(@Node Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "types");
    }
}
