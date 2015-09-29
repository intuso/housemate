package com.intuso.housemate.comms.api.bridge.v1_0;

import com.google.common.base.Function;
import com.intuso.housemate.comms.api.internal.ChildOverview;

/**
 * Created by tomc on 28/09/15.
 */
public class ChildOverviewMapper {

    private final Function<ChildOverview, com.intuso.housemate.comms.v1_0.api.ChildOverview> toV1_0Function = new Function<ChildOverview, com.intuso.housemate.comms.v1_0.api.ChildOverview>() {
        @Override
        public com.intuso.housemate.comms.v1_0.api.ChildOverview apply(ChildOverview childOverview) {
            return map(childOverview);
        }
    };

    private final Function<com.intuso.housemate.comms.v1_0.api.ChildOverview, ChildOverview> fromV1_0Function = new Function<com.intuso.housemate.comms.v1_0.api.ChildOverview, ChildOverview>() {
        @Override
        public ChildOverview apply(com.intuso.housemate.comms.v1_0.api.ChildOverview childOverview) {
            return map(childOverview);
        }
    };

    public Function<ChildOverview, com.intuso.housemate.comms.v1_0.api.ChildOverview> getToV1_0Function() {
        return toV1_0Function;
    }

    public Function<com.intuso.housemate.comms.v1_0.api.ChildOverview, ChildOverview> getFromV1_0Function() {
        return fromV1_0Function;
    }

    public ChildOverview map(com.intuso.housemate.comms.v1_0.api.ChildOverview childOverview) {
        if(childOverview == null)
            return null;
        return new ChildOverview(childOverview.getId(), childOverview.getName(), childOverview.getDescription());
    }

    public com.intuso.housemate.comms.v1_0.api.ChildOverview map(ChildOverview childOverview) {
        if(childOverview == null)
            return null;
        return new com.intuso.housemate.comms.v1_0.api.ChildOverview(childOverview.getId(), childOverview.getName(), childOverview.getDescription());
    }
}
