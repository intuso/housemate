package com.intuso.housemate.server.object.proxy;

import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.object.api.internal.ObjectListener;

public class NoChildrenServerProxyObject extends ServerProxyObject<NoChildrenData, NoChildrenData,
        NoChildrenServerProxyObject, NoChildrenServerProxyObject, ObjectListener> {

    private NoChildrenServerProxyObject() {
        super(null, null, null, null);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // nothing to do
    }
}
