package com.intuso.housemate.broker.plugin.comparator;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonOperator;

/**
 */
public class IntegerComparators {

    private IntegerComparators() {}

    public static class Equals implements Comparator<Integer> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.Equals;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) throws HousemateException {
            return first != null && first.equals(second);
        }
    }
    
    public static class GreaterThan implements Comparator<Integer> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.GreaterThan;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) throws HousemateException {
            return first != null && first.compareTo(second) > 0;
        }
    }
    
    public static class GreaterThanOrEqual implements Comparator<Integer> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.GreaterThanOrEqual;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) throws HousemateException {
            return first != null && first.compareTo(second) >= 0;
        }
    }
    
    public static class LessThan implements Comparator<Integer> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.LessThan;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) throws HousemateException {
            return first != null && first.compareTo(second) < 0;
        }
    }
    
    public static class LessThanOrEqual implements Comparator<Integer> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.LessThanOrEqual;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public boolean compare(Integer first, Integer second) throws HousemateException {
            return first != null && first.compareTo(second) <= 0;
        }
    }
}
