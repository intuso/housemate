package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.plugin.api.Operator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the operators that a plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Operators {

    /**
     * The list of classes of the operators that the plugin provides
     * @return the list of classes of the operators that the plugin provides
     */
    Class<? extends Operator<?, ?>>[] value();
}
