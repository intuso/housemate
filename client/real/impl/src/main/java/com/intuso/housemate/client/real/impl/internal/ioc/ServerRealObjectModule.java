package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.util.Modules;
import com.intuso.housemate.client.real.api.internal.RealApplication;
import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.client.real.api.internal.RealRoot;
import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.client.real.impl.internal.BasicRealRoot;
import com.intuso.housemate.client.real.impl.internal.ServerRealRoot;

/**
 */
public class ServerRealObjectModule extends AbstractModule {

    @Override
    protected void configure() {

        install(Modules.override(new BasicRealObjectModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(RealRoot.class).to(ServerRealRoot.class);
                bind(BasicRealRoot.class).to(ServerRealRoot.class);
            }
        }));

        bind(ServerRealRoot.class).in(Scopes.SINGLETON);

        bind(RealAutomation.Container.class).to(ServerRealRoot.class);
        bind(RealApplication.Container.class).to(ServerRealRoot.class);
        bind(RealUser.Container.class).to(ServerRealRoot.class);
    }
}
