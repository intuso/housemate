package com.intuso.housemate.persistence.api.bridge.v1_0.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.persistence.api.bridge.v1_0.PersistenceV1_0ReverseBridge;
import com.intuso.housemate.persistence.api.internal.Persistence;

/**
 * Created by tomc on 25/09/15.
 */
public class PersistenceV1_0ReverseBridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Persistence.class).to(PersistenceV1_0ReverseBridge.class);
    }
}
