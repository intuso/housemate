package com.intuso.housemate.webserver.ioc;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.webserver.LoadUserFilter;
import com.intuso.housemate.webserver.api.ioc.ApiModule;
import com.intuso.housemate.webserver.database.Database;
import com.intuso.housemate.webserver.ui.ioc.UIModule;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import com.intuso.utilities.webserver.config.HttpPortConfig;
import com.intuso.utilities.webserver.ioc.SessionCookie;
import com.intuso.utilities.webserver.ioc.WebServerModule;
import com.intuso.utilities.webserver.oauth.OAuthStore;

import javax.servlet.Filter;

/**
 * Created by tomc on 21/01/17.
 */
public class HousemateWebServerModule extends ServletModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        UIModule.configureDefaults(defaultProperties);
    }

    private final int port;
    private final String cookieName;
    private final Class<? extends Filter> serverFilter;

    public HousemateWebServerModule(int port, String cookieName, Class<? extends Filter> serverFilter) {
        this.port = port;
        this.cookieName = cookieName;
        this.serverFilter = serverFilter;
    }

    @Override
    protected void configureServlets() {

        // setup the webserver - ports, unsecured endpoints etc
        install(new WebServerModule.Builder()
                .portConfigs(new HttpPortConfig("0.0.0.0", port))
                .loginPage("/login/index.html")
                .nextParam("next")
                // oauth resources
                .unsecuredResource("POST", "/api/oauth/1.0/token")
                // login resources
                .unsecuredResource("GET", "/login/index.html")
                .unsecuredResource("GET", "/js/login.bundle.js")
                .unsecuredResource("POST", "/api/server/1.0/login")
                // register resources
                .unsecuredResource("GET", "/register/index.html")
                .unsecuredResource("GET", "/js/register.bundle.js")
                .unsecuredResource("POST", "/api/server/1.0/register")
                .build());

        bind(String.class).annotatedWith(SessionCookie.class).toInstance(cookieName);

        // pass every request through the load user filter to get the user from the oauth user id
        bind(LoadUserFilter.class).in(Scopes.SINGLETON);
        filter("/*").through(LoadUserFilter.class);

        // pass every request through the server filter to set the server in the session
        bind(serverFilter).in(Scopes.SINGLETON);
        filter("/*").through(serverFilter);

        // install the apis
        install(new ApiModule());

        // add the ui resources
        install(new UIModule());

        // bind the oauth store to the database
        bind(OAuthStore.class).to(Database.class);
    }
}
