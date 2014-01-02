package com.intuso.housemate.server.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.real.*;

/**
 * Version of a root object for server-internal objects
 */
@Singleton
public class LocalClientRoot extends RealRootObject {

    private final RealCommand addDeviceCommand;

    @Inject
    public LocalClientRoot(RealResources resources, RealList<TypeData<?>, RealType<?, ?, ?>> types,
                final LifecycleHandler lifecycleHandler) {
        super(resources, types, new RealList<DeviceData, RealDevice>(resources, Root.DEVICES_ID, "Devices", "Devices"));
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
