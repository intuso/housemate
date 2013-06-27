package com.intuso.housemate.annotations.basic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with this to create a command for your object. All arguments for the command
 * must have {@link Parameter} annotations
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * The command's id
     * @return the command's id
     */
    String id();

    /**
     * The command's name
     * @return the command's name
     */
    String name();

    /**
     * The command's description
     * @return the command's description
     */
    String description();
}
