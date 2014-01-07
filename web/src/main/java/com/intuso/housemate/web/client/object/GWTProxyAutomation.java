package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.proxy.ProxyAutomation;
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
    public GWTProxyAutomation(Log log,
                              Injector injector,
                              @Assisted AutomationData data) {
        super(log, injector, data);
    }
}
