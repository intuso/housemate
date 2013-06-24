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

    public RegexTypeWrappable(String id, String name, String description, String regexPattern) {
        super(id, name, description);
        this.regexPattern = regexPattern;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new RegexTypeWrappable(getId(), getName(), getDescription(), regexPattern);
    }

    /**
     * Gets the regex pattern
     * @return the regex pattern
     */
    public String getRegexPattern() {
        return regexPattern;
    }
}
