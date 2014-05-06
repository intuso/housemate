package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

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
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxyProperty(Log log, ListenersFactory listenersFactory, PropertyData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected AndroidProxyCommand createChildInstance(CommandData commandData) {
        return factory.createCommand(commandData);
    }
}
