package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.object.proxy.ProxySubType;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxySubType extends ProxySubType<
        GWTProxyType,
        GWTProxySubType> {
    public GWTProxySubType(Log log,
                           Injector injector,
                           @Assisted SubTypeData data) {
        super(log, injector, data);
    }
}
