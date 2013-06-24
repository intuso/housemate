package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.object.proxy.ProxyTask;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyTask extends ProxyTask<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyValue,
            GWTProxyList<PropertyWrappable, GWTProxyProperty>,
        GWTProxyTask> {
    public GWTProxyTask(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                        GWTResources<?> subResources,
                        TaskWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}
