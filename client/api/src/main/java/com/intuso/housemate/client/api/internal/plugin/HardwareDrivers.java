package com.intuso.housemate.client.api.internal.plugin;

import com.intuso.housemate.client.api.internal.driver.HardwareDriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the hardware factories that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HardwareDrivers {

    /**
     * The list of the hardware factories the plugin provides
     * @return the list of the hardware factories the plugin provides
     */
    Class<? extends HardwareDriver>[] value();
}
