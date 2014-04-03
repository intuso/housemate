package com.intuso.housemate.object.proxy.simple;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.proxy.ProxyAutomation;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/01/14
* Time: 13:15
* To change this template use File | Settings | File Templates.
*/
public final class SimpleProxyAutomation extends ProxyAutomation<
        SimpleProxyCommand,
        SimpleProxyValue,
        SimpleProxyCondition,
        SimpleProxyList<ConditionData, SimpleProxyCondition>, SimpleProxyTask, SimpleProxyList<TaskData, SimpleProxyTask>, SimpleProxyAutomation> {

    private final Injector injector;

    @Inject
    public SimpleProxyAutomation(Log log,
                                 ListenersFactory listenersFactory,
                                 Injector injector,
                                 @Assisted AutomationData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getInstance(new Key<HousemateObjectFactory<HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>>>() {}).create(data);
    }
}
