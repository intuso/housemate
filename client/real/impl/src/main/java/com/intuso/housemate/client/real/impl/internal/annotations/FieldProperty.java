package com.intuso.housemate.client.real.impl.internal.annotations;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * Property implementation for annotated properties
 */
public class FieldProperty extends RealPropertyImpl<Object> {

    @Inject
    public FieldProperty(ListenersFactory listenersFactory,
                         final @Assisted Logger logger,
                         @Assisted Property.Data data,
                         @Assisted RealTypeImpl<Object> type,
                         @Nullable @Assisted("value") Object value,
                         @Assisted final Field field,
                         @Assisted("instance") final Object instance) {
        super(logger, data, listenersFactory, type, value);
        field.setAccessible(true);
        addObjectListener(new Property.Listener<RealPropertyImpl<Object>>() {

            @Override
            public void valueChanging(RealPropertyImpl<Object> value) {
                // do nothing
            }

            @Override
            public void valueChanged(RealPropertyImpl<Object> property) {
                try {
                    field.set(instance, property.getValue());
                } catch(IllegalAccessException e) {
                    logger.error("Failed to update property for annotated property " + getId(), e);
                }
            }
        });
    }

    public interface Factory {
        FieldProperty create(Logger logger,
                             Property.Data data,
                             RealTypeImpl<Object> type,
                             @Nullable @Assisted("value") Object value,
                             Field field,
                             @Assisted("instance") Object instance);
    }
}

