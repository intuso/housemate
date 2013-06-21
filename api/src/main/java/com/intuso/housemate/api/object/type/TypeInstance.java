package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.comms.Message;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/06/13
 * Time: 07:25
 * To change this template use File | Settings | File Templates.
 */
public class TypeInstance implements Message.Payload {

    private String value;
    private TypeInstances childValues;

    public TypeInstance() {
        this(null);
    }

    public TypeInstance(String value) {
        this(value, new TypeInstances());
    }

    public TypeInstance(String value, TypeInstances childValues) {
        this.value = value;
        this.childValues = (childValues != null ? childValues : new TypeInstances());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TypeInstances getChildValues() {
        return childValues;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstance))
            return false;
        TypeInstance other = (TypeInstance)o;
        if((value == null && other.value != null)
                || !value.equals(other.value))
            return false;
        if(childValues.size() != other.childValues.size())
            return false;
        for(Map.Entry<String, TypeInstance> entry : childValues.entrySet())
            if(!other.childValues.containsKey(entry.getKey()) || !entry.getValue().equals(other.childValues.get(entry.getKey())))
                return false;
        return true;
    }

    @Override
    public String toString() {
        return value + " and " + childValues.size() + " children";
    }
}
