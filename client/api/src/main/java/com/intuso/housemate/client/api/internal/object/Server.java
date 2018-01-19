package com.intuso.housemate.client.api.internal.object;

import com.intuso.housemate.client.api.internal.object.view.ServerView;

public interface Server<
        COMMAND extends Command<?, ?, ?, ?>,
        AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        DEVICES extends List<? extends Reference<?, ? extends Device<?, ?, ?, ?, ?, ?, ?>, ?>, ?>,
        DEVICE_GROUPS extends List<? extends Device.Group<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        USERS extends List<? extends User<?, ?, ?, ?>, ?>,
        NODES extends List<? extends Node<?, ?, ?, ?>, ?>,
        SERVER extends Server<COMMAND, AUTOMATIONS, DEVICES, DEVICE_GROUPS, USERS, NODES, SERVER>>
        extends Object<Server.Data, Server.Listener<? super SERVER>, ServerView>,
        Device.Group.Container<DEVICE_GROUPS>,
        Automation.Container<AUTOMATIONS>,
        User.Container<USERS>,
        Node.Container<NODES> {

    String AUTOMATIONS_ID = "automations";
    String DEVICES_ID = "devices";
    String DEVICE_GROUPS_ID = "deviceGroups";
    String USERS_ID = "users";
    String NODES_ID = "nodes";
    String ADD_AUTOMATION_ID = "addAutomation";
    String ADD_DEVICE_GROUP_ID = "addDeviceGroup";
    String ADD_USER_ID = "addUser";

    COMMAND getAddAutomationCommand();
    DEVICES getDevices();
    COMMAND getAddDeviceGroupCommand();
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
    interface Container<SERVERS extends Iterable<? extends Server<?, ?, ?, ?, ?, ?, ?>>> {

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
