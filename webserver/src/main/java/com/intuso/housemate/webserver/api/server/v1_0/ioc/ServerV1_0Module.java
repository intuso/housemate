package com.intuso.housemate.webserver.api.server.v1_0.ioc;

import com.google.inject.Scopes;
import com.intuso.housemate.webserver.api.server.v1_0.Listeners;
import com.intuso.housemate.webserver.api.server.v1_0.SessionIdInjector;
import com.intuso.utilities.webserver.ioc.JerseyResourcesModule;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.BroadcasterLifeCyclePolicy;
import org.atmosphere.guice.AtmosphereGuiceServlet;

import java.util.HashMap;

/**
 * Created by tomc on 21/01/17.
 */
public class ServerV1_0Module extends JerseyResourcesModule {

    public ServerV1_0Module() {
        super("/server/1.0/", ServerV1_0ResourceConfig.class);
    }

    @Override
    protected void configureServlets() {

        // bind all servlets/filters as singletons
        bind(AtmosphereServlet.class).in(Scopes.SINGLETON);
        bind(SessionIdInjector.class).in(Scopes.SINGLETON);

        bind(Listeners.class).in(Scopes.SINGLETON);

        filter("/server/1.0/listen").through(SessionIdInjector.class);
        serve("/server/1.0/listen").with(AtmosphereGuiceServlet.class, new HashMap<String, String>() {{
            put(ApplicationConfig.ANNOTATION_PACKAGE, "com.intuso.housemate.webserver.api.server.v1_0");
            put(ApplicationConfig.PROPERTY_NATIVE_COMETSUPPORT, "true");
            put(ApplicationConfig.WEBSOCKET_CONTENT_TYPE, "application/json");
            put(ApplicationConfig.ATMOSPHERERESOURCE_INTERCEPTOR_TIMEOUT, "60");
            put(ApplicationConfig.BROADCASTER_LIFECYCLE_POLICY, BroadcasterLifeCyclePolicy.NEVER.getLifeCyclePolicy().name());
        }});

        super.configureServlets();
    }
}
