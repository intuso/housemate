package com.intuso.housemate.server.plugin.main.comparator;

import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.plugin.api.internal.CommonComparators;

/**
 */
public class IntegerComparators {

    private IntegerComparators() {}

    public static class Equal implements CommonComparators.Equal<Integer> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) {
            return first != null && first.equals(second);
        }
    }
    
    public static class GreaterThan implements CommonComparators.GreaterThan<Integer> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) {
            return first != null && first.compareTo(second) > 0;
        }
    }
    
    public static class GreaterThanOrEqual implements CommonComparators.GreaterThanOrEqual<Integer> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) {
            return first != null && first.compareTo(second) >= 0;
        }
    }
    
    public static class LessThan implements CommonComparators.LessThan<Integer> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) {
            return first != null && first.compareTo(second) < 0;
        }
    }
    
    public static class LessThanOrEqual implements CommonComparators.LessThanOrEqual<Integer> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) {
            return first != null && first.compareTo(second) <= 0;
        }
    }
}
