package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.plugin.api.RealDeviceFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DeviceFactories {
    Class<? extends RealDeviceFactory<?>>[] value();
}
