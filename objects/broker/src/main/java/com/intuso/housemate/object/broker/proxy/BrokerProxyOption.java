package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

public class BrokerProxyOption
        extends BrokerProxyObject<OptionWrappable, ListWrappable<SubTypeWrappable>,
            BrokerProxyList<SubTypeWrappable, BrokerProxySubType>,
            BrokerProxyOption,
            OptionListener>
        implements Option<BrokerProxyList<SubTypeWrappable, BrokerProxySubType>> {

    private BrokerProxyList<SubTypeWrappable, BrokerProxySubType> subTypes;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyOption(BrokerProxyResources<BrokerProxyFactory.List<SubTypeWrappable, BrokerProxySubType>> resources,
                             OptionWrappable data) {
        super(resources, data);
    }

    @Override
    protected void getChildObjects() {
        subTypes = getWrapper(SUB_TYPES_ID);
    }

    @Override
    public BrokerProxyList<SubTypeWrappable, BrokerProxySubType> getSubTypes() {
        return subTypes;
    }
}
