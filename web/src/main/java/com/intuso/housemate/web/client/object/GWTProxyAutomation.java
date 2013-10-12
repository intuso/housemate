package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyAutomation;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyAutomation extends ProxyAutomation<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyCommand,
            GWTProxyValue,
            GWTProxyCondition,
            GWTProxyList<ConditionData, GWTProxyCondition>,
            GWTProxyTask,
            GWTProxyList<TaskData, GWTProxyTask>,
            GWTProxyAutomation> {
    public GWTProxyAutomation(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                              GWTResources<?> childResources,
                              AutomationData data) {
        super(resources, childResources, data);
    }
}
