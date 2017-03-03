package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyCommand;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyCommand extends ProxyCommand<AndroidProxyValue, AndroidProxyList<AndroidProxyParameter>, AndroidProxyCommand> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyCommand(Logger logger, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, Sender.Factory senderFactory, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, receiverFactory, senderFactory, factories.value(), factories.parameters());
    }
}
