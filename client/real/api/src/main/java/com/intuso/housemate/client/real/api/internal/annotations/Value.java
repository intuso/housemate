package com.intuso.housemate.client.real.api.internal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate an interface method with this to create a value for your object
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Value {

    /**
     * The value's id
     * @return the value's id
     */
    String id();

    /**
     * The value's name
     * @return the value's name
     */
    String name();

    /**
     * The value's description
     * @return the value's description
     */
    String description();

    /**
     * The value's type
     * @return the value's type
     */
    String typeId();
}
