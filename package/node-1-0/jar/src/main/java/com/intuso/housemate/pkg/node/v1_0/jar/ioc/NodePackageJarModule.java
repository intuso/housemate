package com.intuso.housemate.pkg.node.v1_0.jar.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.client.v1_0.messaging.jms.ioc.JMSMessagingModule;
import com.intuso.housemate.platform.pc.ioc.PCClientModule;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created by tomc on 30/12/16.
 */
public class NodePackageJarModule extends AbstractModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        PCClientModule.configureDefaults(defaultProperties);
    }

    private final PropertyRepository properties;

    public NodePackageJarModule(PropertyRepository properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {
        install(new PCClientModule(properties)); // log and properties provider
        install(new JMSMessagingModule.Javabin());
        install(new NodeModule()); // main server module
    }
}
