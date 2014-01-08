package com.intuso.housemate.plugin.api;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 08/01/14
 * Time: 08:42
 * To change this template use File | Settings | File Templates.
 */
public class TypeInfo {

    private final String id;
    private final String name;
    private final String description;

    public TypeInfo(String id, String name, String description) {
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
