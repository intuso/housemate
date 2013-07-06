package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 * Base data object for a value
 */
public abstract class ValueBaseData<WBL extends HousemateData<?>>
        extends HousemateData<WBL> {
    
    private String type;
    private TypeInstances values;

    protected ValueBaseData() {}

    public ValueBaseData(String id, String name, String description, String type, TypeInstances values) {
        super(id, name,  description);
        this.type = type;
        this.values = values;
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
    public TypeInstances getValues() {
        return values;
    }

    /**
     * Sets the current value
     * @param values the new value
     */
    public void setValues(TypeInstances values) {
        this.values = values;
    }
}
