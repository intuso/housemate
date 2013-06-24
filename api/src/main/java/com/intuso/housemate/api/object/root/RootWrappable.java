package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 *
 * Data object for a root
 */
public final class RootWrappable extends HousemateObjectWrappable<HousemateObjectWrappable<?>> {

    public RootWrappable() {
        super("", "Root", "Root");
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new RootWrappable();
    }
}
