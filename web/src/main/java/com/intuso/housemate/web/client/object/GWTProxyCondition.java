package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyCondition;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyCondition extends ProxyCondition<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyValue,
            GWTProxyList<PropertyData, GWTProxyProperty>,
            GWTProxyCommand,
            GWTProxyCondition, GWTProxyList<ConditionData, GWTProxyCondition>> {
    public GWTProxyCondition(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                             GWTResources childResources,
                             ConditionData data) {
        super(resources, childResources, data);
    }
}
