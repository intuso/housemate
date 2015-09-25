package com.intuso.housemate.comms.api.internal.payload;

/**
 *
 * Data object for a regex type
 */
public final class RegexTypeData extends TypeData<NoChildrenData> {

    private static final long serialVersionUID = -1L;

    private String regexPattern;

    public RegexTypeData() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     * @param regexPattern the regex pattern that values of this type must match
     */
    public RegexTypeData(String id, String name, String description, int minValues, int maxValues, String regexPattern) {
        super(id, name, description, minValues, maxValues);
        this.regexPattern = regexPattern;
    }

    /**
     * Gets the regex pattern
     * @return the regex pattern
     */
    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    @Override
    public HousemateData clone() {
        return new RegexTypeData(getId(), getName(), getDescription(), getMinValues(), getMaxValues(), regexPattern);
    }
}
