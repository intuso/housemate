package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.NoChildrenProxyObject;
import com.intuso.housemate.object.proxy.ProxyValue;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:26
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyValue extends ProxyValue<AndroidProxyType, AndroidProxyValue> {

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public AndroidProxyValue(Log log, ListenersFactory listenersFactory, ValueData data) {
        super(log, listenersFactory, data);
    }

    @Override
    protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
        return null;
    }
}
