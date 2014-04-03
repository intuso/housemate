package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

/**
 */
public class NoChildrenBridgeObject extends BridgeObject<NoChildrenData, NoChildrenData,
            NoChildrenBridgeObject, NoChildrenBridgeObject, ObjectListener> {
    private NoChildrenBridgeObject() {
        super(null, null, null);
    }
}
