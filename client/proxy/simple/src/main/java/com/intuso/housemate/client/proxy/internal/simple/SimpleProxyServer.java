package com.intuso.housemate.client.proxy.internal.simple;

import com.google.inject.Inject;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyServer;
import com.intuso.housemate.client.proxy.internal.simple.ioc.Server;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyServer extends ProxyServer<
        SimpleProxyCommand,
        SimpleProxyList<SimpleProxyAutomation>,
        SimpleProxyList<SimpleProxyDevice>,
        SimpleProxyList<SimpleProxyUser>,
        SimpleProxyList<SimpleProxyNode>,
        SimpleProxyServer> {

    @Inject
    public SimpleProxyServer(@Server Logger logger,
                             Connection connection,
                             ManagedCollectionFactory managedCollectionFactory,
                             ProxyObject.Factory<SimpleProxyCommand> commandFactory,
                             ProxyObject.Factory<SimpleProxyList<SimpleProxyAutomation>> automationsFactory,
                             ProxyObject.Factory<SimpleProxyList<SimpleProxyDevice>> devicesFactory,
                             ProxyObject.Factory<SimpleProxyList<SimpleProxyUser>> usersFactory,
                             ProxyObject.Factory<SimpleProxyList<SimpleProxyNode>> nodesFactory) {
        super(connection, logger, managedCollectionFactory, commandFactory, automationsFactory, devicesFactory, usersFactory, nodesFactory);
    }
}
