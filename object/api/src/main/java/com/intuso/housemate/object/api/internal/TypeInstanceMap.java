package com.intuso.housemate.object.api.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection of type instances, mapped by type id
 */
public class TypeInstanceMap {

    private static final long serialVersionUID = -1L;

    private Map<String, TypeInstances> children = new HashMap<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Map<String, TypeInstances> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TypeInstances> children) {
        this.children = children == null || children instanceof Serializable ? children : new HashMap<>(children);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstanceMap))
            return false;
        return children.equals(((TypeInstanceMap)o).children);
    }
}
