package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;

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
    private TypeInstance value;

    protected ValueWrappableBase() {}

    public ValueWrappableBase(String id, String name, String description, String type, TypeInstance value) {
        super(id, name,  description);
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public TypeInstance getValue() {
        return value;
    }

    public void setValue(TypeInstance value) {
        this.value = value;
    }
}
