package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 */
public final class NoChildrenProxyObject
        extends ProxyObject<
            ProxyResources<NoChildrenProxyObjectFactory>,
            ProxyResources<NoChildrenProxyObjectFactory>,
            NoChildrenWrappable, NoChildrenWrappable, NoChildrenProxyObject,
            NoChildrenProxyObject, ObjectListener> {
    private NoChildrenProxyObject() {
        super(null, null, null);
    }

    @Override
    protected ProxyResources<NoChildrenProxyObjectFactory> getSubResources() {
        return null;
    }
}
