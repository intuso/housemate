package com.intuso.housemate.broker.plugin.main.transformer;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.SimpleTypeData;
import com.intuso.housemate.plugin.api.Transformer;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 16/10/13
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class FromInteger {

    private FromInteger() {}

    public static class ToDouble implements Transformer<Integer, Double> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(Integer input) throws HousemateException {
            return input == null ? null : new Double(input);
        }
    }

    public static class ToString implements Transformer<Integer, String> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public String apply(Integer input) throws HousemateException {
            return input == null ? null : input.toString();
        }
    }

    public static class ToBoolean implements Transformer<Integer, Boolean> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Boolean.getId();
        }

        @Override
        public Boolean apply(Integer input) throws HousemateException {
            return input == null ? null : (input > 0 ? Boolean.TRUE : Boolean.FALSE);
        }
    }
}
