package com.intuso.housemate.client.real.impl.internal.annotation;

import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Command implementation for annotated commands
 */
public class MethodCommandPerformer implements RealCommandImpl.Performer {

    private final Method method;
    private final Object instance;
    private final ParameterConverter parameterConverter;

    @Inject
    protected MethodCommandPerformer(Method method, Object instance, List<RealParameterImpl<?>> parameters) {
        this.method = method;
        this.instance = instance;
        parameterConverter = new ParameterConverter(parameters);
        method.setAccessible(true);
    }

    @Override
    public void perform(Type.InstanceMap values) {
        try {
            method.invoke(instance, parameterConverter.convert(values));
        } catch(InvocationTargetException|IllegalAccessException e) {
            throw new HousemateException("Failed to perform command", e);
        }
    }

    private final class ParameterConverter {

        private final List<RealParameterImpl<?>> parameters;

        private ParameterConverter(List<RealParameterImpl<?>> parameters) {
            this.parameters = parameters;
        }

        public Object[] convert(Type.InstanceMap values) {
            Object[] result = new Object[parameters.size()];
            for(int i = 0; i < result.length; i++) {
                Type.Instances typeInstances = values.getChildren().get(parameters.get(i).getId());
                if(typeInstances == null || typeInstances.getElements().size() == 0)
                    result[i] = null;
                else
                    result[i] = parameters.get(i).getType().deserialise(typeInstances.getElements().get(0));
            }
            return result;
        }
    }
}
