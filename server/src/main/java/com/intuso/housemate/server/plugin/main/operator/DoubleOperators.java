package com.intuso.housemate.server.plugin.main.operator;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.plugin.api.OperationType;
import com.intuso.housemate.plugin.api.Operator;

/**
 */
public class DoubleOperators {

    private DoubleOperators() {}

    private static double getInt(Double doubleObject) {
        return doubleObject == null ? 0 : doubleObject;
    }
    
    public static class Plus implements Operator<Double, Double> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Plus;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(Double first, Double second) throws HousemateException {
            return getInt(first) + getInt(second);
        }
    }

    public static class Minus implements Operator<Double, Double> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Minus;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(Double first, Double second) throws HousemateException {
            return getInt(first) - getInt(second);
        }
    }

    public static class Times implements Operator<Double, Double> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Times;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(Double first, Double second) throws HousemateException {
            return getInt(first) * getInt(second);
        }
    }

    public static class Divide implements Operator<Double, Double> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Divide;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(Double first, Double second) throws HousemateException {
            return getInt(first) / getInt(second);
        }
    }

    public static class Max implements Operator<Double, Double> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Max;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(Double first, Double second) throws HousemateException {
            return Math.max(getInt(first), getInt(second));
        }
    }

    public static class Min implements Operator<Double, Double> {

        @Override
        public OperationType getOperationType() {
            return OperationType.Simple.Min;
        }

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(Double first, Double second) throws HousemateException {
            return Math.min(getInt(first), getInt(second));
        }
    }
}
