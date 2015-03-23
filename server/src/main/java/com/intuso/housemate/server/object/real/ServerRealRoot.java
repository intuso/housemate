package com.intuso.housemate.server.object.real;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.access.ApplicationRegistration;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.factory.automation.AddAutomationCommand;
import com.intuso.housemate.object.real.factory.device.AddDeviceCommand;
import com.intuso.housemate.object.real.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.object.real.factory.user.AddUserCommand;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;

/**
 * Created by tomc on 20/03/15.
 */
public class ServerRealRoot extends RealRoot {

    private boolean isRegistered = false;
    private final List<Message<?>> queue = Lists.newArrayList();

    @Inject
    public ServerRealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router, RealList<TypeData<?>, RealType<?, ?, ?>> types, AddHardwareCommand.Factory addHardwareCommandFactory, AddDeviceCommand.Factory addDeviceCommandFactory, AddAutomationCommand.Factory addAutomationCommandFactory, AddUserCommand.Factory addUserCommandFactory) {
        super(log, listenersFactory, properties, router, types, addHardwareCommandFactory, addDeviceCommandFactory, addAutomationCommandFactory, addUserCommandFactory);
        addObjectListener(new RootListener<RealRoot>() {
            @Override
            public void serverConnectionStatusChanged(RealRoot root, ServerConnectionStatus serverConnectionStatus) {

            }

            @Override
            public void applicationStatusChanged(RealRoot root, ApplicationStatus applicationStatus) {

            }

            @Override
            public void applicationInstanceStatusChanged(RealRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
                if(!isRegistered && applicationInstanceStatus == ApplicationInstanceStatus.Allowed) {
                    isRegistered = true;
                    for(Message<?> message : queue)
                        sendMessage(message);
                    queue.clear();
                }
            }

            @Override
            public void newApplicationInstance(RealRoot root, String instanceId) {

            }

            @Override
            public void newServerInstance(RealRoot root, String serverId) {

            }
        });
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        super.register(applicationDetails);
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(isRegistered || message.getPayload() instanceof ApplicationRegistration)
            super.sendMessage(message);
        else
            queue.add(message);
    }

    @Override
    public boolean checkCanSendMessage(Message<?> message) {
        return true;
    }
}
