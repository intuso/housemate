package com.intuso.housemate.broker.object;

import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.general.BrokerResourcesImpl;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealRootObject;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 22/05/13
 * Time: 07:17
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealResourcesImpl extends BrokerResourcesImpl<BrokerRealRootObject> implements BrokerRealResources {

    public BrokerRealResourcesImpl(BrokerGeneralResources generalResources) {
        super(generalResources);
    }
}
