package com.intuso.housemate.api.object.hardware;

import com.intuso.housemate.api.object.list.List;

/**
 *
 * Interface to show that the implementing object has a list of devices
 */
public interface HasHardwares<L extends List<? extends Hardware<?, ?>>> {

    /**
     * Gets the hardware list
     * @return the hardware list
     */
    public L getHardwares();
}
