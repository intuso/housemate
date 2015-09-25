package com.intuso.housemate.object.api.internal;

/**
 */
public interface Server<
            APPLICATIONS extends List<? extends Application<?, ?, ?, ?, ?>>,
            AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            HARDWARES extends List<? extends Hardware<?, ?>>,
            TYPES extends List<? extends Type>,
            USERS extends List<? extends User<?, ?>>,
            COMMAND extends Command<?, ?, ?, ?>,
            SERVER extends Server>
        extends BaseHousemateObject<Server.Listener<? super SERVER>>,
        Type.Container<TYPES>,
            Hardware.Container<HARDWARES>,
            Device.Container<DEVICES>,
            Automation.Container<AUTOMATIONS>,
            Application.Container<APPLICATIONS>,
        User.Container<USERS>,
            Renameable<COMMAND> {

    COMMAND getAddAutomationCommand();
    COMMAND getAddDeviceCommand();
    COMMAND getAddHardwareCommand();
    COMMAND getAddUserCommand();

    /**
     *
     * Listener interface for server
     */
    interface Listener<SERVER extends Server> extends ObjectListener {}

    /**
     *
     * Interface to show that the implementing object has a list of server
     */
    interface Container<SERVERS extends List<? extends Server>> {

        /**
         * Gets the commands list
         * @return the commands list
         */
        SERVERS getServers();
    }
}
