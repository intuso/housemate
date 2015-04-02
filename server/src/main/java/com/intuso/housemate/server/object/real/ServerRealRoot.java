package com.intuso.housemate.server.object.real;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.RealList;
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
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;

/**
 * Created by tomc on 20/03/15.
 */
public class ServerRealRoot extends RealRoot {

    private boolean initialDataSent = false;

    @Inject
    public ServerRealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router,
                          RealList<TypeData<?>, RealType<?, ?, ?>> types,
                          AddHardwareCommand.Factory addHardwareCommandFactory, AddDeviceCommand.Factory addDeviceCommandFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory, AddUserCommand.Factory addUserCommandFactory,
                          ConditionFactoryType conditionFactoryType, DeviceFactoryType deviceFactoryType,
                          HardwareFactoryType hardwareFactoryType, TaskFactoryType taskFactoryType) {
        super(log, listenersFactory, properties, router, types,
                addHardwareCommandFactory, addDeviceCommandFactory, addAutomationCommandFactory, addUserCommandFactory,
                conditionFactoryType, deviceFactoryType, hardwareFactoryType, taskFactoryType);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(SEND_INITIAL_DATA, new Receiver<NoPayload>() {
            @Override
            public void messageReceived(Message<NoPayload> message) throws HousemateException {
                initialDataSent = true;
            }
        }));
        return result;
    }

    @Override
    public void register(ApplicationDetails applicationDetails, String component) {
        super.register(applicationDetails, component);
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(initialDataSent || message.getPayload() instanceof ApplicationRegistration || message.getType().equals(INITIAL_DATA)) {
            if(message.getPayload() instanceof HousemateData)
                ((Message)message).setPayload(((HousemateData<?>) message.getPayload()).deepClone());
            super.sendMessage(message);
        }
    }

    @Override
    public boolean checkCanSendMessage(Message<?> message) {
        return true;
    }
}
