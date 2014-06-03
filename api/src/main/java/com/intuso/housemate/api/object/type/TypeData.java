package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Base data object for a type
 *
 * @param <CHILD_DATA> the type of the child data objects
 */
public abstract class TypeData<CHILD_DATA extends HousemateData<?>>
        extends HousemateData<CHILD_DATA> {

    private int minValues;
    private int maxValues;

    protected TypeData() {}

    /**
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     */
    public TypeData(String id, String name, String description, int minValues, int maxValues) {
        super(id, name, description);
        this.minValues = minValues;
        this.maxValues = maxValues;
    }

    /**
     * Get the minimum number of values an instance of this type should have
     * @return the minimum number of values an instance of this type should have
     */
    public int getMinValues() {
        return minValues;
    }

    public void setMinValues(int minValues) {
        this.minValues = minValues;
    }

    /**
     * Get the maximum number of values an instance of this type should have
     * @return the maximum number of values an instance of this type should have
     */
    public int getMaxValues() {
        return maxValues;
    }

    public void setMaxValues(int maxValues) {
        this.maxValues = maxValues;
    }
}
