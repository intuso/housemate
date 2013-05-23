package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:05
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyList<SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
        extends ProxyList<GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>>,
                    GWTResources<?>,
                    SWBL,
                    SWR,
                    GWTProxyList<SWBL, SWR>> {

    public GWTProxyList(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SWBL, SWR>> resources,
                        GWTResources<?> subResources,
                        ListWrappable wblListWrappable) {
        super(resources, subResources, wblListWrappable);
    }
}
