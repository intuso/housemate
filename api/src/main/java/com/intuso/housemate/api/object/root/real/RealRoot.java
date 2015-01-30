package com.intuso.housemate.api.object.root.real;

import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.HasDevices;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.HasTypes;
import com.intuso.housemate.api.object.type.Type;

/**
 * @param <TYPE> the type of the types
 * @param <TYPES> the type of the types list
 * @param <DEVICE> the type of the devices
 * @param <DEVICES> the type of the devices list
 * @param <ROOT> the type of the root
 */
public interface RealRoot<
            TYPE extends Type,
            TYPES extends List<TYPE>,
            DEVICE extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
            DEVICES extends List<DEVICE>,
            ROOT extends RealRoot<TYPE, TYPES, DEVICE, DEVICES, ROOT>>
        extends Root<ROOT>, HasTypes<TYPES>, HasDevices<DEVICES> {

    /**
     * Adds a new type
     * @param type the new type
     */
    public void addType(TYPE type);

    /**
     * removes a type
     * @param id the type id
     */
    public void removeType(String id);

    /**
     * Adds a new device
     * @param device the new device
     */
    public void addDevice(DEVICE device);

    /**
     * Removes a device
     * @param id the device id
     */
    public void removeDevice(String id);
}
