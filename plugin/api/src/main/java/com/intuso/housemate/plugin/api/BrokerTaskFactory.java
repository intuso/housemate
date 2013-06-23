package com.intuso.housemate.plugin.api;

import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.api.HousemateException;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/02/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public interface BrokerTaskFactory<T extends BrokerRealTask> {
    public String getTypeId();
    public String getTypeName();
    public String getTypeDescription();
    public T create(BrokerRealResources resources, String id, String name, String description) throws HousemateException;
}
