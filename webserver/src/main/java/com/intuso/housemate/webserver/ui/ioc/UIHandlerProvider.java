package com.intuso.housemate.webserver.ui.ioc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;

/**
 * Created by tomc on 18/05/17.
 */
public class UIHandlerProvider implements Provider<ContextHandler> {public final static String HOUSEMATE_CONFIG_DIR = "conf.dir";

    public final static String UI_ROOT = "web.ui.directory";

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(UI_ROOT, defaultProperties.get(HOUSEMATE_CONFIG_DIR) + File.separator + "ui");
    }

    private final PropertyRepository properties;

    @Inject
    public UIHandlerProvider(PropertyRepository properties) {
        this.properties = properties;
    }

    @Override
    public ContextHandler get() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(Resource.newResource(new File(properties.get(UI_ROOT))));
        ContextHandler contextHandler = new ContextHandler("/");
        contextHandler.setHandler(resourceHandler);
        return contextHandler;
    }
}
