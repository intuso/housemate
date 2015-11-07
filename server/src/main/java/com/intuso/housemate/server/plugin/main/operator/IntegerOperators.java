package com.intuso.housemate.server.plugin.main.operator;

import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.plugin.api.internal.CommonOperators;

/**
 */
public class IntegerOperators {

    private IntegerOperators() {}

    private static int getInt(Integer integer) {
        return integer == null ? 0 : integer;
    }

    public static class Add implements CommonOperators.Add<Integer, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) {
            return getInt(first) + getInt(second);
        }
    }

    public static class Subtract implements CommonOperators.Subtract<Integer, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) {
            return getInt(first) - getInt(second);
        }
    }

    public static class Multiply implements CommonOperators.Multiply<Integer, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) {
            return getInt(first) * getInt(second);
        }
    }

    public static class Divide implements CommonOperators.Divide<Integer, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) {
            return getInt(first) / getInt(second);
        }
    }

    public static class Maximum implements CommonOperators.Maximum<Integer, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) {
            return Math.max(getInt(first), getInt(second));
        }
    }

    public static class Minimum implements CommonOperators.Minimum<Integer, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) {
            return Math.min(getInt(first), getInt(second));
        }
    }
}
