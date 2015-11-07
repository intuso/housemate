package com.intuso.housemate.server.plugin.main.ioc;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tomc on 06/11/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface Operator {
}
