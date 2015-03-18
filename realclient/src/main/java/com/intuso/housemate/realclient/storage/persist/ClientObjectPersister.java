package com.intuso.housemate.realclient.storage.persist;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.property.Property;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/11/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class ClientObjectPersister {

    private final HardwareListWatcher hardwareListWatcher;
    private final DeviceListWatcher deviceListWatcher;

    @Inject
    public ClientObjectPersister(HardwareListWatcher hardwareListWatcher, DeviceListWatcher deviceListWatcher) {
        this.hardwareListWatcher = hardwareListWatcher;
        this.deviceListWatcher = deviceListWatcher;
    }

    public void watchHardwares(List<? extends Hardware<?, ?>> hardwares) {
        hardwares.addObjectListener(hardwareListWatcher, true);
    }

    public void watchDevices(List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> devices) {
        devices.addObjectListener(deviceListWatcher, true);
    }
}
