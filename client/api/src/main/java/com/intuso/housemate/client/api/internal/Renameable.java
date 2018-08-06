package com.intuso.housemate.client.api.internal;

import com.intuso.housemate.client.api.internal.object.Command;

/**
 * Created by tomc on 30/01/15.
 */
public interface Renameable<RENAME_COMMAND extends Command<?, ?, ?, ?>> {

    String RENAME_ID = "rename";
    String NAME_ID = "name";
    String DESCRIPTION_ID = "description";

    /**
     * Gets the rename command
     * @return the rename command
     */
    RENAME_COMMAND getRenameCommand();

    /**
     * Listener interface for automations
     */
    interface Listener<RENAMEABLE extends Renameable<?>> {

        /**
         * Notifies that the primary object's error has changed
         * @param renameable the object that has been renamed
         * @param oldName the old name
         * @param newName the new name
         */
        void renamed(RENAMEABLE renameable, String oldName, String oldDescription, String newName, String newDescription);
    }
}
