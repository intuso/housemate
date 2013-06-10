package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.subtype.SubType;
import com.intuso.housemate.api.object.subtype.SubTypeListener;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:57
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxySubType
        extends BrokerProxyObject<SubTypeWrappable, NoChildrenWrappable, NoChildrenBrokerProxyObject, BrokerProxySubType, SubTypeListener>
        implements SubType<BrokerProxyType> {

    private BrokerProxyType type;

    public BrokerProxySubType(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, SubTypeWrappable wrappable) {
        super(resources, wrappable);
    }

    @Override
    public void getChildObjects() {
        super.getChildObjects();
        type = getResources().getRoot().getTypes().get(getWrappable().getType());
        if(type == null)
            getLog().e("Could not unwrap value, value type \"" + getWrappable().getType() + "\" is not known");
    }

    public final BrokerProxyType getType() {
        return type;
    }
}
