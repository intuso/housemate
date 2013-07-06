package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;

public class BrokerProxyParameter
        extends BrokerProxyObject<ParameterData, NoChildrenData, NoChildrenBrokerProxyObject, BrokerProxyParameter, ParameterListener>
        implements Parameter<BrokerProxyType> {

    private BrokerProxyType type;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyParameter(BrokerProxyResources<NoChildrenBrokerProxyObjectFactory> resources, ParameterData data) {
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
