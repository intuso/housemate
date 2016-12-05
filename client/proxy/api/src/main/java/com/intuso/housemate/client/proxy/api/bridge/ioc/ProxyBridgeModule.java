package com.intuso.housemate.client.proxy.api.bridge.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.intuso.housemate.client.proxy.api.bridge.v1_0.ioc.ProxyBridgeV1_0Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 05/12/16.
 */
public class ProxyBridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ProxyBridgeV1_0Module());
    }

    @Provides
    @Proxy
    public Logger getServerLogger() {
        return LoggerFactory.getLogger("com.intuso.housemate.objects.proxy");
    }
}
