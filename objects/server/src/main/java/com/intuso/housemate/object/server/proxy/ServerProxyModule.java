package com.intuso.housemate.object.server.proxy;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.value.Value;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class ServerProxyModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(Command.class, ServerProxyCommand.class)
                .build(ServerProxyFactory.Command.class));
        install(new FactoryModuleBuilder().implement(Device.class, ServerProxyDevice.class)
                .build(ServerProxyFactory.Device.class));
        install(new FactoryModuleBuilder().implement(List.class, ServerProxyList.class)
                .build(ServerProxyFactory.GenericList.class));
        install(new FactoryModuleBuilder().implement(Option.class, ServerProxyOption.class)
                .build(ServerProxyFactory.Option.class));
        install(new FactoryModuleBuilder().implement(Parameter.class, ServerProxyParameter.class)
                .build(ServerProxyFactory.Parameter.class));
        install(new FactoryModuleBuilder().implement(Property.class, ServerProxyProperty.class)
                .build(ServerProxyFactory.Property.class));
        install(new FactoryModuleBuilder().implement(SubType.class, ServerProxySubType.class)
                .build(ServerProxyFactory.SubType.class));
        install(new FactoryModuleBuilder().implement(Type.class, ServerProxyType.class)
                .build(ServerProxyFactory.Type.class));
        install(new FactoryModuleBuilder().implement(Value.class, ServerProxyValue.class)
                .build(ServerProxyFactory.Value.class));
        bind(new TypeLiteral<HousemateObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>>() {}).to(ServerProxyFactory.All.class);
    }
}
