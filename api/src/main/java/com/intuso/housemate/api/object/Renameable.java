package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.command.Command;

/**
 * Created by tomc on 30/01/15.
 */
public interface Renameable<RENAME_COMMAND extends Command<?, ?, ?>> {

    public final static String RENAME_ID = "rename";
    public final static String NAME_ID = "name";

    /**
     * Gets the rename command
     * @return the rename command
     */
    public RENAME_COMMAND getRenameCommand();

    /**
     * Gets the name
     * @return the name
     */
    public String getName();
}
