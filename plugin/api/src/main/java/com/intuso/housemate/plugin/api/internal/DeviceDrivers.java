package com.intuso.housemate.plugin.api.internal;

import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to list the device factories that the plugin provides
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DeviceDrivers {

    /**
     * The list of the device factories the plugin provides
     * @return the list of the device factories the plugin provides
     */
    Class<? extends DeviceDriver>[] value();
}
