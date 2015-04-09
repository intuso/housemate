package com.intuso.housemate.server.object.real.persist;

import com.google.inject.Inject;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 03/02/15.
 */
public class DeviceListener
        extends PrimaryObjectListener<RealDevice>
        implements com.intuso.housemate.api.object.device.DeviceListener<RealDevice> {

    @Inject
    protected DeviceListener(Log log, Persistence persistence) {
        super(log, persistence);
    }

    @Override
    public void deviceConnected(RealDevice device, boolean connected) {

    }
}
