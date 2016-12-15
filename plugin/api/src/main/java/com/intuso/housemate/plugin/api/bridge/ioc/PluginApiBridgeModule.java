package com.intuso.housemate.plugin.api.bridge.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.plugin.api.bridge.v1_0.ioc.PluginApiBridgeV1_0Module;

/**
 * Created by tomc on 15/12/16.
 */
public class PluginApiBridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new PluginApiBridgeV1_0Module());
    }
}
