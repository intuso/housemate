package com.intuso.housemate.server.plugin.main.operator;

import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.plugin.api.internal.CommonOperators;

/**
 */
public class DoubleOperators {

    private DoubleOperators() {}

    private static double getDouble(Double doubleObject) {
        return doubleObject == null ? 0 : doubleObject;
    }
    
    public static class Add implements CommonOperators.Add<Double, Double> {

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

    public static class Subtract implements CommonOperators.Subtract<Double, Double> {

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

    public static class Multiply implements CommonOperators.Multiply<Double, Double> {

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

    public static class Divide implements CommonOperators.Divide<Double, Double> {

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

    public static class Maximum implements CommonOperators.Maximum<Double, Double> {

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

    public static class Minimum implements CommonOperators.Minimum<Double, Double> {

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
