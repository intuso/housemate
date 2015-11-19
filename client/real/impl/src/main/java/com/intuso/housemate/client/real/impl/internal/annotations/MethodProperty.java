package com.intuso.housemate.client.real.impl.internal.annotations;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Property implementation for annotated properties
 */
public class MethodProperty extends RealPropertyImpl<Object> {

    @Inject
    public MethodProperty(Log log,
                          ListenersFactory listenersFactory,
                          @Assisted("id") String id,
                          @Assisted("name") String name,
                          @Assisted("description") String description,
                          @Assisted RealTypeImpl<?, ?, Object> type,
                          @Nullable @Assisted("value") Object value,
                          @Assisted final Method method,
                          @Assisted("instance") final Object instance) {
        super(log, listenersFactory, id, name, description, type, value);
        method.setAccessible(true);
        addObjectListener(new Property.Listener<RealProperty<Object>>() {

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

    public interface Factory {
        MethodProperty create(@Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              RealTypeImpl<?, ?, Object> type,
                              @Nullable @Assisted("value") Object value,
                              Method method,
                              @Assisted("instance") Object instance);
    }
}
