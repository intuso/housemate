package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for a condition
 */
public final class ConditionData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String REMOVE_ID = "remove";
    public final static String SATISFIED_ID = "satisfied";
    public final static String ERROR_ID = "error";
    public final static String DRIVER_ID = "driver";
    public final static String DRIVER_LOADED_ID = "driver-loaded";
    public final static String PROPERTIES_ID = "properties";
    public final static String CONDITIONS_ID = "conditions";
    public final static String ADD_CONDITION_ID = "add-condition";

    public ConditionData() {}

    public ConditionData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new ConditionData(getId(), getName(), getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ConditionData))
            return false;
        ConditionData other = (ConditionData)o;
        if(!other.getChildData(PROPERTIES_ID).equals(getChildData(PROPERTIES_ID)))
            return false;
        return true;
    }
}
