package com.intuso.housemate.api.object.parameter;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.type.Type;

/**
 * @param <TYPE> the type of the parameter's type
 */
public interface Parameter<TYPE extends Type> extends BaseHousemateObject<ParameterListener> {

    /**
     * Gets the type id of the parameter
     * @return the type id of the parameter
     */
    public String getTypeId();

    /**
     * Gets the type of the parameter
     * @return the type of the parameter
     */
    public TYPE getType();
}
