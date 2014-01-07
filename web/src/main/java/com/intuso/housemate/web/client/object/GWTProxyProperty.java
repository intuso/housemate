package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyProperty extends ProxyProperty<
            GWTProxyType,
            GWTProxyCommand,
            GWTProxyProperty> {
    public GWTProxyProperty(Log log,
                            Injector injector,
                            @Assisted PropertyData data) {
        super(log, injector, data);
    }
}
