package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.utilities.log.Log;

/**
 */
public class NoChildrenBridgeObject extends BridgeObject<NoChildrenData, NoChildrenData,
            NoChildrenBridgeObject, NoChildrenBridgeObject, ObjectListener> {
    protected NoChildrenBridgeObject(Log log) {
        super(log, null);
    }
}
