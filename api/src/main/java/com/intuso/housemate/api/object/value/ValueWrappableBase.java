package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:08
 * To change this template use File | Settings | File Templates.
 */
public abstract class ValueWrappableBase<WBL extends HousemateObjectWrappable<?>>
        extends HousemateObjectWrappable<WBL> {
    
    private String type;
    private String value;

    protected ValueWrappableBase() {}

    public ValueWrappableBase(String id, String name, String description, String type, String value) {
        super(id, name,  description);
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
