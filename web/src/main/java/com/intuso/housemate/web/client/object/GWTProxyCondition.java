package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyCondition;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyCondition extends ProxyCondition<
            GWTProxyCommand, GWTProxyValue,
            GWTProxyList<PropertyData, GWTProxyProperty>,
            GWTProxyCondition, GWTProxyList<ConditionData, GWTProxyCondition>> {
    public GWTProxyCondition(Log log,
                             Injector injector,
                             @Assisted ConditionData data) {
        super(log, injector, data);
    }
}
