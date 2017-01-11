package com.intuso.housemate.client.api.internal.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the double range types that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DoubleRangeTypes {

    /**
     * The list of the double range types the plugin provides
     * @return the list of the double range types the plugin provides
     */
    DoubleRangeType[] value();

}
