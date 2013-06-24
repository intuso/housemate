package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 */
public class NoChildrenBrokerRealObject extends BrokerRealObject<NoChildrenWrappable, NoChildrenWrappable,
        NoChildrenBrokerRealObject, ObjectListener> {
    private NoChildrenBrokerRealObject(BrokerRealResources resources, NoChildrenWrappable wrappable) {
        super(resources, wrappable);
    }
}
