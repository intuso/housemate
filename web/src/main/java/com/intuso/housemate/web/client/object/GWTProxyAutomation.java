package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.proxy.ProxyAutomation;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyAutomation extends ProxyAutomation<
            GWTProxyCommand,
            GWTProxyValue,
            GWTProxyCondition,
            GWTProxyList<ConditionData, GWTProxyCondition>,
            GWTProxyTask,
            GWTProxyList<TaskData, GWTProxyTask>,
            GWTProxyAutomation> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyAutomation(Log log,
                              ListenersFactory listenersFactory,
                              GWTGinjector injector,
                              @Assisted AutomationData data) {
        super(log, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChildInstance(HousemateData<?> data) {
        return injector.getObjectFactory().create(data);
    }
}
