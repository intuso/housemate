package com.intuso.housemate.comms.api.internal;

import com.intuso.housemate.comms.api.internal.payload.HousemateData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Container class for loaded objects
 */
public class TreeData implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private String id;
    private HousemateData<?> data;
    private Map<String, TreeData> children;
    private Map<String, ChildOverview> childOverviews;

    public TreeData() {}

    public TreeData(String id, HousemateData<?> data, Map<String, TreeData> children, Map<String, ChildOverview> childOverviews) {
        this.id = id;
        this.data = data;
        this.children = children;
        this.childOverviews = childOverviews;
    }

    /**
     * Get the id of the loaded object
     * @return the id of the loaded object
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the object data
     * @return the object data
     */
    public HousemateData<?> getData() {
        return data;
    }

    public void setData(HousemateData<?> data) {
        this.data = data;
    }

    /**
     * Get the children object data
     * @return the children object data
     */
    public Map<String, TreeData> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TreeData> children) {
        this.children = children == null || children instanceof Serializable ? children : new HashMap<>(children);
    }

    /**
     * Get the overviews of other unloaded children
     * @return the overviews of other unloaded children
     */
    public Map<String, ChildOverview> getChildOverviews() {
        return childOverviews;
    }

    public void setChildOverviews(Map<String, ChildOverview> childOverviews) {
        this.childOverviews = childOverviews == null || childOverviews instanceof Serializable ? childOverviews : new HashMap<>(childOverviews);
    }

    @Override
    public void ensureSerialisable() {
        if(children != null && !(children instanceof HashMap))
            children = new HashMap<>(children);
        if(childOverviews != null && !(childOverviews instanceof HashMap))
            childOverviews = new HashMap<>(childOverviews);
        if(data != null)
            data.ensureSerialisable();
        if(children != null)
            for(TreeData child : children.values())
                child.ensureSerialisable();
    }
}
