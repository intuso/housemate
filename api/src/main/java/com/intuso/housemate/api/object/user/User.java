package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.command.Command;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
public interface User<RC extends Command<?, ?>> extends BaseObject<UserListener> {

    public final static String REMOVE_COMMAND = "remove";

    public RC getRemoveCommand();
}
