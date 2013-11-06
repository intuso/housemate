package com.intuso.housemate.broker.client;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealRootObject;

/**
 * Version of a root object for broker-internal objects
 */
public class LocalClientRoot extends RealRootObject {

    private final RealCommand addDeviceCommand;

    public LocalClientRoot(BrokerGeneralResources resources) {
        super(resources.getClientResources());
        addDeviceCommand = resources.getLifecycleHandler().createAddDeviceCommand(getDevices());
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
