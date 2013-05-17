package com.intuso.housemate.web.client.object;

import com.intuso.housemate.core.object.type.option.OptionWrappable;
import com.intuso.housemate.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.proxy.ProxyOption;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:03
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyOption extends ProxyOption<GWTResources<NoChildrenProxyObjectFactory>, GWTProxyOption> {
    public GWTProxyOption(GWTResources<NoChildrenProxyObjectFactory> resources, OptionWrappable wrappable) {
        super(resources, wrappable);
    }
}
