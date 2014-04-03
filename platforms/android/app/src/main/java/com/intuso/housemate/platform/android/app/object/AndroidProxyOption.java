package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyOption extends ProxyOption<AndroidProxySubType,
        AndroidProxyList<SubTypeData, AndroidProxySubType>, AndroidProxyOption> {

    private final AndroidProxyFactory factory;

    /**
     * @param log  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxyOption(Log log, ListenersFactory listenersFactory, OptionData data, AndroidProxyFactory factory) {
        super(log, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected AndroidProxyList<SubTypeData, AndroidProxySubType> createChildInstance(ListData<SubTypeData> data) {
        return factory.<SubTypeData, AndroidProxySubType>createList(data);
    }
}
