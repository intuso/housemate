package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyCommand extends ProxyCommand<AndroidProxyValue, AndroidProxyParameter,
        AndroidProxyList<ParameterData, AndroidProxyParameter>, AndroidProxyCommand> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxyCommand(Log log, ListenersFactory listenersFactory, CommandData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return factory.create(data);
    }
}
