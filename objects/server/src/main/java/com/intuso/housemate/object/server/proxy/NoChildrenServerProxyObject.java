package com.intuso.housemate.object.server.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.utilities.log.Log;

public class NoChildrenServerProxyObject extends ServerProxyObject<NoChildrenData, NoChildrenData,
        NoChildrenServerProxyObject, NoChildrenServerProxyObject, ObjectListener> {
    private NoChildrenServerProxyObject(Log log, Injector injector, NoChildrenData data) {
        super(log, injector, data);
    }
}
