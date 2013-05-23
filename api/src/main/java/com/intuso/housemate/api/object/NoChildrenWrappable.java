package com.intuso.housemate.api.object;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 20/02/13
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
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
