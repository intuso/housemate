package com.intuso.housemate.client.api.internal.object;

public interface Server<
        COMMAND extends Command<?, ?, ?, ?>,
        AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        SYSTEMS extends List<? extends System<?, ?, ?, ?>, ?>,
        USERS extends List<? extends User<?, ?, ?, ?>, ?>,
        NODES extends List<? extends Node<?, ?, ?, ?>, ?>,
        SERVER extends Server<COMMAND, AUTOMATIONS, SYSTEMS, USERS, NODES, SERVER>>
        extends Object<Server.Listener<? super SERVER>>,
        System.Container<SYSTEMS>,
        Automation.Container<AUTOMATIONS>,
        User.Container<USERS>,
        Node.Container<NODES> {

    String AUTOMATIONS_ID = "automation";
    String SYSTEMS_ID = "system";
    String USERS_ID = "user";
    String NODES_ID = "node";
    String ADD_AUTOMATION_ID = "add-automation";
    String ADD_SYSTEM_ID = "add-system";
    String ADD_USER_ID = "add-user";

    COMMAND getAddAutomationCommand();
    COMMAND getAddSystemCommand();
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

        public final static String OBJECT_CLASS = "server";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }
}
