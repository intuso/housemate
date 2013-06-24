package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;

/**
 *
 * Base data object for a value
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

    /**
     * Gets the id of the type
     * @return the id of the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the current value
     * @return the current value
     */
    public TypeInstance getValue() {
        return value;
    }

    /**
     * Sets the current value
     * @param value the new value
     */
    public void setValue(TypeInstance value) {
        this.value = value;
    }
}
