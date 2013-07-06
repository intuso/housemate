package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

public final class NoChildrenProxyObject
        extends ProxyObject<
            ProxyResources<NoChildrenProxyObjectFactory>,
            ProxyResources<NoChildrenProxyObjectFactory>,
        NoChildrenData, NoChildrenData, NoChildrenProxyObject,
            NoChildrenProxyObject, ObjectListener> {
    private NoChildrenProxyObject() {
        super(null, null, null);
    }

    @Override
    protected ProxyResources<NoChildrenProxyObjectFactory> getSubResources() {
        return null;
    }
}
