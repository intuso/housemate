package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 */
public class NoChildrenBridgeObject extends BridgeObject<NoChildrenWrappable, NoChildrenWrappable,
            NoChildrenBridgeObject, NoChildrenBridgeObject, ObjectListener> {
    protected NoChildrenBridgeObject(BrokerBridgeResources resources) {
        super(resources, null);
    }
}
