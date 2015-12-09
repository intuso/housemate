package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyOption;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.OptionData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyOption extends ProxyOption<
            GWTProxySubType,
            GWTProxyList<SubTypeData, GWTProxySubType>,
            GWTProxyOption> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyOption(Logger logger,
                          ListenersFactory listenersFactory,
                          GWTGinjector injector,
                          @Assisted OptionData data) {
        super(logger, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected GWTProxyList<SubTypeData, GWTProxySubType> createChildInstance(ListData<SubTypeData> data) {
        return new GWTProxyList<>(getLogger(), getListenersFactory(), injector, data);
    }
}
