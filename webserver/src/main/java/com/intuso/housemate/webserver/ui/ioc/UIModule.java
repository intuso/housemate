package com.intuso.housemate.webserver.ui.ioc;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.webserver.ui.UIServlet;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created by tomc on 08/03/17.
 */
public class UIModule extends ServletModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        UIServlet.configureDefaults(defaultProperties);
    }

    @Override
    protected void configureServlets() {
        super.configureServlets();
        serve("/*").with(UIServlet.class);
        bind(UIServlet.class).in(Scopes.SINGLETON);
    }
}
