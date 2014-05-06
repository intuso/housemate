package com.intuso.housemate.sample.plugin;

import com.google.inject.Inject;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Simple class to demonstrate how to use custom arguments required by plugin components
 */
public class CustomArg {

    public final static String PROP_KEY = "com.me.prop";

    @Inject
    public CustomArg(PropertyRepository properties) {
        properties.get(PROP_KEY); // gets the command line value if set, otherwise the config file value if set,
                                  // otherwise the default configured in the module
    }
}
