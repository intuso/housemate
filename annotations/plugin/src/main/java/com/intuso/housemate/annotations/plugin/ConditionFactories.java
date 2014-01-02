package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.plugin.api.ServerConditionFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the condition factories that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConditionFactories {

    /**
     * The list of the condition factories the plugin provides
     * @return the list of the condition factories the plugin provides
     */
    Class<? extends ServerConditionFactory<?>>[] value();
}
