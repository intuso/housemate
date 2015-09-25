package com.intuso.housemate.server.plugin.main.operator;

import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.plugin.api.internal.OperationType;
import com.intuso.housemate.plugin.api.internal.Operator;

/**
 */
public class DoubleOperators {

    private DoubleOperators() {}

    private static double getDouble(Double doubleObject) {
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
        public Double apply(Double first, Double second) {
            return getDouble(first) + getDouble(second);
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
        public Double apply(Double first, Double second) {
            return getDouble(first) - getDouble(second);
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
        public Double apply(Double first, Double second) {
            return getDouble(first) * getDouble(second);
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
        public Double apply(Double first, Double second) {
            return getDouble(first) / getDouble(second);
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
        public Double apply(Double first, Double second) {
            return Math.max(getDouble(first), getDouble(second));
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
        public Double apply(Double first, Double second) {
            return Math.min(getDouble(first), getDouble(second));
        }
    }
}
