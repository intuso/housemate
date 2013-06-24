package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.type.Type;

/**
 * @param <TYPE> the type of the sub type's type
 */
public interface SubType<TYPE extends Type> extends BaseObject<SubTypeListener> {

    /**
     * Gets the sub types' type
     * @return the sub type's type
     */
    public abstract TYPE getType();
}
