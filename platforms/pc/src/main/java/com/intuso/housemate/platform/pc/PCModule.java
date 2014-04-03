package com.intuso.housemate.platform.pc;

import com.google.inject.AbstractModule;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class PCModule extends AbstractModule {

    private final WriteableMapPropertyRepository defaultProperties;
    private final String defaultLogName;

    public PCModule(WriteableMapPropertyRepository defaultProperties, String defaultLogName) {
        this.defaultProperties = defaultProperties;
        this.defaultLogName = defaultLogName;
    }

    @Override
    protected void configure() {
        install(new PCLogModule(defaultProperties, defaultLogName));
        install(new PCRegexMatcherModule());
    }
}
