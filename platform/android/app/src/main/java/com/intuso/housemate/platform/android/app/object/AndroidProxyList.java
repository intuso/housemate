package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.ProxyList;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyList<CHILD_DATA extends HousemateData<?>, CHILD extends ProxyObject<CHILD_DATA, ?, ?, ?, ?>>
        extends ProxyList<CHILD_DATA, CHILD, AndroidProxyList<CHILD_DATA, CHILD>> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    public AndroidProxyList(Logger logger, ListenersFactory listenersFactory, ListData data, AndroidProxyFactory factory) {
        super(logger, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected CHILD createChildInstance(CHILD_DATA data) {
        return (CHILD) factory.create(data);
    }
}
