package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyList;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyObject;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyList<ELEMENT extends ProxyObject<?, ?, ?>>
        extends ProxyList<ELEMENT, AndroidProxyList<ELEMENT>> {

    /**
     * @param logger  {@inheritDoc}
     */
    public AndroidProxyList(Logger logger, String name, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory, ProxyObject.Factory<ELEMENT> factory) {
        super(logger, name, managedCollectionFactory, receiverFactory, factory);
    }
}
