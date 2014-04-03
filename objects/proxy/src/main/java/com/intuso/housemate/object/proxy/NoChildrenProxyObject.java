package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public final class NoChildrenProxyObject extends ProxyObject<NoChildrenData, NoChildrenData, NoChildrenProxyObject,
        NoChildrenProxyObject, ObjectListener> {

    private NoChildrenProxyObject() {
        super(null, null, null);
    }

    @Override
    protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
        return null;
    }
}
