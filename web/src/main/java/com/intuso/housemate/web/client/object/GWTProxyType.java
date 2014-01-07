package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyType extends ProxyType<
        TypeData<HousemateData<?>>,
        HousemateData<?>,
            ProxyObject<?, ?, ?, ?, ?>,
            GWTProxyType> {
    @Inject
    public GWTProxyType(Log log,
                        Injector injector,
                        @Assisted TypeData data) {
        super(log, injector, data);
    }
}
