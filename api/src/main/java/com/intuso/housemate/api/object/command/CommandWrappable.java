package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;

/**
 *
 * Data object for a command
 */
public final class CommandWrappable extends HousemateObjectWrappable<ListWrappable<ParameterWrappable>> {

    private CommandWrappable() {}

    public CommandWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new CommandWrappable(getId(), getName(), getDescription());
    }
}
