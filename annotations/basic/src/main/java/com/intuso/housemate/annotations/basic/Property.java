package com.intuso.housemate.annotations.basic;

import com.intuso.housemate.object.real.RealType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Property {
    String id();
    String name();
    String description();
    Class<? extends RealType<?, ?, ?>> type();
}
