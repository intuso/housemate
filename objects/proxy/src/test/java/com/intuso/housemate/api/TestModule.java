package com.intuso.housemate.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.intuso.housemate.api.object.TestProxyRoot;
import com.intuso.housemate.api.object.TestRealRoot;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.real.RealRootObject;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.writer.StdOutWriter;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 06/01/14
 * Time: 08:26
 * To change this template use File | Settings | File Templates.
 */
public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PropertyContainer.class).in(Scopes.SINGLETON);
        bind(RealRootObject.class).to(TestRealRoot.class);
        bind(ProxyRootObject.class).to(TestProxyRoot.class);
    }

    @Provides
    @Singleton
    public Log getLog() {
        Log log = new Log();
        log.addWriter(new StdOutWriter(LogLevel.DEBUG));
        return log;
    }
}
