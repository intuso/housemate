package com.intuso.housemate.comms.api.internal.payload;

/**
 * Data object for a parameter
 */
public final class ParameterData extends HousemateData<NoChildrenData> {

    private static final long serialVersionUID = -1L;

    private String type;

    public ParameterData() {}

    public ParameterData(String id, String name, String description, String type) {
        super(id, name, description);
        this.type = type;
    }

    @Override
    public HousemateData clone() {
        return new ParameterData(getId(), getName(), getDescription(), type);
    }

    /**
     * Gets the type id of the parameter
     * @return the type if of the parameter
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
