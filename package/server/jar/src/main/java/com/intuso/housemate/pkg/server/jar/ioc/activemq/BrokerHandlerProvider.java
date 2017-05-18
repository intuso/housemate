package com.intuso.housemate.pkg.server.jar.ioc.activemq;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.apache.activemq.broker.BrokerService;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.util.List;

/**
 * Created by tomc on 21/04/16.
 */
public class BrokerHandlerProvider implements Provider<WebAppContext> {

    public final static String PATH = "broker.web-console.webapp";

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(PATH, "./activemq/webconsole");
    }

    private final File warFile;

    @Inject
    public BrokerHandlerProvider(PropertyRepository properties,
                                 BrokerService brokerService /* bind this so it gets created before the web app */) {
        BrokerServiceProvider.brokerService = brokerService;
        this.warFile = new File(properties.get(PATH));
    }

    @Override
    public WebAppContext get() {
        WebAppContext context = new WebAppContext();
        context.setContextPath("/broker");
        context.setWar(warFile.getAbsolutePath());
        context.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        context.addBean(new ServletContainerInitializersStarter(context), true);
        return context;
    }

    /**
     * Ensure the jsp engine is initialized correctly
     */
    private List<ContainerInitializer> jspInitializers() {
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = Lists.newArrayList();
        initializers.add(initializer);
        return initializers;
    }
}
