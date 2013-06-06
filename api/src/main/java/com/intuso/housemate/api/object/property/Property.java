package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:18
 * To change this template use File | Settings | File Templates.
 */
public interface Property<T extends Type, C extends Command<?, ?>, P extends Property<T, C, P>>
        extends Value<T, P> {

    public final static String SET_COMMAND = "set-command";
    public final static String VALUE_PARAM = "value";

    public void set(TypeInstance value, CommandListener<? super C> listener) throws HousemateException;
    public C getSetCommand();
}
