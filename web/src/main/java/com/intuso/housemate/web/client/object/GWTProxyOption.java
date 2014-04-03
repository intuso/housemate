package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.housemate.web.client.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyOption extends ProxyOption<
            GWTProxySubType,
            GWTProxyList<SubTypeData, GWTProxySubType>,
            GWTProxyOption> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyOption(Log log,
                          ListenersFactory listenersFactory,
                          GWTGinjector injector,
                          @Assisted OptionData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected GWTProxyList<SubTypeData, GWTProxySubType> createChildInstance(ListData<SubTypeData> data) {
        return new GWTProxyList<SubTypeData, GWTProxySubType>(getLog(), getListenersFactory(), injector, data);
    }
}
