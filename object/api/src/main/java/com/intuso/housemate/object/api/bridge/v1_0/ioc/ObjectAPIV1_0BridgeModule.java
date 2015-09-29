package com.intuso.housemate.object.api.bridge.v1_0.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstanceMapMapper;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstanceMapper;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstancesMapper;

/**
 * Created by tomc on 29/09/15.
 */
public class ObjectAPIV1_0BridgeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TypeInstanceMapMapper.class).to(TypeInstanceMapMapper.Impl.class);
        bind(TypeInstanceMapper.class).to(TypeInstanceMapper.Impl.class);
        bind(TypeInstancesMapper.class).to(TypeInstancesMapper.Impl.class);
    }
}
