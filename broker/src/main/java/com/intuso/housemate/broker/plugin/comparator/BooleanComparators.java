package com.intuso.housemate.broker.plugin.comparator;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonOperator;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 19/06/13
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
public class BooleanComparators {

    private BooleanComparators() {}

    public static class Equals implements Comparator<Boolean> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.Equals;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeWrappable.Type.Boolean.getId();
        }

        @Override
        public boolean compare(Boolean first, Boolean second) throws HousemateException {
            return first != null && first.equals(second);
        }
    }
}
