package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyAutomation;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyAutomation extends ProxyAutomation<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyCommand,
            GWTProxyValue,
            GWTProxyCondition,
            GWTProxyList<ConditionWrappable, GWTProxyCondition>,
        GWTProxyTask,
            GWTProxyList<TaskWrappable, GWTProxyTask>,
        GWTProxyAutomation> {
    public GWTProxyAutomation(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                              GWTResources<?> childResources,
                              AutomationWrappable data) {
        super(resources, childResources, data);
    }
}
