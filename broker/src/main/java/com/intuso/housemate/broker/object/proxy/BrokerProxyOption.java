package com.intuso.housemate.broker.object.proxy;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.type.option.Option;
import com.intuso.housemate.core.object.type.option.OptionListener;
import com.intuso.housemate.core.object.type.option.OptionWrappable;

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
