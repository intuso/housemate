package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyAutomation;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyAutomation extends ProxyAutomation<AndroidProxyCommand, AndroidProxyValue,
        AndroidProxyList<AndroidProxyCondition>,
        AndroidProxyList<AndroidProxyTask>, AndroidProxyAutomation> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyAutomation(Logger logger, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, receiverFactory, factories.command(), factories.value(), factories.conditions(), factories.tasks());
    }
}
