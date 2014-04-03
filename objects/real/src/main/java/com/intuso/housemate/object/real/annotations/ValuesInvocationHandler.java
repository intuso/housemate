package com.intuso.housemate.object.real.annotations;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealValue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Method invoker for value updates
 */
public class ValuesInvocationHandler implements InvocationHandler {

    private final Map<Method, RealValue<Object>> values;

    public ValuesInvocationHandler(Map<Method, RealValue<Object>> values) {
        this.values = values;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        RealValue<Object> value = values.get(method);
        if(value == null)
            throw new HousemateException("Could not find value instance for annotated method " + method.getName());
        value.setTypedValues(objects[0]);
        return null;
    }
}
