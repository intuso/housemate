package com.intuso.housemate.broker.object;

import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.general.BrokerResourcesImpl;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealRootObject;

/**
 */
public class BrokerRealResourcesImpl extends BrokerResourcesImpl<BrokerRealRootObject> implements BrokerRealResources {

    public BrokerRealResourcesImpl(BrokerGeneralResources generalResources) {
        super(generalResources);
    }
}
