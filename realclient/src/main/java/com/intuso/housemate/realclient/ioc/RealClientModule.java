package com.intuso.housemate.realclient.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.object.real.ioc.RealObjectModule;
import com.intuso.housemate.realclient.factory.FactoryPluginListener;

/**
 * Created by tomc on 20/03/15.
 */
public class RealClientModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new RealObjectModule());

        bind(FactoryPluginListener.class).in(Scopes.SINGLETON);
    }
}
