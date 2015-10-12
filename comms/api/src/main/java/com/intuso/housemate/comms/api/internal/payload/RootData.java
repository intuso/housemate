package com.intuso.housemate.comms.api.internal.payload;

/**
 *
 * Data object for a root
 */
public final class RootData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String APPLICATION_INSTANCE_ID_TYPE = "application-instance-id";
    public final static String APPLICATION_STATUS_TYPE = "application-status";
    public final static String APPLICATION_INSTANCE_STATUS_TYPE = "application-instance-status";

    public RootData() {
        super("", "Root", "Root");
    }

    @Override
    public HousemateData clone() {
        return new RootData();
    }
}
