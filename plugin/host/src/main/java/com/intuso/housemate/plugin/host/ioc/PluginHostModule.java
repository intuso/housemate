package com.intuso.housemate.plugin.host.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.plugin.host.PluginManager;

/**
 * Created by tomc on 18/03/15.
 */
public class PluginHostModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PluginManager.class).in(Scopes.SINGLETON);
    }
}
