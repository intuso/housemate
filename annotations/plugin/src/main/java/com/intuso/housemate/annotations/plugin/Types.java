package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.object.real.RealType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the types that a plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Types {

    /**
     * The list of classes of the types that the plugin provides
     * @return the list of classes of the types that the plugin provides
     */
    Class<? extends RealType<?, ?, ?>>[] value();
}
