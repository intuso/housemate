package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.ProxyOption;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.comms.v1_0.api.payload.OptionData;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

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
    protected AndroidProxyOption(Logger logger, ListenersFactory listenersFactory, OptionData data, AndroidProxyFactory factory) {
        super(logger, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected AndroidProxyList<SubTypeData, AndroidProxySubType> createChildInstance(ListData<SubTypeData> data) {
        return factory.<SubTypeData, AndroidProxySubType>createList(data);
    }
}
