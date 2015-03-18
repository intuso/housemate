package com.intuso.housemate.platform.pc.ioc;

import com.google.inject.AbstractModule;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/01/14
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class PCClientModule extends AbstractModule {

    private final WriteableMapPropertyRepository defaultProperties;
    private final PropertyRepository properties;
    private final String logName;

    public PCClientModule(WriteableMapPropertyRepository defaultProperties, PropertyRepository properties, String logName) {
        this.defaultProperties = defaultProperties;
        this.properties = properties;
        this.logName = logName;
    }

    @Override
    protected void configure() {
        bind(PropertyRepository.class).toInstance(properties);
        install(new PCModule(defaultProperties, logName));
    }
}
