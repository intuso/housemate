package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/02/13
 * Time: 09:02
 * To change this template use File | Settings | File Templates.
 */
public class NoChildrenBrokerRealObject extends BrokerRealObject<NoChildrenWrappable, NoChildrenWrappable,
        NoChildrenBrokerRealObject, ObjectListener> {
    private NoChildrenBrokerRealObject(BrokerRealResources resources, NoChildrenWrappable wrappable) {
        super(resources, wrappable);
    }
}
