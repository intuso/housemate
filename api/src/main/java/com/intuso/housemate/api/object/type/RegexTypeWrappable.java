package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 *
 * Data object for a regex type
 */
public final class RegexTypeWrappable extends TypeWrappable<NoChildrenWrappable> {

    private String regexPattern;

    private RegexTypeWrappable() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     * @param regexPattern the regex pattern that values of this type must match
     */
    public RegexTypeWrappable(String id, String name, String description, int minValues, int maxValues, String regexPattern) {
        super(id, name, description, minValues, maxValues);
        this.regexPattern = regexPattern;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new RegexTypeWrappable(getId(), getName(), getDescription(), getMinValues(), getMaxValues(), regexPattern);
    }

    /**
     * Gets the regex pattern
     * @return the regex pattern
     */
    public String getRegexPattern() {
        return regexPattern;
    }
}
