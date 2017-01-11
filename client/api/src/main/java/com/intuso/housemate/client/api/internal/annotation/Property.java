package com.intuso.housemate.client.api.internal.annotation;

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
     * The property's type
     * @return the property's type
     */
    String restriction() default "";

    int minValues() default 0;

    int maxValues() default -1;
}
