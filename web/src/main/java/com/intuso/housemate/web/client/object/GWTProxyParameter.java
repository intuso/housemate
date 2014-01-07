package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.object.proxy.ProxyParameter;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyParameter extends ProxyParameter<
            GWTProxyType,
            GWTProxyParameter> {
    public GWTProxyParameter(Log log,
                             Injector injector,
                             @Assisted ParameterData data) {
        super(log, injector, data);
    }
}
