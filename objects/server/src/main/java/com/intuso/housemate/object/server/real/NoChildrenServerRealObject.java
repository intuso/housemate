package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

public class NoChildrenServerRealObject extends ServerRealObject<NoChildrenData, NoChildrenData,
        NoChildrenServerRealObject, ObjectListener> {
    private NoChildrenServerRealObject(ServerRealResources resources, NoChildrenData data) {
        super(resources, data);
    }
}
