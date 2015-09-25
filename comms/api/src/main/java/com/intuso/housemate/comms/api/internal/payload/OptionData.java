package com.intuso.housemate.comms.api.internal.payload;

/**
 *
 * Data object for an option
 */
public final class OptionData extends HousemateData<ListData<SubTypeData>> {

    private static final long serialVersionUID = -1L;

    public final static String SUB_TYPES_ID = "sub-types";

    public OptionData() {}

    public OptionData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new OptionData(getId(), getName(), getDescription());
    }
}
