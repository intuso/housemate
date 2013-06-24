package com.intuso.housemate.annotations.plugin;

import com.intuso.housemate.object.real.RealDevice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Devices {
    Class<? extends RealDevice>[] value();
}
