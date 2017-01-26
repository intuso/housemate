package com.intuso.housemate.platform.pc.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.platform.pc.CopyOnWriteManagedCollectionFactory;
import com.intuso.housemate.platform.pc.PCPluginFileFinder;
import com.intuso.housemate.plugin.host.internal.PluginFileFinder;
import com.intuso.utilities.listener.ManagedCollectionFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class PCModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ManagedCollectionFactory.class).to(CopyOnWriteManagedCollectionFactory.class);
        bind(PluginFileFinder.class).to(PCPluginFileFinder.class);
        bind(PCPluginFileFinder.class).in(Scopes.SINGLETON);
    }
}
