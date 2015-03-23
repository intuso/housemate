package com.intuso.housemate.object.server;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

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
