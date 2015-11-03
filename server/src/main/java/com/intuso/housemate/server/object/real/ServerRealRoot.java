package com.intuso.housemate.server.object.real;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.RealListImpl;
import com.intuso.housemate.client.real.impl.internal.RealRootImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.factory.automation.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.real.impl.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.user.AddUserCommand;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Created by tomc on 20/03/15.
 */
public class ServerRealRoot extends RealRootImpl {

    @Inject
    public ServerRealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router<?> router,
                          RealListImpl<TypeData<?>, RealTypeImpl<?, ?, ?>> types,
                          AddHardwareCommand.Factory addHardwareCommandFactory, AddDeviceCommand.Factory addDeviceCommandFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory, AddUserCommand.Factory addUserCommandFactory,
                          ConditionFactoryType conditionFactoryType, DeviceFactoryType deviceFactoryType,
                          HardwareFactoryType hardwareFactoryType, TaskFactoryType taskFactoryType) {
        super(log, listenersFactory, properties, router, types,
                addHardwareCommandFactory, addDeviceCommandFactory, addAutomationCommandFactory, addUserCommandFactory,
                conditionFactoryType, deviceFactoryType, hardwareFactoryType, taskFactoryType);
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(message.getPayload() instanceof HousemateData)
            ((Message)message).setPayload(((HousemateData<?>) message.getPayload()).deepClone());
        super.sendMessage(message);
    }

    @Override
    public boolean checkCanSendMessage(Message<?> message) {
        return true;
    }
}
