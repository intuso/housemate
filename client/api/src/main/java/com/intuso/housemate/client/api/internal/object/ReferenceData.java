package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.v1_0.api.object.Object;

public class ReferenceData extends Object.Data {

    private static final long serialVersionUID = -1L;

    public final static String OBJECT_CLASS = "reference";

    private String path;

    public ReferenceData() {}

    public ReferenceData(String id, String name, String description, String path) {
        super(OBJECT_CLASS, id, name, description);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
