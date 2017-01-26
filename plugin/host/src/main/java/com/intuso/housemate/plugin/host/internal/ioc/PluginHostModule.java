package com.intuso.housemate.plugin.host.internal.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.api.internal.plugin.PluginListener;
import com.intuso.housemate.plugin.host.internal.PluginHost;

/**
 * Created by tomc on 18/03/15.
 */
public class PluginHostModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), PluginListener.class);
        Multibinder.newSetBinder(binder(), com.intuso.housemate.client.v1_0.api.plugin.PluginListener.class);
        bind(PluginHost.class).in(Scopes.SINGLETON);

        // bind the ervice
        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(PluginHost.class).in(Scopes.SINGLETON);
    }
}
