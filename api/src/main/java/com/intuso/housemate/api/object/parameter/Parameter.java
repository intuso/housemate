package com.intuso.housemate.api.object.parameter;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.type.Type;

/**
 * @param <TYPE> the type of the parameter's type
 */
public interface Parameter<TYPE extends Type> extends BaseObject<ParameterListener> {

    /**
     * Gets the type of the parameter
     * @return the type of the parameter
     */
    public abstract TYPE getType();
}
