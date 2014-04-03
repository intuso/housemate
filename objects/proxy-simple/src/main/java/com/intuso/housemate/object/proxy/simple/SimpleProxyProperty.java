package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyProperty extends ProxyProperty<
        SimpleProxyType,
        SimpleProxyCommand,
        SimpleProxyProperty> {

    private final Injector injector;

    @Inject
    public SimpleProxyProperty(Log log,
                               ListenersFactory listenersFactory,
                               Injector injector,
                               @Assisted PropertyData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected SimpleProxyCommand createChildInstance(CommandData data) {
        return injector.getInstance(new Key<HousemateObjectFactory<CommandData, SimpleProxyCommand>>() {}).create(data);
    }
}
