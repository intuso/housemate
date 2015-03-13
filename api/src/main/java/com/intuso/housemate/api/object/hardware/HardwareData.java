package com.intuso.housemate.api.object.hardware;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for a device
 */
public final class HardwareData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public HardwareData() {}

    public HardwareData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new HardwareData(getId(), getName(), getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof HardwareData))
            return false;
        HardwareData other = (HardwareData)o;
        if(!other.getChildData(Hardware.PROPERTIES_ID).equals(getChildData(Hardware.PROPERTIES_ID)))
            return false;
        return true;
    }
}
