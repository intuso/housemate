package com.intuso.housemate.server.plugin.main.transformer;

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
public class FromString {

    private FromString() {}

    public static class ToInteger implements Transformer<String, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(String input) throws HousemateException {
            try {
                return new Integer(input);
            } catch(NumberFormatException e) {
                return null;
            }
        }
    }

    public static class ToDouble implements Transformer<String, Double> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public Double apply(String input) throws HousemateException {
            try {
                return new Double(input);
            } catch(NumberFormatException e) {
                return null;
            }
        }
    }

    public static class ToBoolean implements Transformer<String, Boolean> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Boolean.getId();
        }

        @Override
        public Boolean apply(String input) throws HousemateException {
            return input == null ? null : Boolean.valueOf(input);
        }
    }
}
