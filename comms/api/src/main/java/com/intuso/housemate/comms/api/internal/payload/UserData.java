package com.intuso.housemate.comms.api.internal.payload;

/**
 *
 * Data object for a user
 */
public final class UserData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String REMOVE_ID = "remove";
    public final static String EMAIL_ID = "email";

    public UserData() {}

    public UserData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new UserData(getId(), getName(), getDescription());
    }
}
