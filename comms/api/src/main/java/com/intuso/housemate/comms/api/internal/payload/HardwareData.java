package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for a device
 */
public final class HardwareData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String PROPERTIES_ID = "properties";

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
        if(!other.getChildData(PROPERTIES_ID).equals(getChildData(PROPERTIES_ID)))
            return false;
        return true;
    }
}
