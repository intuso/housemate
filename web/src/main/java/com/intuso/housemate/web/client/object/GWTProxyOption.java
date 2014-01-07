package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyOption extends ProxyOption<
            GWTProxySubType,
            GWTProxyList<SubTypeData, GWTProxySubType>,
            GWTProxyOption> {
    public GWTProxyOption(Log log,
                          Injector injector,
                          @Assisted OptionData data) {
        super(log, injector, data);
    }
}
