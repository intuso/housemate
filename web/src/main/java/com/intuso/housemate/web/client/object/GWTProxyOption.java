package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyOption extends ProxyOption<
            GWTResources<GWTProxyFactory.List<SubTypeWrappable, GWTProxySubType>>,
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SubTypeWrappable, GWTProxySubType>>,
            GWTProxySubType,
            GWTProxyList<SubTypeWrappable, GWTProxySubType>,
            GWTProxyOption> {
    public GWTProxyOption(GWTResources<GWTProxyFactory.List<SubTypeWrappable, GWTProxySubType>> resources,
                          GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SubTypeWrappable, GWTProxySubType>> childResources,
                          OptionWrappable wrappable) {
        super(resources, childResources, wrappable);
    }
}
