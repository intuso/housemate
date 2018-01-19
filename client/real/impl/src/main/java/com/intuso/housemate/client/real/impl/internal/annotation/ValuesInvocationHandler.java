package com.intuso.housemate.client.real.impl.internal.annotation;

import com.google.common.collect.Sets;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Method invoker for value updates
 */
public class ValuesInvocationHandler implements InvocationHandler {

    private final static Set<Method> PASS_THROUGH_METHODS = Sets.newHashSet();
    static {
        try {
            PASS_THROUGH_METHODS.add(Object.class.getMethod("equals", Object.class));
            PASS_THROUGH_METHODS.add(Object.class.getMethod("hashCode"));
        } catch(NoSuchMethodException e) {
            System.err.println("Problem accessing equals and hashCode methods reflectively");
            e.printStackTrace();
        }
    }

    private final Map<Method, RealValueImpl<?>> values;

    public ValuesInvocationHandler(Map<Method, RealValueImpl<?>> values) {
        this.values = values;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if(PASS_THROUGH_METHODS.contains(method))
            return method.invoke(ValuesInvocationHandler.this, objects);
        else {
            RealValueImpl value = values.get(method);
            if (value == null)
                throw new HousemateException("Could not find value instance for annotated method " + method.getName());
            value.setValue(objects[0]);
            return null;
        }
    }
}
