package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyType extends ProxyType<TypeData<HousemateData<?>>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, AndroidProxyType> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory Android proxy object factory
     */
    public AndroidProxyType(Log log, ListenersFactory listenersFactory, TypeData<HousemateData<?>> data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected ProxyObject createChildInstance(HousemateData<?> housemateData) {
        return factory.create(housemateData);
    }
}
