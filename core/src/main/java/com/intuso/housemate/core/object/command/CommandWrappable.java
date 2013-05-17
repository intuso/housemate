package com.intuso.housemate.core.object.command;

import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.core.object.list.ListWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 05/07/12
 * Time: 20:00
 * To change this template use File | Settings | File Templates.
 */
public final class CommandWrappable extends HousemateObjectWrappable<ListWrappable<ArgumentWrappable>> {

    private CommandWrappable() {}

    public CommandWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new CommandWrappable(getId(), getName(), getDescription());
    }
}
