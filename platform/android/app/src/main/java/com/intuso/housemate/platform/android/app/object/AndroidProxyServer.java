package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyServer;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

//import javax.jms.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyServer extends ProxyServer<AndroidProxyCommand,
        AndroidProxyValue,
        AndroidProxyList<AndroidProxyValue>,
        ProxyDevice<?, ?, ?, ?, ?, ?>,
        AndroidProxyList<AndroidProxyAutomation>,
        AndroidProxyList<AndroidProxyDeviceGroup>,
        AndroidProxyList<AndroidProxyUser>,
        AndroidProxyList<AndroidProxyNode>,
        AndroidProxyServer> {

    private final AndroidObjectFactories factories;

    /**
     * @param logger    {@inheritDoc}
     */
    public AndroidProxyServer(Logger logger, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, Sender.Factory senderFactory) {
        super(logger, managedCollectionFactory, receiverFactory);
        factories = new AndroidObjectFactories(managedCollectionFactory, receiverFactory, senderFactory, this);
    }

    @Override
    protected AndroidProxyCommand createCommand(Logger logger) {
        return factories.command().create(logger);
    }

    @Override
    protected AndroidProxyList<AndroidProxyValue> createValues(Logger logger) {
        return factories.values().create(logger);
    }

    @Override
    protected AndroidProxyList<AndroidProxyAutomation> createAutomations(Logger logger) {
        return factories.automations().create(logger);
    }

    @Override
    protected AndroidProxyList<AndroidProxyDeviceGroup> createDeviceGroups(Logger logger) {
        return factories.deviceGroups().create(logger);
    }

    @Override
    protected AndroidProxyList<AndroidProxyUser> createUsers(Logger logger) {
        return factories.users().create(logger);
    }

    @Override
    protected AndroidProxyList<AndroidProxyNode> createNodes(Logger logger) {
        return factories.nodes().create(logger);
    }
}
