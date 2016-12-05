package com.intuso.housemate.client.api.bridge.v1_0.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.api.bridge.v1_0.*;

/**
 * Created by tomc on 29/09/15.
 */
public class ObjectApiV1_0BridgeModule extends AbstractModule {
    @Override
    protected void configure() {

        // data mappers
        bind(AutomationMapper.class).in(Scopes.SINGLETON);
        bind(CommandMapper.class).in(Scopes.SINGLETON);
        bind(ConditionMapper.class).in(Scopes.SINGLETON);
        bind(DeviceMapper.class).in(Scopes.SINGLETON);
        bind(FeatureMapper.class).in(Scopes.SINGLETON);
        bind(HardwareMapper.class).in(Scopes.SINGLETON);
        bind(ListMapper.class).in(Scopes.SINGLETON);
        bind(NodeMapper.class).in(Scopes.SINGLETON);
        bind(OptionMapper.class).in(Scopes.SINGLETON);
        bind(ParameterMapper.class).in(Scopes.SINGLETON);
        bind(PropertyMapper.class).in(Scopes.SINGLETON);
        bind(ServerMapper.class).in(Scopes.SINGLETON);
        bind(SubTypeMapper.class).in(Scopes.SINGLETON);
        bind(TaskMapper.class).in(Scopes.SINGLETON);
        bind(TypeMapper.class).in(Scopes.SINGLETON);
        bind(UserMapper.class).in(Scopes.SINGLETON);
        bind(ValueMapper.class).in(Scopes.SINGLETON);

        // type instance(s)(map) mappers
        bind(TypeInstanceMapMapper.class).to(TypeInstanceMapMapper.Impl.class);
        bind(TypeInstanceMapMapper.Impl.class).in(Scopes.SINGLETON);
        bind(TypeInstanceMapper.class).to(TypeInstanceMapper.Impl.class);
        bind(TypeInstanceMapper.Impl.class).in(Scopes.SINGLETON);
        bind(TypeInstancesMapper.class).to(TypeInstancesMapper.Impl.class);
        bind(TypeInstancesMapper.Impl.class).in(Scopes.SINGLETON);
    }
}
