package com.intuso.housemate.server.plugin.main.comparator;

import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.plugin.api.internal.CommonComparators;

/**
 */
public class DoubleComparators {

    private DoubleComparators() {}

    public static class Equal implements CommonComparators.Equal<Double> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) {
            return first != null && first.equals(second);
        }
    }
    
    public static class GreaterThan implements CommonComparators.GreaterThan<Double> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) {
            return first != null && first.compareTo(second) > 0;
        }
    }
    
    public static class GreaterThanOrEqual implements CommonComparators.GreaterThanOrEqual<Double> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) {
            return first != null && first.compareTo(second) >= 0;
        }
    }
    
    public static class LessThan implements CommonComparators.LessThan<Double> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) {
            return first != null && first.compareTo(second) < 0;
        }
    }
    
    public static class LessThanOrEqual implements CommonComparators.LessThanOrEqual<Double> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) {
            return first != null && first.compareTo(second) <= 0;
        }
    }
}
