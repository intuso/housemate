package com.intuso.housemate.plugin.api.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the comparators that a plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Comparators {

    /**
     * The list of classes of the comparators that the plugin provides
     * @return the list of classes of the comparators that the plugin provides
     */
    Class<? extends Comparator<?>>[] value();
}
