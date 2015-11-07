package com.intuso.housemate.server.plugin.main.comparator;

import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.plugin.api.internal.CommonComparators;

/**
 */
public class StringComparators {

    private StringComparators() {}

    public static class Equal implements CommonComparators.Equal<String> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) {
            return first != null && first.equals(second);
        }
    }
    
    public static class GreaterThan implements CommonComparators.GreaterThan<String> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) {
            return first != null && first.compareTo(second) > 0;
        }
    }
    
    public static class GreaterThanOrEqual implements CommonComparators.GreaterThanOrEqual<String> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) {
            return first != null && first.compareTo(second) >= 0;
        }
    }
    
    public static class LessThan implements CommonComparators.LessThan<String> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) {
            return first != null && first.compareTo(second) < 0;
        }
    }
    
    public static class LessThanOrEqual implements CommonComparators.LessThanOrEqual<String> {

        @Override
        public String getTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public boolean compare(String first, String second) {
            return first != null && first.compareTo(second) <= 0;
        }
    }
}
