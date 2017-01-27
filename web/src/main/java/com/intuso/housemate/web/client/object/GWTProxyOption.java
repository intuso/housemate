package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.List;
import com.intuso.housemate.client.v1_0.api.object.SubType;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyOption;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.collection.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyOption extends ProxyOption<
            GWTProxySubType,
            GWTProxyList<SubType.Data, GWTProxySubType>,
            GWTProxyOption> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyOption(Logger logger,
                          ListenersFactory managedCollectionFactory,
                          GWTGinjector injector,
                          @Assisted Data data) {
        super(logger, managedCollectionFactory, data);
        this.injector = injector;
    }

    @Override
    protected GWTProxyList<SubType.Data, GWTProxySubType> createChild(List.Data<SubType.Data> data) {
        return new GWTProxyList<>(getLogger(), getListenersFactory(), injector, data);
    }
}
