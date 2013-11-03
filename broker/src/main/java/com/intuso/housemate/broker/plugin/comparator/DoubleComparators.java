package com.intuso.housemate.broker.plugin.comparator;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonType;

/**
 */
public class DoubleComparators {

    private DoubleComparators() {}

    public static class Equals implements Comparator<Double> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.Equals;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) throws HousemateException {
            return first != null && first.equals(second);
        }
    }
    
    public static class GreaterThan implements Comparator<Double> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.GreaterThan;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) throws HousemateException {
            return first != null && first.compareTo(second) > 0;
        }
    }
    
    public static class GreaterThanOrEqual implements Comparator<Double> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.GreaterThanOrEqual;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) throws HousemateException {
            return first != null && first.compareTo(second) >= 0;
        }
    }
    
    public static class LessThan implements Comparator<Double> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.LessThan;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) throws HousemateException {
            return first != null && first.compareTo(second) < 0;
        }
    }
    
    public static class LessThanOrEqual implements Comparator<Double> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.LessThanOrEqual;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public boolean compare(Double first, Double second) throws HousemateException {
            return first != null && first.compareTo(second) <= 0;
        }
    }
}
