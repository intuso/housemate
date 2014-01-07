package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.ProxyValue;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyValue extends ProxyValue<GWTProxyType, GWTProxyValue> {
    public GWTProxyValue(Log log,
                         Injector injector,
                         @Assisted ValueData data) {
        super(log, injector, data);
    }
}
