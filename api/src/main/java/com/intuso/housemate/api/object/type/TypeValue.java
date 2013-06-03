package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.comms.Message;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/06/13
 * Time: 07:25
 * To change this template use File | Settings | File Templates.
 */
public class TypeValue implements Message.Payload {

    private String value;
    private TypeValues childValues;

    private TypeValue() {}

    public TypeValue(String value) {
        this.value = value;
    }

    public TypeValue(String value, TypeValues childValues) {
        this.value = value;
        this.childValues = childValues;
    }

    public String getValue() {
        return value;
    }

    public TypeValues getChildValues() {
        return childValues;
    }
}
