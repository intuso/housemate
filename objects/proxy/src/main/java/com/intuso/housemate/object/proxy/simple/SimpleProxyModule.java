package com.intuso.housemate.object.proxy.simple;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.object.proxy.ProxyObject;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProxyModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(Automation.class, SimpleProxyObject.Automation.class)
                .build(SimpleProxyFactory.Automation.class));
        install(new FactoryModuleBuilder().implement(Command.class, SimpleProxyObject.Command.class)
                .build(SimpleProxyFactory.Command.class));
        install(new FactoryModuleBuilder().implement(Condition.class, SimpleProxyObject.Condition.class)
                .build(SimpleProxyFactory.Condition.class));
        install(new FactoryModuleBuilder().implement(Device.class, SimpleProxyObject.Device.class)
                .build(SimpleProxyFactory.Device.class));
        install(new FactoryModuleBuilder().implement(List.class, SimpleProxyObject.List.class)
                .build(SimpleProxyFactory.GenericList.class));
        install(new FactoryModuleBuilder().implement(Option.class, SimpleProxyObject.Option.class)
                .build(SimpleProxyFactory.Option.class));
        install(new FactoryModuleBuilder().implement(Parameter.class, SimpleProxyObject.Parameter.class)
                .build(SimpleProxyFactory.Parameter.class));
        install(new FactoryModuleBuilder().implement(Property.class, SimpleProxyObject.Property.class)
                .build(SimpleProxyFactory.Property.class));
        install(new FactoryModuleBuilder().implement(SubType.class, SimpleProxyObject.SubType.class)
                .build(SimpleProxyFactory.SubType.class));
        install(new FactoryModuleBuilder().implement(Task.class, SimpleProxyObject.Task.class)
                .build(SimpleProxyFactory.Task.class));
        install(new FactoryModuleBuilder().implement(Type.class, SimpleProxyObject.Type.class)
                .build(SimpleProxyFactory.Type.class));
        install(new FactoryModuleBuilder().implement(User.class, SimpleProxyObject.User.class)
                .build(SimpleProxyFactory.User.class));
        install(new FactoryModuleBuilder().implement(Value.class, SimpleProxyObject.Value.class)
                .build(SimpleProxyFactory.Value.class));
        bind(new TypeLiteral<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).to(SimpleProxyFactory.All.class);
    }
}
