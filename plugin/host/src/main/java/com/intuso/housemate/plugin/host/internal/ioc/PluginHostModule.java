package com.intuso.housemate.plugin.host.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.plugin.host.internal.PluginHost;

/**
 * Created by tomc on 18/03/15.
 */
public class PluginHostModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PluginHost.class).in(Scopes.SINGLETON);
    }
}
