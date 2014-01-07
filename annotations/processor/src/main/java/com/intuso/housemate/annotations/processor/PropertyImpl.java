package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.log.Log;

import java.lang.reflect.Field;

/**
 * Property implementation for annotated properties
 */
public class PropertyImpl extends RealProperty<Object> {

    public PropertyImpl(Log log, String id, String name, String description,
                        RealType<?, ?, Object> type, Object value, final Field field, final Object instance) {
        super(log, id, name, description, type, value);
        addObjectListener(new ValueListener<RealProperty<Object>>() {

            @Override
            public void valueChanging(RealProperty<Object> value) {
                // do nothing
            }

            @Override
            public void valueChanged(RealProperty<Object> value) {
                try {
                    field.set(instance, value.getTypedValue());
                } catch(IllegalAccessException e) {
                    getLog().e("Failed to update value for annotated property " + getId(), e);
                }
            }
        });
    }
}
