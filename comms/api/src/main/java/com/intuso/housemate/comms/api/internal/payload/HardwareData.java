package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for a device
 */
public final class HardwareData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String RENAME_ID = "rename";
    public final static String NEW_NAME = "new-name";
    public final static String NAME_ID = "name";
    public final static String REMOVE_ID = "remove";
    public final static String START_ID = "start";
    public final static String STOP_ID = "stop";
    public final static String RUNNING_ID = "running";
    public final static String ERROR_ID = "error";
    public final static String DRIVER_ID = "driver";
    public final static String DRIVER_LOADED_ID = "driver-loaded";
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
