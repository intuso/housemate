package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.comms.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Collection of type instances, mapped by type id
 */
public class TypeInstanceMap extends HashMap<String, TypeInstances> implements Message.Payload {
    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstanceMap))
            return false;
        TypeInstanceMap other = (TypeInstanceMap)o;
        if(size() != other.size())
            return false;
        else if(!keySet().containsAll(other.keySet()))
            return false;
        else {
            for(Map.Entry<String, TypeInstances> entry : entrySet()) {
                if(!(entry.getValue() == null && other.get(entry.getKey()) == null)
                        || entry.getValue().equals(other.get(entry.getKey())))
                    return false;
            }
            return true;
        }
    }
}
