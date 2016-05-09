package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyAutomation;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyAutomation extends ProxyAutomation<AndroidProxyCommand, AndroidProxyValue,
        AndroidProxyList<AndroidProxyCondition>,
        AndroidProxyList<AndroidProxyTask>, AndroidProxyAutomation> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyAutomation(Logger logger, ListenersFactory listenersFactory,
                                     ProxyObject.Factory<AndroidProxyCommand> commandFactory,
                                     ProxyObject.Factory<AndroidProxyValue> valueFactory,
                                     ProxyObject.Factory<AndroidProxyList<AndroidProxyCondition>> conditionsFactory,
                                     ProxyObject. Factory<AndroidProxyList<AndroidProxyTask>> tasksFactory) {
        super(logger, listenersFactory, commandFactory, valueFactory, conditionsFactory, tasksFactory);
    }
}
