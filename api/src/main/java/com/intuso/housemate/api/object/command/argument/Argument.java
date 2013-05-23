package com.intuso.housemate.api.object.command.argument;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.type.Type;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/07/12
 * Time: 18:29
 * To change this template use File | Settings | File Templates.
 */
public interface Argument<T extends Type> extends BaseObject<ArgumentListener> {
    public abstract T getType();
}
