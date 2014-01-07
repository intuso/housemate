package com.intuso.housemate.plugin.api;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.utilities.log.Log;

/**
 * @param <DEVICE> the type of the devices created by this factory
 */
public interface RealDeviceFactory<DEVICE extends RealDevice> {

    /**
     * Gets the id for this factory
     * @return the id for this factory
     */
    public String getTypeId();

    /**
     * Gets the name for this factory
     * @return the name for this factory
     */
    public String getTypeName();

    /**
     * Gets the description for this factory
     * @return the description for this factory
     */
    public String getTypeDescription();

    /**
     * Creates a device
     * @param log the log for the device
     * @param id the device's id
     * @param name the device's name
     * @param description the device's description
     * @return a new device
     * @throws HousemateException if the device cannot be created
     */
    public DEVICE create(Log log, String id, String name, String description) throws HousemateException;
}
