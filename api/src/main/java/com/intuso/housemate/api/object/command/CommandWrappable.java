package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.command.argument.ArgumentWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;

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
