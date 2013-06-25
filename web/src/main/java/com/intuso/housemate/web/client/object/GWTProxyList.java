package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyList<SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
        extends ProxyList<GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>>,
                    GWTResources<?>,
                    SWBL,
                    SWR,
                    GWTProxyList<SWBL, SWR>> {

    public GWTProxyList(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>> resources,
                        GWTResources<?> childResources,
                        ListWrappable wblListWrappable) {
        super(resources, childResources, wblListWrappable);
    }
}
