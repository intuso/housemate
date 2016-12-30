package com.intuso.housemate.pkg.node.v1_0.jar.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.platform.pc.ioc.PCModule;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created by tomc on 30/12/16.
 */
public class NodePackageJarModule extends AbstractModule {

    private final WriteableMapPropertyRepository defaultProperties;
    private final PropertyRepository properties;

    public NodePackageJarModule(WriteableMapPropertyRepository defaultProperties, PropertyRepository properties) {
        this.defaultProperties = defaultProperties;
        this.properties = properties;
    }

    @Override
    protected void configure() {
        bind(PropertyRepository.class).toInstance(properties);
        install(new PCModule()); // log and properties provider
        install(new NodeModule(defaultProperties)); // main server module
    }
}
