package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Sender;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.application.HasApplications;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.HasAutomations;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.HasDevices;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.hardware.HasHardwares;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.type.HasTypes;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.user.HasUsers;
import com.intuso.housemate.api.object.user.User;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * @param <ROOT> the type of the root
 */
public interface ObjectRoot<
            TYPES extends List<? extends Type>,
            HARDWARES extends List<? extends Hardware<?, ?>>,
            DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            APPLICATIONS extends List<? extends Application<?, ?, ?, ?, ?>>,
            USERS extends List<? extends User<?, ?>>,
            ADD_COMMAND extends Command<?, ?, ?>,
            ROOT extends ObjectRoot<TYPES, HARDWARES, DEVICES, AUTOMATIONS, APPLICATIONS, USERS, ADD_COMMAND, ROOT>>
        extends Root<ROOT>,
            Receiver<Message.Payload>,
            Sender,
            HasApplications<APPLICATIONS>,
            HasUsers<USERS>,
            HasHardwares<HARDWARES>,
            HasTypes<TYPES>,
            HasDevices<DEVICES>,
            HasAutomations<AUTOMATIONS> {

    public final static String APPLICATIONS_ID = "applications";
    public final static String USERS_ID = "users";
    public final static String HARDWARES_ID = "hardwares";
    public final static String TYPES_ID = "types";
    public final static String DEVICES_ID = "devices";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String ADD_USER_ID = "add-user";
    public final static String ADD_HARDWARE_ID = "add-hardware";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_AUTOMATION_ID = "add-automation";

    public final static String CLEAR_LOADED = "clear-loaded";

    public ADD_COMMAND getAddHardwareCommand();

    public ADD_COMMAND getAddDeviceCommand();

    public ADD_COMMAND getAddAutomationCommand();

    public ADD_COMMAND getAddUserCommand();

    /**
     * Add a listener for lifecycle updates about an object
     * @param path the path of the object
     * @param listener the listener
     * @return the listener registration
     */
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener);

    /**
     * Gets an object attached to this root
     * @param path the path of the object to get
     * @return the object at that path, or null if there isn't one
     */
    public HousemateObject<?, ?, ?, ?> getObject(String[] path);
}
