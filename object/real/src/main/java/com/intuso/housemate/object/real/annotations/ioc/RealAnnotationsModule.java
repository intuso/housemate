package com.intuso.housemate.object.real.annotations.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;

/**
 * Created by tomc on 20/03/15.
 */
public class RealAnnotationsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AnnotationProcessor.class).in(Scopes.SINGLETON);
    }
}
