package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:57
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyParameter
        extends BrokerProxyObject<ParameterWrappable, NoChildrenWrappable, NoChildrenBrokerProxyObject, BrokerProxyParameter, ParameterListener>
        implements Parameter<BrokerProxyType> {

    private BrokerProxyType type;

    public BrokerProxyParameter(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, ParameterWrappable wrappable) {
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
