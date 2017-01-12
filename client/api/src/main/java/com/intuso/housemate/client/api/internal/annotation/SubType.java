package com.intuso.housemate.client.api.internal.annotation;

import com.intuso.housemate.client.v1_0.api.annotation.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a type's field as a sub type
 *
 * @see Command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SubType {

    /**
     * The sub type's type
     * @return the sub type's type
     */
    String restriction() default "";

    int minValues() default -1;

    int maxValues() default -1;
}
