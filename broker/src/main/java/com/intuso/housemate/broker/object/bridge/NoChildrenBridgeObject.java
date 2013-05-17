package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 23/02/13
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class NoChildrenBridgeObject extends BridgeObject<NoChildrenWrappable, NoChildrenWrappable,
            NoChildrenBridgeObject, NoChildrenBridgeObject, ObjectListener> {
    protected NoChildrenBridgeObject(BrokerBridgeResources resources, NoChildrenWrappable wrappable) {
        super(resources, wrappable);
    }
}
