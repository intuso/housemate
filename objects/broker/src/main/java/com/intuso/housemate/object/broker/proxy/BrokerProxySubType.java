package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

public class BrokerProxySubType
        extends BrokerProxyObject<SubTypeWrappable, NoChildrenWrappable, NoChildrenBrokerProxyObject, BrokerProxySubType, SubTypeListener>
        implements SubType<BrokerProxyType> {

    private BrokerProxyType type;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxySubType(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, SubTypeWrappable data) {
        super(resources, data);
    }

    @Override
    public void getChildObjects() {
        super.getChildObjects();
        type = getResources().getRoot().getTypes().get(getData().getType());
        if(type == null)
            getLog().e("Could not unwrap value, value type \"" + getData().getType() + "\" is not known");
    }

    @Override
    public final BrokerProxyType getType() {
        return type;
    }
}
