package com.intuso.housemate.webserver.api.server.v1_0.model;

import com.intuso.housemate.client.v1_0.api.object.Object;

import java.io.Serializable;

public class DataUpdate implements Serializable {

    private static final long serialVersionUID = -1L;

    private String path;
    private Object.Data data;

    public DataUpdate() {}

    public DataUpdate(String path, Object.Data data) {
        this.path = path;
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object.Data getData() {
        return data;
    }

    public void setData(Object.Data data) {
        this.data = data;
    }
}
