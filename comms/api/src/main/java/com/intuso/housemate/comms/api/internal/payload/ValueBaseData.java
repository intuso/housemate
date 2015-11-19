package com.intuso.housemate.comms.api.internal.payload;

import com.intuso.housemate.object.api.internal.TypeInstances;

/**
 * Base data object for a value
 */
public abstract class ValueBaseData<WBL extends HousemateData<?>>
        extends HousemateData<WBL> {

    private static final long serialVersionUID = -1L;

    public final static String VALUE_ID = "value";
    
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
            TypeData.TypeInstancesPayload.ensureSerialisable(typeInstances);
    }
}
