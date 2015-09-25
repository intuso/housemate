package com.intuso.housemate.comms.api.internal;

/**
 * Container class for basic information about a child object
 */
public class ChildOverview implements Message.Payload {

    private String id, name, description;

    public ChildOverview() {}

    public ChildOverview(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Get the child object's id
     * @return the child object's id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the child object's name
     * @return the child object's name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the child object's description
     * @return the child object's description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void ensureSerialisable() {}
}
