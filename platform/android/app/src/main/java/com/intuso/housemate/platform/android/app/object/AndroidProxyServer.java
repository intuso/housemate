package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
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
        ProxyDevice<?, ?, ?, ?, ?, ?>,
        AndroidProxyList<AndroidProxyValue>,
        AndroidProxyList<AndroidProxyAutomation>,
        AndroidProxyList<AndroidProxyDeviceCombi>,
        AndroidProxyList<AndroidProxyUser>,
        AndroidProxyList<AndroidProxyNode>,
        AndroidProxyServer> {

    /**
     * @param logger    {@inheritDoc}
     */
    public AndroidProxyServer(Logger logger, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, receiverFactory, factories.command(), factories.values(), factories.automations(), factories.deviceCombis(), factories.users(), factories.nodes());
    }
}
