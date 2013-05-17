package com.intuso.housemate.core.object.command.argument;

import com.intuso.housemate.core.object.BaseObject;
import com.intuso.housemate.core.object.type.Type;

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
