package com.intuso.housemate.object.api.internal;

/**
 * Created by tomc on 30/01/15.
 */
public interface Renameable<RENAME_COMMAND extends Command<?, ?, ?, ?>> {

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
        void renamed(RENAMEABLE renameable, String oldName, String newName);
    }
}
