package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.option.Option;
import com.intuso.housemate.api.object.type.option.OptionListener;
import com.intuso.housemate.api.object.type.option.OptionWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 08:16
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyOption
        extends BrokerProxyObject<OptionWrappable, NoChildrenWrappable, NoChildrenBrokerProxyObject, BrokerProxyOption, OptionListener>
        implements Option {

    public BrokerProxyOption(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, OptionWrappable wrappable) {
        super(resources, wrappable);
    }
}
