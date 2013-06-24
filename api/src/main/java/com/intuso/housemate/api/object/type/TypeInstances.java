package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.comms.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Collection of type instance, mapped by type id
 */
public class TypeInstances extends HashMap<String, TypeInstance> implements Message.Payload {
    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstances))
            return false;
        TypeInstances other = (TypeInstances)o;
        if(size() != other.size())
            return false;
        else if(!keySet().containsAll(other.keySet()))
            return false;
        else {
            for(Map.Entry<String, TypeInstance> entry : entrySet()) {
                if(!(entry.getValue() == null && other.get(entry.getKey()) == null)
                        || entry.getValue().equals(other.get(entry.getKey())))
                    return false;
            }
            return true;
        }
    }
}
