package com.intuso.housemate.client.api.internal.plugin;

import com.intuso.housemate.client.api.internal.annotation.Id;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to describe a composite type
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CompositeType {
    Id id();
    Class<?> type();
}
