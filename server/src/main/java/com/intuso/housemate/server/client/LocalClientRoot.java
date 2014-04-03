package com.intuso.housemate.server.client;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.server.Server;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Version of a root object for server-internal objects
 */
public class LocalClientRoot extends RealRoot {

    private final RealCommand addDeviceCommand;

    @Inject
    public LocalClientRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router,
                           RealList<TypeData<?>, RealType<?, ?, ?>> types,
                           RealList<DeviceData, RealDevice> devices, LifecycleHandler lifecycleHandler) {
        super(log, listenersFactory, Server.createApplicationInstanceProperties(listenersFactory, properties), router, types, devices);
        addDeviceCommand = lifecycleHandler.createAddDeviceCommand(getDevices());
        addChild(addDeviceCommand);
    }

    /**
     * Get the add device command for this client
     * @return
     */
    public RealCommand getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        if(message.getPayload() instanceof HousemateData)
            super.messageReceived(new Message(message.getPath(), message.getType(),
                    ((HousemateData)message.getPayload()).deepClone(),
                    message.getRoute()));
        else
            super.messageReceived(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(message.getPayload() instanceof HousemateData)
            super.sendMessage(new Message(message.getPath(), message.getType(),
                    ((HousemateData)message.getPayload()).deepClone(),
                    message.getRoute()));
        else
            super.sendMessage(message);
    }
}
