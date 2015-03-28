package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:17
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyOption extends ProxyOption<
        SimpleProxySubType,
        SimpleProxyList<SubTypeData, SimpleProxySubType>,
        SimpleProxyOption> {

    private final Injector injector;

    @Inject
    public SimpleProxyOption(Log log,
                             ListenersFactory listenersFactory,
                             Injector injector,
                             @Assisted OptionData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected SimpleProxyList<SubTypeData, SimpleProxySubType> createChildInstance(ListData<SubTypeData> data) {
        return new SimpleProxyList<>(getLog(), getListenersFactory(), injector, data);
    }
}
