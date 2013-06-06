package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealArgument;
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
    private final ArgumentConverter argumentConverter;

    protected CommandImpl(RealResources resources, String id, String name, String description, List<RealArgument<?>> arguments, Method method, Object instance) {
        super(resources, id, name, description, arguments);
        this.method = method;
        this.instance = instance;
        argumentConverter = new ArgumentConverter(arguments);
    }

    @Override
    public void perform(TypeInstances values) throws HousemateException {
        try {
            method.invoke(instance, argumentConverter.convert(values));
        } catch(InvocationTargetException e) {
            throw new HousemateException("Failed to perform command", e);
        } catch(IllegalAccessException e) {
            throw new HousemateException("Failed to perform command", e);
        }
    }

    private final class ArgumentConverter {

        private final List<RealArgument<?>> arguments;

        private ArgumentConverter(List<RealArgument<?>> arguments) {
            this.arguments = arguments;
        }

        public Object[] convert(TypeInstances values) {
            Object[] result = new Object[arguments.size()];
            for(int i = 0; i < result.length; i++) {
                TypeInstance value = values.get(arguments.get(i).getId());
                if(value == null)
                    result[i] = null;
                else
                    result[i] = arguments.get(i).getType().deserialise(value);
            }
            return result;
        }
    }
}
