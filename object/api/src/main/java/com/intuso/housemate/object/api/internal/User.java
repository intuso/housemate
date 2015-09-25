package com.intuso.housemate.object.api.internal;

/**
 * @param <REMOVE_COMMAND> the type of the remove command
 */
public interface User<REMOVE_COMMAND extends Command<?, ?, ?, ?>, PROPERTY extends Property<?, ?, ?>>
        extends BaseHousemateObject<User.Listener>, Removeable<REMOVE_COMMAND> {

    PROPERTY getEmailProperty();

    /**
     *
     * Listener interface for users
     */
    interface Listener<USER extends User<?, ?>> extends ObjectListener {}

    /**
     *
     * Interface to show that the implementing object has a list of users
     */
    interface Container<USERS extends List<? extends User<?, ?>>> {

        /**
         * Gets the user list
         * @return the user list
         */
        USERS getUsers();
    }
}
