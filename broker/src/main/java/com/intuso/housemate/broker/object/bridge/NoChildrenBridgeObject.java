package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

/**
 */
public class NoChildrenBridgeObject extends BridgeObject<NoChildrenData, NoChildrenData,
            NoChildrenBridgeObject, NoChildrenBridgeObject, ObjectListener> {
    protected NoChildrenBridgeObject(BrokerBridgeResources resources) {
        super(resources, null);
    }
}
