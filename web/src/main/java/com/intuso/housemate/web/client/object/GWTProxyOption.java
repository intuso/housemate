package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyOption extends ProxyOption<
            GWTResources<GWTProxyFactory.List<SubTypeData, GWTProxySubType>>,
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SubTypeData, GWTProxySubType>>,
            GWTProxySubType,
            GWTProxyList<SubTypeData, GWTProxySubType>,
            GWTProxyOption> {
    public GWTProxyOption(GWTResources<GWTProxyFactory.List<SubTypeData, GWTProxySubType>> resources,
                          GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SubTypeData, GWTProxySubType>> childResources,
                          OptionData data) {
        super(resources, childResources, data);
    }
}
