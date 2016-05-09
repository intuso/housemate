package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;

import java.util.Map;

/**
 * Created by tomc on 04/05/16.
 */
public class RealTypesModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new TypesModule());
        bind(new TypeLiteral<Map<String, RealTypeImpl.Factory<?>>>() {}).toProvider(TypeFactoriesProvider.class);
    }
}
