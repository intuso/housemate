package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.NoChildrenProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxySubType;
import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxySubType extends ProxySubType<AndroidProxySubType> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxySubType(Log log, ListenersFactory listenersFactory, SubTypeData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
        return null;
    }
}
