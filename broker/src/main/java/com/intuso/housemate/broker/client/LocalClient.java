package com.intuso.housemate.broker.client;

import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.plugin.PluginDescriptor;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.authentication.AuthenticationMethod;
import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.real.RealRootObject;
import com.intuso.housemate.real.RealType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/01/13
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class LocalClient implements PluginListener {

    private final BrokerGeneralResources resources;
    private final RealRootObject root;

    public LocalClient(final BrokerGeneralResources resources) throws HousemateException {
        this.resources = resources;
        root = new RealRootObject(resources.getClientResources()) {
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
        };
        root.connect(new InternalConnectMethod(), null);
        root.addType(resources.getDeviceFactory().getType());
        root.addType(resources.getConditionFactory().getType());
        root.addType(resources.getConsequenceFactory().getType());
        resources.addPluginListener(LocalClient.this, true);
    }

    public RealRootObject getRoot() {
        return root;
    }

    @Override
    public void pluginAdded(PluginDescriptor plugin) {
        for(RealType<?, ?, ?> type : plugin.getTypes()) {
            resources.getLog().d("Adding type " + type.getId());
            root.addType(type);
        }
    }

    @Override
    public void pluginRemoved(PluginDescriptor plugin) {
        for(RealType<?, ?, ?> type : plugin.getTypes())
            root.removeType(type.getId());
    }

    public final class InternalConnectMethod extends AuthenticationMethod {
        private InternalConnectMethod() {
            super(false);
        }
    }
}
