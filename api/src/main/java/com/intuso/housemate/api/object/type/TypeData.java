package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Base data object for a type
 *
 * @param <WBL> the type of the sub wrappable objects
 */
public abstract class TypeData<WBL extends HousemateData<?>>
        extends HousemateData<WBL> {

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

    public int getMinValues() {
        return minValues;
    }

    public int getMaxValues() {
        return maxValues;
    }
}
