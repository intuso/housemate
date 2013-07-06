package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;

public class NoChildrenBrokerRealObject extends BrokerRealObject<NoChildrenData, NoChildrenData,
        NoChildrenBrokerRealObject, ObjectListener> {
    private NoChildrenBrokerRealObject(BrokerRealResources resources, NoChildrenData data) {
        super(resources, data);
    }
}
