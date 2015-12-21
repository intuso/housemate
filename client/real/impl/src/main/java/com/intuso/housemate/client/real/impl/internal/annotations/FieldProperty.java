package com.intuso.housemate.client.real.impl.internal.annotations;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.object.api.internal.Property;
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
                         @Assisted Logger logger,
                         @Assisted("id") String id,
                         @Assisted("name") String name,
                         @Assisted("description") String description,
                         @Assisted RealTypeImpl<?, ?, Object> type,
                         @Nullable @Assisted("value") Object value,
                         @Assisted final Field field,
                         @Assisted("instance") final Object instance) {
        super(logger, listenersFactory, id, name, description, type, value);
        field.setAccessible(true);
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
                    getLogger().error("Failed to update property for annotated property " + getId(), e);
                }
            }
        });
    }

    public interface Factory {
        FieldProperty create(Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             RealTypeImpl<?, ?, Object> type,
                             @Nullable @Assisted("value") Object value,
                             Field field,
                             @Assisted("instance") Object instance);
    }
}
