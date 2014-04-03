package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for a command
 */
public final class CommandData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    private CommandData() {}

    public CommandData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new CommandData(getId(), getName(), getDescription());
    }
}
