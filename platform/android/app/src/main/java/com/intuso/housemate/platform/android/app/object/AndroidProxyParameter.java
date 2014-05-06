package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.object.proxy.NoChildrenProxyObject;
import com.intuso.housemate.object.proxy.ProxyParameter;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyParameter extends ProxyParameter<AndroidProxyType, AndroidProxyParameter> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory Android proxy object factory
     */
    public AndroidProxyParameter(Log log, ListenersFactory listenersFactory, ParameterData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
        return null;
    }
}
