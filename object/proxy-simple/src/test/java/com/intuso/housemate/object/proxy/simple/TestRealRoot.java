package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealObject;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.factory.automation.AddAutomationCommand;
import com.intuso.housemate.object.real.factory.condition.ConditionFactoryType;
import com.intuso.housemate.object.real.factory.device.AddDeviceCommand;
import com.intuso.housemate.object.real.factory.device.DeviceFactoryType;
import com.intuso.housemate.object.real.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.object.real.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.object.real.factory.task.TaskFactoryType;
import com.intuso.housemate.object.real.factory.user.AddUserCommand;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.junit.Ignore;

/**
 */
@Ignore
public class TestRealRoot extends RealRoot {

    @Inject
    public TestRealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router,
                        RealList<TypeData<?>, RealType<?, ?, ?>> types,
                        AddHardwareCommand.Factory addHardwareCommandFactory, AddDeviceCommand.Factory addDeviceCommandFactory,
                        AddAutomationCommand.Factory addAutomationCommandFactory, AddUserCommand.Factory addUserCommandFactory,
                        ConditionFactoryType conditionFactoryType, DeviceFactoryType deviceFactoryType,
                        HardwareFactoryType hardwareFactoryType, TaskFactoryType taskFactoryType) {
        super(log, listenersFactory, properties, router, types,
                addHardwareCommandFactory, addDeviceCommandFactory, addAutomationCommandFactory, addUserCommandFactory,
                conditionFactoryType, deviceFactoryType, hardwareFactoryType, taskFactoryType);
        try {
            distributeMessage(new Message<>(new String[] {""}, Root.SERVER_CONNECTION_STATUS_TYPE, ServerConnectionStatus.ConnectedToServer));
            distributeMessage(new Message<>(new String[] {""}, Root.APPLICATION_STATUS_TYPE, ApplicationStatus.AllowInstances));
            distributeMessage(new Message<>(new String[] {""}, Root.APPLICATION_INSTANCE_STATUS_TYPE, ApplicationInstanceStatus.Allowed));
        } catch (HousemateException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        // do nothing
    }

    public void init() {
        addType(new StringType(getLog(), getListenersFactory()));
        addType(new IntegerType(getLog(), getListenersFactory()));
        addType(new BooleanType(getLog(), getListenersFactory()));
    }

    public void addWrapper(RealObject<?, ?, ?, ?> wrapper) {
        removeChild(wrapper.getId());
        super.addChild(wrapper);
        wrapper.init(this);
    }
}
