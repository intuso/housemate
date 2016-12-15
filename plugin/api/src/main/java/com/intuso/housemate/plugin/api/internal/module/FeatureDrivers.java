package com.intuso.housemate.plugin.api.internal.module;

import com.intuso.housemate.plugin.api.internal.driver.FeatureDriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the feature factories that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FeatureDrivers {

    /**
     * The list of the feature factories the plugin provides
     * @return the list of the feature factories the plugin provides
     */
    Class<? extends FeatureDriver>[] value();
}
