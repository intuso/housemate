package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for a command
 */
public final class ServerData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String RENAME_ID = "rename";
    public final static String NAME_ID = "name";
    public final static String NEW_NAME = "new-name";
    public final static String APPLICATIONS_ID = "applications";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String DEVICES_ID = "devices";
    public final static String HARDWARES_ID = "hardwares";
    public final static String TYPES_ID = "types";
    public final static String USERS_ID = "users";
    public final static String ADD_AUTOMATION_ID = "add-automation";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_HARDWARE_ID = "add-hardware";
    public final static String ADD_USER_ID = "add-user";

    public ServerData() {}

    public ServerData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new ServerData(getId(), getName(), getDescription());
    }
}
