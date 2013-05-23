package com.intuso.housemate.broker.plugin.device;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.device.OnOffCommandDevice;
import com.intuso.housemate.plugin.api.RealDeviceFactory;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 27/02/13
* Time: 23:11
* To change this template use File | Settings | File Templates.
*/
public class OnOffCommandDeviceFactory implements RealDeviceFactory<OnOffCommandDevice> {

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
