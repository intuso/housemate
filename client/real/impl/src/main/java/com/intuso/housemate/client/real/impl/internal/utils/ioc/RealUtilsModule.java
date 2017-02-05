package com.intuso.housemate.client.real.impl.internal.utils.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.impl.internal.utils.*;

/**
 * Created by tomc on 19/05/16.
 */
public class RealUtilsModule extends AbstractModule {
    @Override
    protected void configure() {

        // add automation command
        bind(AddAutomationCommand.Factory.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddAutomationCommand.Performer.Factory.class));

        // add condition command
        bind(AddConditionCommand.Factory.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddConditionCommand.Performer.Factory.class));

        // add device command
        bind(AddDeviceCommand.Factory.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddDeviceCommand.Performer.Factory.class));

        // add hardware command
        bind(AddHardwareCommand.Factory.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddHardwareCommand.Performer.Factory.class));

        // add task command
        bind(AddTaskCommand.Factory.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddTaskCommand.Performer.Factory.class));

        // add user command
        bind(AddUserCommand.Factory.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddUserCommand.Performer.Factory.class));
    }
}
