package com.intuso.housemate.object.real.annotations;

import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Property implementation for annotated properties
 */
public class MethodPropertyImpl extends RealProperty<Object> {

    public MethodPropertyImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                              RealType<?, ?, Object> type, Object value, final Method method, final Object instance) {
        super(log, listenersFactory, id, name, description, type, value);
        addObjectListener(new ValueListener<RealProperty<Object>>() {

            @Override
            public void valueChanging(RealProperty<Object> value) {
                // do nothing
            }

            @Override
            public void valueChanged(RealProperty<Object> property) {
                try {
                    method.invoke(instance, property.getTypedValue());
                } catch(IllegalAccessException|InvocationTargetException e) {
                    getLog().e("Failed to update property for annotated property " + getId(), e);
                }
            }
        });
    }
}
