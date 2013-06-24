package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeWrappable;

/**
 */
public abstract class ProxyType<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, SWBL, SWR>>,
            SR extends ProxyResources<?>,
            WBL extends TypeWrappable<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>,
            T extends ProxyType<R, SR, WBL, SWBL, SWR, T>>
        extends ProxyObject<R, SR, WBL, SWBL, SWR, T, TypeListener>
        implements Type {
    public ProxyType(R resources, SR subResources, WBL wrappable) {
        super(resources, subResources, wrappable);
    }
}
