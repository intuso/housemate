package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.resources.Resources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 21/05/13
 * Time: 22:49
 * To change this template use File | Settings | File Templates.
 */
public interface BrokerResources<R> extends Resources {
    ServerComms getComms();
    LifecycleHandler getLifecycleHandler();
    R getRoot();
}
