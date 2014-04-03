package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

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
    public AndroidProxyList(Log log, ListenersFactory listenersFactory, ListData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected CHILD createChildInstance(CHILD_DATA data) {
        return (CHILD) factory.create(data);
    }
}
