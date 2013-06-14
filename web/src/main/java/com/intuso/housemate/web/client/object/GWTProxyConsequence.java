package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.object.proxy.ProxyConsequence;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:34
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyConsequence extends ProxyConsequence<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyValue,
            GWTProxyList<PropertyWrappable, GWTProxyProperty>,
            GWTProxyConsequence> {
    public GWTProxyConsequence(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                               GWTResources<?> subResources,
                               ConsequenceWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}
