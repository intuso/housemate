package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.object.proxy.ProxyCondition;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:34
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyCondition extends ProxyCondition<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyValue,
            GWTProxyList<PropertyWrappable, GWTProxyProperty>,
            GWTProxyCommand,
            GWTProxyCondition, GWTProxyList<ConditionWrappable, GWTProxyCondition>> {
    public GWTProxyCondition(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                             GWTResources subResources,
                             ConditionWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}
