package com.intuso.housemate.server.plugin.main.comparator;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonType;

/**
 */
public class StringComparators {

    private StringComparators() {}

    public static class Equals implements Comparator<String> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.Equals;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.equals(second);
        }
    }
    
    public static class GreaterThan implements Comparator<String> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.GreaterThan;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.compareTo(second) > 0;
        }
    }
    
    public static class GreaterThanOrEqual implements Comparator<String> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.GreaterThanOrEqual;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.compareTo(second) >= 0;
        }
    }
    
    public static class LessThan implements Comparator<String> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.LessThan;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.compareTo(second) < 0;
        }
    }
    
    public static class LessThanOrEqual implements Comparator<String> {

        @Override
        public ComparisonType getComparisonType() {
            return ComparisonType.Simple.LessThanOrEqual;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.compareTo(second) <= 0;
        }
    }
}
