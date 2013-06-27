package com.intuso.housemate.annotations.basic;

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
     * The parameter's id
     * @return the parameter's id
     */
    String id();

    /**
     * The parameter's name
     * @return the parameter's name
     */
    String name();

    /**
     * The parameter's description
     * @return the parameter's description
     */
    String description();

    /**
     * The parameter's type
     * @return the parameter's type
     */
    String typeId();
}
