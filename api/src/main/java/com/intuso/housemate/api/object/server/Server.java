package com.intuso.housemate.api.object.server;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.Renameable;
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

/**
 */
public interface Server<
            APPLICATIONS extends List<? extends Application<?, ?, ?, ?, ?>>,
            AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            HARDWARES extends List<? extends Hardware<?, ?>>,
            TYPES extends List<? extends Type>,
            USERS extends List<? extends User<?, ?>>,
            COMMAND extends Command<?, ?, ?>,
            SERVER extends Server<APPLICATIONS, AUTOMATIONS, DEVICES, HARDWARES, TYPES, USERS, COMMAND, SERVER>>
        extends BaseHousemateObject<ServerListener<? super SERVER>>,
            HasTypes<TYPES>,
            HasHardwares<HARDWARES>,
            HasDevices<DEVICES>,
            HasAutomations<AUTOMATIONS>,
            HasApplications<APPLICATIONS>,
            HasUsers<USERS>,
            Renameable<COMMAND> {

    public final static String APPLICATIONS_ID = "applications";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String DEVICES_ID = "devices";
    public final static String HARDWARES_ID = "hardwares";
    public final static String TYPES_ID = "types";
    public final static String USERS_ID = "users";
    public final static String ADD_AUTOMATION_ID = "add-automation";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_HARDWARE_ID = "add-hardware";
    public final static String ADD_USER_ID = "add-user";

    public COMMAND getAddAutomationCommand();
    public COMMAND getAddDeviceCommand();
    public COMMAND getAddHardwareCommand();
    public COMMAND getAddUserCommand();
}
