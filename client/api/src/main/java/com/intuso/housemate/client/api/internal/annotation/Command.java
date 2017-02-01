package com.intuso.housemate.client.api.internal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with this to create a command for your object. All arguments for the method must have {@link Id}
 * annotations and optionally a {@link Parameter} annotation to use restrictions or control min and max num values
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {}
