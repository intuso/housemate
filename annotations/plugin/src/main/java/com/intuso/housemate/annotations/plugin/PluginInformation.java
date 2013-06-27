package com.intuso.housemate.annotations.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to provide the information about a plugin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginInformation {

    /**
     * The id of the plugin
     * @return the id of the plugin
     */
    String id();

    /**
     * The name of the plugin
     * @return the name of the plugin
     */
    String name();

    /**
     * The description of the plugin
     * @return the description of the plugin
     */
    String description();

    /**
     * The author of the plugin
     * @return the author of the plugin
     */
    String author();
}
