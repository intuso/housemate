package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.type.Type;

/**
 * @param <TYPE> the type of the sub type's type
 */
public interface SubType<TYPE extends Type> extends BaseHousemateObject<SubTypeListener> {

    /**
     * Gets the sub types' type's id
     * @return the sub type's type's id
     */
    public String getTypeId();

    /**
     * Gets the sub types' type
     * @return the sub type's type
     */
    public TYPE getType();
}
