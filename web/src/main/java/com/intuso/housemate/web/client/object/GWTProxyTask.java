package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.proxy.ProxyTask;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyTask extends ProxyTask<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyCommand,
            GWTProxyValue,
            GWTProxyList<PropertyData, GWTProxyProperty>,
        GWTProxyTask> {
    public GWTProxyTask(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                        GWTResources<?> childResources,
                        TaskData data) {
        super(resources, childResources, data);
    }
}
