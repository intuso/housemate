package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealResources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/06/13
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class CommandImpl extends RealCommand {

    private final Method method;
    private final Object instance;
    private final ParameterConverter parameterConverter;

    protected CommandImpl(RealResources resources, String id, String name, String description, List<RealParameter<?>> parameters, Method method, Object instance) {
        super(resources, id, name, description, parameters);
        this.method = method;
        this.instance = instance;
        parameterConverter = new ParameterConverter(parameters);
    }

    @Override
    public void perform(TypeInstances values) throws HousemateException {
        try {
            method.invoke(instance, parameterConverter.convert(values));
        } catch(InvocationTargetException e) {
            throw new HousemateException("Failed to perform command", e);
        } catch(IllegalAccessException e) {
            throw new HousemateException("Failed to perform command", e);
        }
    }

    private final class ParameterConverter {

        private final List<RealParameter<?>> parameters;

        private ParameterConverter(List<RealParameter<?>> parameters) {
            this.parameters = parameters;
        }

        public Object[] convert(TypeInstances values) {
            Object[] result = new Object[parameters.size()];
            for(int i = 0; i < result.length; i++) {
                TypeInstance value = values.get(parameters.get(i).getId());
                if(value == null)
                    result[i] = null;
                else
                    result[i] = parameters.get(i).getType().deserialise(value);
            }
            return result;
        }
    }
}
