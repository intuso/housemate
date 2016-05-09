package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyTask;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyTask extends ProxyTask<AndroidProxyCommand,
        AndroidProxyValue,
        AndroidProxyProperty,
        AndroidProxyList<AndroidProxyProperty>,
        AndroidProxyTask> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyTask(Logger logger, ListenersFactory listenersFactory,
                               ProxyObject.Factory<AndroidProxyCommand> commandFactory,
                               ProxyObject.Factory<AndroidProxyValue> valueFactory,
                               ProxyObject.Factory<AndroidProxyProperty> propertyFactory,
                               ProxyObject.Factory<AndroidProxyList<AndroidProxyProperty>> propertiesFactory) {
        super(logger, listenersFactory, commandFactory, valueFactory, propertyFactory, propertiesFactory);
    }
}
