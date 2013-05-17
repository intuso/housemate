package com.intuso.housemate.proxy;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.value.ValueWrappable;
import com.intuso.housemate.core.object.value.ValueWrappableBase;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 10/05/13
* Time: 00:09
* To change this template use File | Settings | File Templates.
*/
public class ProxyValue<
            R extends ProxyResources<NoChildrenProxyObjectFactory>,
            T extends ProxyType<?, ?, ?, ?, ?, ?>, V extends ProxyValue<R, T, V>>
        extends ProxyValueBase<R, ProxyResources<NoChildrenProxyObjectFactory>, ValueWrappable, NoChildrenWrappable, NoChildrenProxyObject, T, V> {
    public ProxyValue(R resources, ValueWrappable value) {
        super(resources, null, value);
    }
}
