package com.intuso.housemate.proxy;

import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.type.Type;
import com.intuso.housemate.core.object.type.TypeListener;
import com.intuso.housemate.core.object.type.TypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
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
