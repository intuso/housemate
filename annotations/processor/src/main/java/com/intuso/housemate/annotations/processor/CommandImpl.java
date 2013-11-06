package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealResources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Command implementation for annotated commands
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
    public void perform(TypeInstanceMap values) throws HousemateException {
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

        public Object[] convert(TypeInstanceMap values) {
            Object[] result = new Object[parameters.size()];
            for(int i = 0; i < result.length; i++) {
                TypeInstances typeInstances = values.get(parameters.get(i).getId());
                if(typeInstances == null || typeInstances.size() == 0)
                    result[i] = null;
                else
                    result[i] = parameters.get(i).getType().deserialise(typeInstances.get(0));
            }
            return result;
        }
    }
}
