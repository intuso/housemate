package com.intuso.housemate.client.real.api.internal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a field with this to create a property for your object
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FeatureId {

    /**
     * The property's id
     * @return the property's id
     */
    String value();
}
