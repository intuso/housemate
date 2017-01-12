package com.intuso.housemate.client.api.internal.type.serialiser.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.api.internal.type.serialiser.SystemTypeSerialisersRepository;
import com.intuso.housemate.client.api.internal.type.serialiser.TypeSerialiser;

/**
 * Created by tomc on 12/01/17.
 */
public class SystemTypeSerialisersRepositoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TypeSerialiser.Repository.class).to(SystemTypeSerialisersRepository.class);
        bind(SystemTypeSerialisersRepository.class).in(Scopes.SINGLETON);
    }
}
