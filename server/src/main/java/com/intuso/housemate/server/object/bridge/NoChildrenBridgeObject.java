package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.object.api.internal.ObjectListener;

/**
 */
public class NoChildrenBridgeObject extends BridgeObject<NoChildrenData, NoChildrenData,
            NoChildrenBridgeObject, NoChildrenBridgeObject, ObjectListener> {
    private NoChildrenBridgeObject() {
        super(null, null, null);
    }
}
