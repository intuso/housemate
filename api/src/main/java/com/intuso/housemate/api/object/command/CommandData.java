package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.list.ListData;

/**
 * Data object for a command
 */
public final class CommandData extends HousemateData<ListData<ParameterData>> {

    private CommandData() {}

    public CommandData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new CommandData(getId(), getName(), getDescription());
    }
}
