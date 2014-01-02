package com.intuso.housemate.broker.plugin.main.operator;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.plugin.api.OperationType;
import com.intuso.housemate.plugin.api.Operator;

/**
 */
public class IntegerOperators {

    private IntegerOperators() {}

    private static int getInt(Integer integer) {
        return integer == null ? 0 : integer;
    }

    public static class Plus implements Operator<Integer, Integer> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Plus;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) throws HousemateException {
            return getInt(first) + getInt(second);
        }
    }

    public static class Minus implements Operator<Integer, Integer> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Minus;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) throws HousemateException {
            return getInt(first) - getInt(second);
        }
    }

    public static class Times implements Operator<Integer, Integer> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Times;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) throws HousemateException {
            return getInt(first) * getInt(second);
        }
    }

    public static class Divide implements Operator<Integer, Integer> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Divide;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) throws HousemateException {
            return getInt(first) / getInt(second);
        }
    }

    public static class Max implements Operator<Integer, Integer> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Max;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) throws HousemateException {
            return Math.max(getInt(first), getInt(second));
        }
    }

    public static class Min implements Operator<Integer, Integer> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Min;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Integer first, Integer second) throws HousemateException {
            return Math.min(getInt(first), getInt(second));
        }
    }
}
