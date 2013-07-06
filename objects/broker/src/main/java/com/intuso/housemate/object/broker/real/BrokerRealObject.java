package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.ObjectListener;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 * @param <LISTENER> the type of the listener
 */
public class BrokerRealObject<
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends HousemateObject<?, ? extends CHILD_DATA, ?, ?, ?>,
            LISTENER extends ObjectListener>
        extends HousemateObject<BrokerRealResources, DATA, CHILD_DATA, CHILD, LISTENER> {

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected BrokerRealObject(BrokerRealResources resources, DATA data) {
        super(resources, data);
    }
}
