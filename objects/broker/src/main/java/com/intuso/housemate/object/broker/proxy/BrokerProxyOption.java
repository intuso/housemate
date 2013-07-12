package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.subtype.SubTypeData;

public class BrokerProxyOption
        extends BrokerProxyObject<OptionData, ListData<SubTypeData>,
            BrokerProxyList<SubTypeData, BrokerProxySubType>,
            BrokerProxyOption,
            OptionListener>
        implements Option<BrokerProxyList<SubTypeData, BrokerProxySubType>> {

    private BrokerProxyList<SubTypeData, BrokerProxySubType> subTypes;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyOption(BrokerProxyResources<BrokerProxyFactory.List<SubTypeData, BrokerProxySubType>> resources,
                             OptionData data) {
        super(resources, data);
    }

    @Override
    protected void getChildObjects() {
        subTypes = getChild(SUB_TYPES_ID);
    }

    @Override
    public BrokerProxyList<SubTypeData, BrokerProxySubType> getSubTypes() {
        return subTypes;
    }
}
