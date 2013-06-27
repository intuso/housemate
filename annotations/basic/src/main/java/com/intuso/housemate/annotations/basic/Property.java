package com.intuso.housemate.annotations.basic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a field with this to create a property for your object
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Property {

    /**
     * The property's id
     * @return the property's id
     */
    String id();

    /**
     * The property's name
     * @return the property's name
     */
    String name();

    /**
     * The property's description
     * @return the property's description
     */
    String description();

    /**
     * The property's type
     * @return the property's type
     */
    String typeId();
}
