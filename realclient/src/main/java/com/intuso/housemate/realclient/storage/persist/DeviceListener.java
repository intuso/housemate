package com.intuso.housemate.realclient.storage.persist;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.utilities.log.Log;

/**
 * Created by tomc on 03/02/15.
 */
public class DeviceListener
        extends PrimaryObjectListener<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        implements com.intuso.housemate.api.object.device.DeviceListener<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> {

    @Inject
    protected DeviceListener(Log log, Persistence persistence) {
        super(log, persistence);
    }

    @Override
    public void deviceConnected(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> device, boolean connected) {

    }
}
