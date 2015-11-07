package com.intuso.housemate.server.plugin.main.comparator;

import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.plugin.api.internal.CommonComparators;

/**
 */
public class BooleanComparators {

    private BooleanComparators() {}

    public static class Equal implements CommonComparators.Equal<Boolean> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Boolean.getId();
        }

        @Override
        public boolean compare(Boolean first, Boolean second) {
            return first != null && first.equals(second);
        }
    }
}
