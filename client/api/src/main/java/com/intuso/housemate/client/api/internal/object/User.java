package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;

/**
 * @param <REMOVE_COMMAND> the type of the remove command
 */
public interface User<RENAME_COMMAND extends Command<?, ?, ?, ?>,
        REMOVE_COMMAND extends Command<?, ?, ?, ?>,
        PROPERTY extends Property<?, ?, ?, ?>,
        USER extends User<RENAME_COMMAND, REMOVE_COMMAND, PROPERTY, USER>>
        extends Object<User.Listener<? super USER>>, Renameable<RENAME_COMMAND>, Removeable<REMOVE_COMMAND> {

    String EMAIL_ID = "email";

    PROPERTY getEmailProperty();

    /**
     *
     * Listener interface for users
     */
    interface Listener<USER extends User<?, ?, ?, ?>> extends Object.Listener, Renameable.Listener<USER> {}

    /**
     *
     * Interface to show that the implementing object has a list of users
     */
    interface Container<USERS extends Iterable<? extends User<?, ?, ?, ?>>> {

        /**
         * Gets the user list
         * @return the user list
         */
        USERS getUsers();
    }

    /**
     *
     * Data object for a user
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "user";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name,  description);
        }
    }
}
