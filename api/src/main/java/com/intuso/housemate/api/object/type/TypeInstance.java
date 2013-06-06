package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.comms.Message;

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

    private TypeInstance() {}

    public TypeInstance(String value) {
        this.value = value;
    }

    public TypeInstance(String value, TypeInstances childValues) {
        this.value = value;
        this.childValues = childValues;
    }

    public String getValue() {
        return value;
    }

    public TypeInstances getChildValues() {
        return childValues;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TypeInstance))
            return false;
        TypeInstance other = (TypeInstance)o;
        if(!(value == null && other.value == null)
                || (value != null && other.value != null && value.equals(other.value)))
            return false;
        return  (value == null && other.value == null)
                || (value != null && other.value != null && value.equals(other.value));
    }
}
