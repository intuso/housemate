package com.intuso.housemate.client.real.api.internal.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TypeInfo {
    String id();
    String name();
    String description();
}
