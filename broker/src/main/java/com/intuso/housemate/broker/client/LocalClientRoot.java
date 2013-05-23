package com.intuso.housemate.broker.client;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealRootObject;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 23/05/13
 * Time: 08:49
 * To change this template use File | Settings | File Templates.
 */
public class LocalClientRoot extends RealRootObject {

    private final RealCommand addDeviceCommand;

    public LocalClientRoot(BrokerGeneralResources resources) {
        super(resources.getClientResources());
        addDeviceCommand = resources.getLifecycleHandler().createAddDeviceCommand(getDevices());
        addWrapper(addDeviceCommand);
    }

    public RealCommand getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        if(message.getPayload() instanceof HousemateObjectWrappable)
            super.messageReceived(new Message(message.getPath(), message.getType(),
                    ((HousemateObjectWrappable)message.getPayload()).deepClone(),
                    message.getRoute()));
        else
            super.messageReceived(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(message.getPayload() instanceof HousemateObjectWrappable)
            super.sendMessage(new Message(message.getPath(), message.getType(),
                    ((HousemateObjectWrappable)message.getPayload()).deepClone(),
                    message.getRoute()));
        else
            super.sendMessage(message);
    }
}
