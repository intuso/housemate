package com.intuso.housemate.client.real.impl.internal.annotation.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParser;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParserImpl;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParserInternal;
import com.intuso.housemate.client.real.impl.internal.annotation.AnnotationParserV1_0;

/**
 * Created by tomc on 20/03/15.
 */
public class AnnotationParserInternalModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AnnotationParser.class).to(AnnotationParserImpl.class);
        bind(AnnotationParserInternal.class).in(Scopes.SINGLETON);
        bind(AnnotationParserV1_0.class).in(Scopes.SINGLETON);
   }
}
