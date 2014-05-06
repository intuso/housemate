package com.intuso.housemate.plugin.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the transformers that a plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Transformers {

    /**
     * The list of classes of the transformers that the plugin provides
     * @return the list of classes of the transformers that the plugin provides
     */
    Class<? extends Transformer<?, ?>>[] value();
}
