package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 * @param <TYPE> the type of the value's type
 * @param <VALUE> the type of the value
 */
public interface Value<
            TYPE extends Type,
            VALUE extends Value<?, ?>>
        extends BaseHousemateObject<ValueListener<? super VALUE>> {

    public final static String VALUE_ID = "value";

    /**
     * Gets the value's type's id
     * @return the value's type's id
     */
    public String getTypeId();

    /**
     * Gets the value's value
     * @return the value's value
     */
    public TypeInstances getTypeInstances();
}
