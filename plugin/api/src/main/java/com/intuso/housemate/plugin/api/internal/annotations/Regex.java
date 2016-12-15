package com.intuso.housemate.plugin.api.internal.annotations;

import com.intuso.housemate.plugin.api.internal.type.RegexType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate an interface method with this to create a value for your object
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Regex {
    String regex();
    Class<? extends RegexType.Factory<?>> factory();
}
