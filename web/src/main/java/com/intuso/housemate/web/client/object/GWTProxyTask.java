package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.proxy.ProxyTask;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyTask extends ProxyTask<
            GWTProxyCommand,
            GWTProxyValue,
            GWTProxyList<PropertyData, GWTProxyProperty>,
        GWTProxyTask> {
    public GWTProxyTask(Log log,
                        Injector injector,
                        @Assisted TaskData data) {
        super(log, injector, data);
    }
}
