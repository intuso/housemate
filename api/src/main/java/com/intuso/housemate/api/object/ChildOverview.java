package com.intuso.housemate.api.object;

import com.intuso.housemate.api.comms.Message;

/**
 * Container class for basic information about a child object
 */
public class ChildOverview implements Message.Payload {

    private String id, name, description;

    private ChildOverview() {}

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

    /**
     * Get the child object's name
     * @return the child object's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the child object's description
     * @return the child object's description
     */
    public String getDescription() {
        return description;
    }
}
