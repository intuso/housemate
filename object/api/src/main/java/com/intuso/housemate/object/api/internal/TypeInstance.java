package com.intuso.housemate.object.api.internal;

import java.util.Map;

/**
 * Representation of an instance of a type
 */
public class TypeInstance {

    private static final long serialVersionUID = -1L;

    private String value;
    private TypeInstanceMap childValues;

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
        this(value, new TypeInstanceMap());
    }

    /**
     * Create a type instance with a value and predefined child values
     * @param value the value
     * @param childValues the child values
     */
    public TypeInstance(String value, TypeInstanceMap childValues) {
        this.value = value;
        this.childValues = (childValues != null ? childValues : new TypeInstanceMap());
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
    public TypeInstanceMap getChildValues() {
        return childValues;
    }

    public void setChildValues(TypeInstanceMap childValues) {
        this.childValues = childValues;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstance))
            return false;
        TypeInstance other = (TypeInstance)o;
        if((value == null && other.value != null)
                || !value.equals(other.value))
            return false;
        if(childValues.getChildren().size() != other.childValues.getChildren().size())
            return false;
        for(Map.Entry<String, TypeInstances> entry : childValues.getChildren().entrySet())
            if(!other.childValues.getChildren().containsKey(entry.getKey()) || !entry.getValue().equals(other.childValues.getChildren().get(entry.getKey())))
                return false;
        return true;
    }

    @Override
    public String toString() {
        return value + " and " + childValues.getChildren().size() + " children";
    }
}
