package com.intuso.housemate.object.server.proxy;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.option.OptionFactory;
import com.intuso.housemate.api.object.parameter.ParameterFactory;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.subtype.SubTypeFactory;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.value.ValueFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class ServerProxyObjectModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(new TypeLiteral<CommandFactory<ServerProxyCommand>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<DeviceFactory<ServerProxyDevice>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ListFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<OptionFactory<ServerProxyOption>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ParameterFactory<ServerProxyParameter>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<PropertyFactory<ServerProxyProperty>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<SubTypeFactory<ServerProxySubType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<TypeFactory<ServerProxyType>>() {}));
        install(new FactoryModuleBuilder().build(new TypeLiteral<ValueFactory<ServerProxyValue>>() {}));
        bind(new TypeLiteral<HousemateObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>() {}).to(ServerProxyFactory.All.class);
    }
}
