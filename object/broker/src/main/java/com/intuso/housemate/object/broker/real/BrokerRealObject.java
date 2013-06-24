package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 */
public class BrokerRealObject<WBL extends HousemateObjectWrappable<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends HousemateObject<?, ? extends SWBL, ?, ?, ?>,
            L extends ObjectListener>
        extends HousemateObject<BrokerRealResources, WBL, SWBL, SWR, L> {
    protected BrokerRealObject(BrokerRealResources resources, WBL wrappable) {
        super(resources, wrappable);
    }
}
