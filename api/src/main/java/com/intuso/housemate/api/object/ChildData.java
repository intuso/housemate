package com.intuso.housemate.api.object;

import com.intuso.housemate.api.comms.Message;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 09/09/13
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class ChildData implements Message.Payload {

    private String id, name, description;

    private ChildData() {}

    public ChildData(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
