package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 13/05/16.
 */
public class LoggersModule extends AbstractModule {
    @Override
    protected void configure() {}

    @Provides
    @Root
    public Logger getRootLogger() {
        return LoggerFactory.getLogger("com.intuso.housemate.objects");
    }

    @Provides
    @Types
    public Logger getTypesLogger(@Root Logger rootLogger) {
        return ChildUtil.logger(rootLogger, "types");
    }
}
