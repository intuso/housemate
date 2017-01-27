package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Property;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyProperty;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.collection.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyProperty extends ProxyProperty<
            GWTProxyType,
            GWTProxyCommand,
            GWTProxyProperty> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyProperty(Logger logger,
                            ListenersFactory managedCollectionFactory,
                            GWTGinjector injector,
                            @Assisted Property.Data data) {
        super(logger, managedCollectionFactory, data);
        this.injector = injector;
    }

    @Override
    protected GWTProxyCommand createChild(Command.Data data) {
        return new GWTProxyCommand(getLogger(), getListenersFactory(), injector, data);
    }
}
