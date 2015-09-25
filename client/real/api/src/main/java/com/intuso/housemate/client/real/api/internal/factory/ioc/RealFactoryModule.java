package com.intuso.housemate.client.real.api.internal.factory.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.intuso.housemate.client.real.api.internal.factory.automation.AddAutomationCommand;
import com.intuso.housemate.client.real.api.internal.factory.automation.RealAutomationFactory;
import com.intuso.housemate.client.real.api.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.api.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.real.api.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.real.api.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.task.AddTaskCommand;
import com.intuso.housemate.client.real.api.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.user.AddUserCommand;
import com.intuso.housemate.client.real.api.internal.factory.user.RealUserFactory;

/**
 * Created by tomc on 20/03/15.
 */
public class RealFactoryModule extends AbstractModule {
    @Override
    protected void configure() {

        // automations
        install(new FactoryModuleBuilder().build(RealAutomationFactory.class));
        install(new FactoryModuleBuilder().build(AddAutomationCommand.Factory.class));

        // conditions
        bind(ConditionFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddConditionCommand.Factory.class));

        // devices
        bind(DeviceFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddDeviceCommand.Factory.class));

        // hardwares
        bind(HardwareFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddHardwareCommand.Factory.class));

        // tasks
        bind(TaskFactoryType.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(AddTaskCommand.Factory.class));

        // users
        install(new FactoryModuleBuilder().build(RealUserFactory.class));
        install(new FactoryModuleBuilder().build(AddUserCommand.Factory.class));
    }
}
