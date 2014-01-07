package com.intuso.housemate.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.utilities.log.Log;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/01/14
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public final class NoChildrenProxyObject extends ProxyObject<NoChildrenData, NoChildrenData, NoChildrenProxyObject,
        NoChildrenProxyObject, ObjectListener> {
    /**
     * @param log      the log
     * @param injector the injector
     * @param data     the data object
     */
    private NoChildrenProxyObject(Log log, Injector injector, NoChildrenData data) {
        super(log, injector, data);
    }
}
