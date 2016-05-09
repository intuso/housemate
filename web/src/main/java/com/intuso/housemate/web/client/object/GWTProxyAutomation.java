package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Condition;
import com.intuso.housemate.client.v1_0.api.object.Task;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyAutomation;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyAutomation extends ProxyAutomation<
            GWTProxyCommand,
            GWTProxyValue,
            GWTProxyCondition,
            GWTProxyList<Condition.Data, GWTProxyCondition>,
            GWTProxyTask,
            GWTProxyList<Task.Data, GWTProxyTask>,
            GWTProxyAutomation> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyAutomation(Logger logger,
                              ListenersFactory listenersFactory,
                              GWTGinjector injector,
                              @Assisted Data data) {
        super(logger, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(Object.Data<?> data) {
        return injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
