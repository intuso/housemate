package com.intuso.housemate.pkg.server.jar.ioc.activemq;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.apache.activemq.broker.BrokerService;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.util.List;

/**
 * Created by tomc on 21/04/16.
 */
public class BrokerWebAppContextProvider implements Provider<WebAppContext> {

    private final ContextHandlerCollection contextHandlerCollection;
    private final File warFile;
    private final BrokerService brokerService; // bind this so it gets created before the web app

    @Inject
    public BrokerWebAppContextProvider(ContextHandlerCollection contextHandlerCollection, PropertyRepository properties, BrokerService brokerService) {
        BrokerServiceProvider.brokerService = brokerService;
        this.contextHandlerCollection = contextHandlerCollection;
        this.warFile = new File(properties.get("web.activemq-web-console.path"));
        this.brokerService = brokerService;
    }

    @Override
    public WebAppContext get() {
        WebAppContext context = new WebAppContext(contextHandlerCollection, warFile.getAbsolutePath(), "/broker");
        context.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        context.addBean(new ServletContainerInitializersStarter(context), true);
//        context.setClassLoader(new URLClassLoader(new URL[0], BrokerWebAppContextProvider.class.getClassLoader()));
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
