package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyList<SWBL extends HousemateData<?>,
            SWR extends ProxyObject<? extends SWBL, ?, ?, ?, ?>>
        extends ProxyList<
                    SWBL,
                    SWR,
                    GWTProxyList<SWBL, SWR>> {

    public GWTProxyList(Log log,
                        Injector injector,
                        @Assisted ListData data) {
        super(log, injector, data);
    }
}
