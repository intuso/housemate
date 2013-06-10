package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 27/02/13
 * Time: 08:16
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyOption
        extends BrokerProxyObject<OptionWrappable, ListWrappable<SubTypeWrappable>,
            BrokerProxyList<SubTypeWrappable, BrokerProxySubType>,
            BrokerProxyOption,
            OptionListener>
        implements Option<BrokerProxyList<SubTypeWrappable, BrokerProxySubType>> {

    private BrokerProxyList<SubTypeWrappable, BrokerProxySubType> subTypes;

    public BrokerProxyOption(BrokerProxyResources<BrokerProxyFactory.List<SubTypeWrappable, BrokerProxySubType>> resources,
                             OptionWrappable wrappable) {
        super(resources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        subTypes = getWrapper(SUB_TYPES);
    }

    @Override
    public BrokerProxyList<SubTypeWrappable, BrokerProxySubType> getSubTypes() {
        return subTypes;
    }
}
