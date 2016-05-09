package com.intuso.housemate.client.api.internal.object;

public interface Server<
        COMMAND extends Command<?, ?, ?, ?>, AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        USERS extends List<? extends User<?, ?, ?, ?>, ?>,
        NODES extends List<? extends Node<?, ?, ?>, ?>,
        SERVER extends Server>
        extends Object<Server.Listener<? super SERVER>>,
        Device.Container<DEVICES>,
        Automation.Container<AUTOMATIONS>,
        User.Container<USERS>,
        Node.Container<NODES> {

    String AUTOMATIONS_ID = "automations";
    String DEVICES_ID = "devices";
    String USERS_ID = "users";
    String NODES_ID = "nodes";
    String ADD_AUTOMATION_ID = "add-automation";
    String ADD_DEVICE_ID = "add-device";
    String ADD_USER_ID = "add-user";

    COMMAND getAddAutomationCommand();
    COMMAND getAddDeviceCommand();
    COMMAND getAddUserCommand();

    /**
     *
     * Listener interface for server
     */
    interface Listener<SERVER extends Server> extends Object.Listener {}

    /**
     *
     * Interface to show that the implementing object has a list of server
     */
    interface Container<SERVERS extends List<? extends Server<?, ?, ?, ?, ?, ?>, ?>> {

        /**
         * Gets the commands list
         * @return the commands list
         */
        SERVERS getServers();
    }

    /**
     * Data object for a command
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String TYPE = "server";

        public Data() {}

        public Data(String id, String name, String description) {
            super(TYPE, id, name, description);
        }
    }
}
