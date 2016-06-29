package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.intuso.housemate.client.real.api.internal.RealHardware;
import com.intuso.housemate.client.real.api.internal.RealNode;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealNodeImpl;
import com.intuso.housemate.client.real.impl.internal.annotations.ioc.RealAnnotationsModule;
import com.intuso.housemate.client.real.impl.internal.type.ioc.RealTypesModule;
import com.intuso.housemate.client.real.impl.internal.utils.ioc.RealUtilsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class NodeRealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        // install other required modules
        install(new RealAnnotationsModule());
        install(new RealObjectsModule());
        install(new RealTypesModule());
        install(new RealUtilsModule());

        bind(RealNode.class).to(RealNodeImpl.class);
        bind(RealNodeImpl.class).in(Scopes.SINGLETON);

        bind(RealHardware.Container.class).to(RealNodeImpl.class);
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
