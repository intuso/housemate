package com.intuso.housemate.client.real.impl.internal.annotations;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.housemate.client.real.impl.internal.RealCommandImpl;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Command implementation for annotated commands
 */
public class MethodCommand extends RealCommandImpl {

    private final Method method;
    private final Object instance;
    private final ParameterConverter parameterConverter;

    @Inject
    protected MethodCommand(Log log,
                            ListenersFactory listenersFactory,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            @Assisted List<RealParameter<?>> parameters,
                            @Assisted Method method,
                            @Assisted Object instance) {
        super(log, listenersFactory, id, name, description, parameters);
        this.method = method;
        this.instance = instance;
        parameterConverter = new ParameterConverter(parameters);
    }

    @Override
    public void perform(TypeInstanceMap values) {
        try {
            method.invoke(instance, parameterConverter.convert(values));
        } catch(InvocationTargetException|IllegalAccessException e) {
            throw new HousemateCommsException("Failed to perform command", e);
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
                TypeInstances typeInstances = values.getChildren().get(parameters.get(i).getId());
                if(typeInstances == null || typeInstances.getElements().size() == 0)
                    result[i] = null;
                else
                    result[i] = parameters.get(i).getType().deserialise(typeInstances.getElements().get(0));
            }
            return result;
        }
    }

    public interface Factory {
        MethodCommand create(@Assisted("id") String id, @Assisted("name") String name,
                             @Assisted("description") String description, List<RealParameter<?>> parameters,
                             Method method, Object instance);
    }
}
