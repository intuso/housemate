package com.intuso.housemate.comms.api.internal.payload;

/**
 *
 * Dummy data object to be used as a generic type for infertile objects
 */
public final class NoChildrenData extends HousemateData<NoChildrenData> {

    private static final long serialVersionUID = -1L;

    private NoChildrenData() {
        super(null, null, null);
    }

    @Override
    public HousemateData clone() {
        return new NoChildrenData();
    }
}
