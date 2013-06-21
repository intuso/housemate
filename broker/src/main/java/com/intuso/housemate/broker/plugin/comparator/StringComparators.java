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
public class StringComparators {

    private StringComparators() {}

    public static class Equals implements Comparator<String> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.Equals;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeWrappable.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.equals(second);
        }
    }
    
    public static class GreaterThan implements Comparator<String> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.GreaterThan;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeWrappable.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.compareTo(second) > 0;
        }
    }
    
    public static class GreaterThanOrEqual implements Comparator<String> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.GreaterThanOrEqual;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeWrappable.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.compareTo(second) >= 0;
        }
    }
    
    public static class LessThan implements Comparator<String> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.LessThan;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeWrappable.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.compareTo(second) < 0;
        }
    }
    
    public static class LessThanOrEqual implements Comparator<String> {

        @Override
        public ComparisonOperator getOperator() {
            return ComparisonOperator.Simple.LessThanOrEqual;
        }

        @Override
        public String getTypeId() {
            return SimpleTypeWrappable.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) throws HousemateException {
            return first != null && first.compareTo(second) <= 0;
        }
    }
}
