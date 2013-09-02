package com.intuso.housemate.api.object;

/**
 *
 * Dummy data object to be used as a generic type for infertile objects
 */
public final class NoChildrenData extends HousemateData<NoChildrenData> {
    private NoChildrenData() {
        super(null, null, null);
    }

    @Override
    public HousemateData clone() {
        return new NoChildrenData();
    }
}