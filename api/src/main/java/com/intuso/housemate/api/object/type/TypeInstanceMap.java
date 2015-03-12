package com.intuso.housemate.api.object.type;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.comms.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Collection of type instances, mapped by type id
 */
public class TypeInstanceMap implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private Map<String, TypeInstances> children = Maps.newHashMap();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Map<String, TypeInstances> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TypeInstances> children) {
        this.children = children == null || children instanceof Serializable ? children : Maps.newHashMap(children);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstanceMap))
            return false;
        return children.equals(((TypeInstanceMap)o).children);
    }

    @Override
    public void ensureSerialisable() {
        if(children != null && !(children instanceof HashMap))
            children = Maps.newHashMap(children);
        if(children != null)
            for(TypeInstances child : children.values())
                if(child != null)
                    child.ensureSerialisable();
    }
}
