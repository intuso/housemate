package com.intuso.housemate.plugin.api.internal.driver;

/**
 * Created by tomc on 08/12/16.
 */
public class PropertyDescription extends ObjectDescription {

    private int minValues;
    private int maxValues;

    public PropertyDescription() {}

    public PropertyDescription(String id, String name, String description, int minValues, int maxValues) {
        super(id, name, description);
        this.minValues = minValues;
        this.maxValues = maxValues;
    }

    public int getMinValues() {
        return minValues;
    }

    public void setMinValues(int minValues) {
        this.minValues = minValues;
    }

    public int getMaxValues() {
        return maxValues;
    }

    public void setMaxValues(int maxValues) {
        this.maxValues = maxValues;
    }
}
