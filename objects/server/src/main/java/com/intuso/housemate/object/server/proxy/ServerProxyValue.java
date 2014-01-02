package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.ValueData;

public class ServerProxyValue extends ServerProxyValueBase<ValueData, NoChildrenData,
            NoChildrenServerProxyObject, ServerProxyValue> {
    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ServerProxyValue(ServerProxyResources<NoChildrenServerProxyObjectFactory> resources, ValueData data) {
        super(resources, data);
    }
}
