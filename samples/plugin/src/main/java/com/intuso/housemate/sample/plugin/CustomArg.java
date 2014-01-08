package com.intuso.housemate.sample.plugin;

import com.google.inject.Inject;
import com.intuso.utilities.properties.api.PropertyContainer;

/**
 * Simple class to demonstrate how to use custom arguments required by plugin components
 */
public class CustomArg {

    public final static String PROP_KEY = "com.me.prop";

    @Inject
    public CustomArg(PropertyContainer properties) {
        properties.get("com.me.prop"); // gets the command line value if set, otherwise the config file value if set,
                                       // otherwise the default configured in the module
    }
}
