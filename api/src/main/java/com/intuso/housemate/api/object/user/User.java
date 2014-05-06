package com.intuso.housemate.api.object.user;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.RemoveableObject;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.property.Property;

/**
 * @param <REMOVE_COMMAND> the type of the remove command
 */
public interface User<REMOVE_COMMAND extends Command<?, ?, ?>, PROPERTY extends Property<?, ?, ?>>
        extends BaseHousemateObject<UserListener>, RemoveableObject<REMOVE_COMMAND> {

    public final static String EMAIL_ID = "email";

    public PROPERTY getEmailProperty();
}
