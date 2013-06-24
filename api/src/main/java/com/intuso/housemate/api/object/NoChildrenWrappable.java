package com.intuso.housemate.api.object;

/**
 *
 * Dummy data object to be used as a generic type for infertile objects
 */
public final class NoChildrenWrappable extends HousemateObjectWrappable<NoChildrenWrappable> {
    private NoChildrenWrappable() {
        super(null, null, null);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new NoChildrenWrappable();
    }
}
