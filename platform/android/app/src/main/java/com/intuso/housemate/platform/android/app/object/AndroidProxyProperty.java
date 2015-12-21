package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyProperty;
import com.intuso.housemate.comms.v1_0.api.payload.CommandData;
import com.intuso.housemate.comms.v1_0.api.payload.PropertyData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyProperty extends ProxyProperty<AndroidProxyType, AndroidProxyCommand, AndroidProxyProperty> {

    private final AndroidProxyFactory factory;

    /**
     * @param logger  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxyProperty(Logger logger, ListenersFactory listenersFactory, PropertyData data, AndroidProxyFactory factory) {
        super(logger, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected AndroidProxyCommand createChild(CommandData data) {
        return factory.createCommand(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
