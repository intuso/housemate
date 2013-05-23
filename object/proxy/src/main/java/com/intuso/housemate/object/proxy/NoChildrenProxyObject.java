package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 20/02/13
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
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
