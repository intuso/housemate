package com.intuso.housemate.broker.plugin.impl;

import com.intuso.housemate.broker.plugin.RealDeviceFactory;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.impl.device.OnOffCommandDevice;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 27/02/13
* Time: 23:11
* To change this template use File | Settings | File Templates.
*/
class OnOffCommandDeviceFactory implements RealDeviceFactory<OnOffCommandDevice> {

    @Override
    public String getTypeId() {
        return "on-off-command";
    }

    @Override
    public String getTypeName() {
        return "On Off Command";
    }

    @Override
    public String getTypeDescription() {
        return "Device which runs a configured command to turn things on and off";
    }

    @Override
    public OnOffCommandDevice create(RealResources resources, String id, String name, String description) throws HousemateException {
        return new OnOffCommandDevice(resources, id, name, description);
    }
}
