package com.intuso.housemate.client.api.internal.annotation;

import com.intuso.housemate.client.v1_0.api.annotation.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with this to create a command for your object. All arguments for the command
 * must have {@link Parameter} annotations
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HardwareAPI {
    String value();
}
