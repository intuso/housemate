package com.intuso.housemate.server.plugin.main.transformer;

import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.plugin.api.internal.Transformer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 16/10/13
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class FromBoolean {

    private FromBoolean() {}

    public static class ToInteger implements Transformer<Boolean, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Boolean.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Boolean input) {
            return input == null ? null : (input ? 1 : 0);
        }
    }

    public static class ToDouble implements Transformer<Boolean, Double> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Boolean.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(Boolean input) {
            return input == null ? null : (input ? 1.0 : 0.0);
        }
    }

    public static class ToString implements Transformer<Boolean, String> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Boolean.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public String apply(Boolean input) {
            return input == null ? null : input.toString();
        }
    }
}
