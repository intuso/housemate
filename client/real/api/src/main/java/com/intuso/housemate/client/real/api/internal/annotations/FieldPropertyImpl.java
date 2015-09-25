package com.intuso.housemate.client.real.api.internal.annotations;

import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.object.api.internal.Property;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.lang.reflect.Field;

/**
 * Property implementation for annotated properties
 */
public class FieldPropertyImpl extends RealProperty<Object> {

    public FieldPropertyImpl(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                             RealType<?, ?, Object> type, Object value, final Field field, final Object instance) {
        super(log, listenersFactory, id, name, description, type, value);
        addObjectListener(new Property.Listener<RealProperty<Object>>() {

            @Override
            public void valueChanging(RealProperty<Object> value) {
                // do nothing
            }

            @Override
            public void valueChanged(RealProperty<Object> property) {
                try {
                    field.set(instance, property.getTypedValue());
                } catch(IllegalAccessException e) {
                    getLog().e("Failed to update property for annotated property " + getId(), e);
                }
            }
        });
    }
}
