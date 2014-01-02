package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

public class NoChildrenServerProxyObject extends ServerProxyObject<NoChildrenData, NoChildrenData,
        NoChildrenServerProxyObject, NoChildrenServerProxyObject, ObjectListener> {
    private NoChildrenServerProxyObject(ServerProxyResources<NoChildrenServerProxyObjectFactory> resources, NoChildrenData data) {
        super(resources, data);
    }
}
