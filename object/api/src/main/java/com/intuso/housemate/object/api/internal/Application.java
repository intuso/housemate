package com.intuso.housemate.object.api.internal;

/**
 * @param <APPLICATION_INSTANCE> the type of the application instances
 * @param <APPLICATION_INSTANCES> the type of the application instances list
 * @param <APPLICATION> the type of the application
 */
public interface Application<VALUE extends Value<?, ?>,
        COMMAND extends Command<?, ?, ?, ?>,
        APPLICATION_INSTANCE extends ApplicationInstance<?, ?, ?>,
        APPLICATION_INSTANCES extends List<? extends ApplicationInstance<?, ?, ?>>,
        APPLICATION extends Application<VALUE, COMMAND, APPLICATION_INSTANCE, APPLICATION_INSTANCES, APPLICATION>>
        extends BaseHousemateObject<Application.Listener<? super APPLICATION>>,
        ApplicationInstance.Container<APPLICATION_INSTANCES> {

    COMMAND getAllowCommand();
    COMMAND getSomeCommand();
    COMMAND getRejectCommand();
    VALUE getStatusValue();

    enum Status {
        Unregistered,
        AllowInstances,
        SomeInstances,
        RejectInstances,
        Expired;
    }

    /**
     * Listener interface for applications
     */
    interface Listener<APPLICATION extends Application<?, ?, ?, ?, ?>> extends ObjectListener {}

    /**
     * Interface to show that the implementing object has a list of applications
     */
    interface Container<APPLICATIONS extends Iterable<? extends Application<?, ?, ?, ?, ?>>> {

        /**
         * Gets the application list
         * @return the application list
         */
        APPLICATIONS getApplications();
    }
}
