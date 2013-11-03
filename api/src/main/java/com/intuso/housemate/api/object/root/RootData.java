package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.object.HousemateData;

/**
 *
 * Data object for a root
 */
public final class RootData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public RootData() {
        super("", "Root", "Root");
    }

    @Override
    public HousemateData clone() {
        return new RootData();
    }
}
