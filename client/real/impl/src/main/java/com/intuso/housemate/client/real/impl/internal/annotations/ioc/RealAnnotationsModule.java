package com.intuso.housemate.client.real.impl.internal.annotations.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.impl.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.impl.internal.annotations.FieldProperty;
import com.intuso.housemate.client.real.impl.internal.annotations.MethodCommand;
import com.intuso.housemate.client.real.impl.internal.annotations.MethodProperty;

/**
 * Created by tomc on 20/03/15.
 */
public class RealAnnotationsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AnnotationProcessor.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(MethodCommand.Factory.class));
        install(new FactoryModuleBuilder().build(FieldProperty.Factory.class));
        install(new FactoryModuleBuilder().build(MethodProperty.Factory.class));
    }
}
