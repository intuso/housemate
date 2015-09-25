package com.intuso.housemate.plugin.api.internal;

import com.intuso.housemate.client.real.api.internal.RealCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the condition factories that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Conditions {

    /**
     * The list of the condition factories the plugin provides
     * @return the list of the condition factories the plugin provides
     */
    Class<? extends RealCondition>[] value();
}
