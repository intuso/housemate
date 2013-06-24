package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of devices
 */
public interface HasDevices<L extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>> {

    /**
     * Gets the devices list
     * @return the devices list
     */
    public L getDevices();
}
