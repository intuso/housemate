package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.object.broker.BrokerResources;
import com.intuso.housemate.object.real.RealResources;

public interface BrokerRealResources extends BrokerResources<BrokerRealRootObject> {
    RealResources getRealResources();
}
