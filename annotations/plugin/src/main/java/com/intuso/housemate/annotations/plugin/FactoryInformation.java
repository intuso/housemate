package com.intuso.housemate.annotations.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Plugin to provide the information for the generated factory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FactoryInformation {

    /**
     * The id of the factory
     * @return the id of the factory
     */
    String id();

    /**
     * The name of the factory
     * @return the name of the factory
     */
    String name();

    /**
     * The description of the factory
     * @return the description of the factory
     */
    String description();
}
