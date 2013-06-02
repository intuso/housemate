package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealValue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/06/13
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
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
        value.setTypedValue(objects[0]);
        return null;
    }
}
