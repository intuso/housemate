package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 * Base data object for a value
 */
public abstract class ValueBaseData<WBL extends HousemateData<?>>
        extends HousemateData<WBL> {
    
    private String type;
    private TypeInstances typeInstances;

    protected ValueBaseData() {}

    public ValueBaseData(String id, String name, String description, String type, TypeInstances typeInstances) {
        super(id, name,  description);
        this.type = type;
        this.typeInstances = typeInstances;
    }

    /**
     * Gets the id of the type
     * @return the id of the type
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the current value
     * @return the current value
     */
    public TypeInstances getTypeInstances() {
        return typeInstances;
    }

    /**
     * Sets the current value
     * @param typeInstances the new value
     */
    public void setTypeInstances(TypeInstances typeInstances) {
        this.typeInstances = typeInstances;
    }

    @Override
    public void ensureSerialisable() {
        super.ensureSerialisable();
        if(typeInstances != null)
            typeInstances.ensureSerialisable();
    }
}
