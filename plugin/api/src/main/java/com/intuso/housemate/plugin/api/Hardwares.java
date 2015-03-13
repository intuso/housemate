package com.intuso.housemate.plugin.api;

import com.intuso.housemate.object.real.RealHardware;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the device factories that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Hardwares {

    /**
     * The list of the device factories the plugin provides
     * @return the list of the device factories the plugin provides
     */
    Class<? extends RealHardware>[] value();
}
