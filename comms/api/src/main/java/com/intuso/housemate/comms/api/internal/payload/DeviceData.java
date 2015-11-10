package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for a device
 */
public final class DeviceData extends HousemateData<HousemateData<?>> {

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
    public final static String FEATURES_ID = "features";

    public DeviceData() {}

    public DeviceData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new DeviceData(getId(), getName(), getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DeviceData))
            return false;
        DeviceData other = (DeviceData)o;
        if(!other.getChildData(PROPERTIES_ID).equals(getChildData(PROPERTIES_ID)))
            return false;
        if(!other.getChildData(FEATURES_ID).equals(getChildData(FEATURES_ID)))
            return false;
        return true;
    }
}