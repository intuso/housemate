package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.type.Type;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public interface SubType<T extends Type> extends BaseObject<SubTypeListener> {
    public abstract T getType();
}
