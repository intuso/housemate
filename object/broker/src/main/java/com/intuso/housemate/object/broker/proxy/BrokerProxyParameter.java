package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;

public class BrokerProxyParameter
        extends BrokerProxyObject<ParameterWrappable, NoChildrenWrappable, NoChildrenBrokerProxyObject, BrokerProxyParameter, ParameterListener>
        implements Parameter<BrokerProxyType> {

    private BrokerProxyType type;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyParameter(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, ParameterWrappable data) {
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
