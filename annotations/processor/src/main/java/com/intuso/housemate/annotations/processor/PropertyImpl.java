package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/06/13
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class PropertyImpl extends RealProperty<Object> {

    public PropertyImpl(RealResources resources, String id, String name, String description,
                        RealType<?, ?, Object> type, Object value, final Field field, final Object instance) {
        super(resources, id, name, description, type, value);
        addObjectListener(new ValueListener<RealProperty<Object>>() {
            @Override
            public void valueChanged(RealProperty<Object> value) {
                try {
                    field.set(instance, value.getTypedValue());
                } catch(IllegalAccessException e) {
                    getResources().getLog().e("Failed to update value for annotated property " + getId());
                    getResources().getLog().st(e);
                }
            }
        });
    }
}
