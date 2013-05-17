package com.intuso.housemate.core.object.value;

import com.intuso.housemate.core.object.BaseObject;
import com.intuso.housemate.core.object.type.Type;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/02/12
 * Time: 08:17
 * To change this template use File | Settings | File Templates.
 */
public interface Value<T extends Type, V extends Value<?, ?>>
        extends BaseObject<ValueListener<? super V>> {

    public final static String VALUE = "value";

    public T getType();
    public String getValue();
}
