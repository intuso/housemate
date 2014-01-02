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
public class FromDouble {

    private FromDouble() {}

    public static class ToInteger implements Transformer<Double, Integer> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Integer.getId();
        }

        @Override
        public Integer apply(Double input) throws HousemateException {
            return input == null ? null : new Integer((int)input.doubleValue());
        }
    }

    public static class ToString implements Transformer<Double, String> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.String.getId();
        }

        @Override
        public String apply(Double input) throws HousemateException {
            return input == null ? null : input.toString();
        }
    }

    public static class ToBoolean implements Transformer<Double, Boolean> {

        @Override
        public String getInputTypeId() {
            return SimpleTypeData.Type.Double.getId();
        }

        @Override
        public String getOutputTypeId() {
            return SimpleTypeData.Type.Boolean.getId();
        }

        @Override
        public Boolean apply(Double input) throws HousemateException {
            return input == null ? null : (input > 0 ? Boolean.TRUE : Boolean.FALSE);
        }
    }
}
