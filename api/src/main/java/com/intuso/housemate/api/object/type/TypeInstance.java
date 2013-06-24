package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.comms.Message;

import java.util.Map;

/**
 * Representation of an instance of a type
 */
public class TypeInstance implements Message.Payload {

    private String value;
    private TypeInstances childValues;

    /**
     * Create an empty type instance
     */
    public TypeInstance() {
        this(null);
    }

    /**
     * Create a type instance with a value
     * @param value the value
     */
    public TypeInstance(String value) {
        this(value, new TypeInstances());
    }

    /**
     * Create a type instance with a value and predefined child values
     * @param value the value
     * @param childValues the child values
     */
    public TypeInstance(String value, TypeInstances childValues) {
        this.value = value;
        this.childValues = (childValues != null ? childValues : new TypeInstances());
    }

    /**
     * Gets the value
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the child values
     * @return the child values
     */
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
