package com.intuso.housemate.client.api.internal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a command's argument with this to create a parameter for your command
 *
 * @see Command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {

    /**
     * The parameter's type
     * @return the parameter's type
     */
    String value() default "";

    int minValues() default -1;

    int maxValues() default -1;
}
