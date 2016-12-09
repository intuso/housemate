package com.intuso.housemate.client.real.api.internal.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the types that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Types {

    /**
     * The list of the types the plugin provides
     * @return the list of the types the plugin provides
     */
    Class<?>[] value();
}
