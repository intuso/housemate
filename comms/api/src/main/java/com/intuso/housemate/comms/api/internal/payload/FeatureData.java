package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for a device
 */
public final class FeatureData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String COMMANDS_ID = "commands";
    public final static String VALUES_ID = "values";

    public FeatureData() {}

    public FeatureData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new FeatureData(getId(), getName(), getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof FeatureData))
            return false;
        FeatureData other = (FeatureData)o;
        if(!other.getChildData(COMMANDS_ID).equals(getChildData(COMMANDS_ID)))
            return false;
        if(!other.getChildData(VALUES_ID).equals(getChildData(VALUES_ID)))
            return false;
        return true;
    }
}
