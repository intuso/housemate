package com.intuso.housemate.client.api.internal.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the composite types that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CompositeTypes {

    /**
     * The list of the composite types the plugin provides
     * @return the list of the composite types the plugin provides
     */
    Class<?>[] value();
}
