package com.intuso.housemate.object.api.internal;

/**
 * @param <VALUE> the type of the value
 * @param <COMMAND> the type of the command
 * @param <APPLICATION_INSTANCE> the type of the application
 */
public interface ApplicationInstance<VALUE extends Value<?, ?>,
        COMMAND extends Command<?, ?, ?, ?>,
        APPLICATION_INSTANCE extends ApplicationInstance<VALUE, COMMAND, APPLICATION_INSTANCE>>
        extends BaseHousemateObject<ApplicationInstance.Listener<? super APPLICATION_INSTANCE>> {

    COMMAND getAllowCommand();
    COMMAND getRejectCommand();
    VALUE getStatusValue();

    enum Status {
        Unregistered,
        Registering,
        Allowed,
        Pending,
        Rejected,
        Expired;
    }

    /**
     * Listener interface for application instances
     */
    interface Listener<APPLICATION_INSTANCE extends ApplicationInstance<?, ?, ?>> extends ObjectListener {}

    /**
     * Interface to show that the implementing object has a list of application instances
     */
    interface Container<APPLICATION_INSTANCES extends List<? extends ApplicationInstance<?, ?, ?>>> {

        /**
         * Gets the application instances list
         * @return the application instances list
         */
        APPLICATION_INSTANCES getApplicationInstances();
    }
}
