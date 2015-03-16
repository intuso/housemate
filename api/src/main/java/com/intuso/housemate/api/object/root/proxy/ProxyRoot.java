package com.intuso.housemate.api.object.root.proxy;

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
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.HasTypes;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.user.HasUsers;
import com.intuso.housemate.api.object.user.User;

/**
 * @param <APPLICATIONS> the type of the applications list
 * @param <USERS> the type of the users list
 * @param <HARDWARES> the type of the hardwares list
 * @param <TYPES> the type of the types list
 * @param <DEVICES> the type of the devices list
 * @param <AUTOMATIONS> the type of the automations list
 * @param <ADD_COMMAND> the type of the add command
 * @param <ROOT> the type of the root
 */
public interface ProxyRoot<
            APPLICATIONS extends List<? extends Application<?, ?, ?, ?, ?>>,
            USERS extends List<? extends User<?, ?>>,
            HARDWARES extends List<? extends Hardware<?, ?>>,
            TYPES extends List<? extends Type>,
            DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            ADD_COMMAND extends Command<?, ?, ?>,
            ROOT extends ProxyRoot<APPLICATIONS, USERS, HARDWARES, TYPES, DEVICES, AUTOMATIONS, ADD_COMMAND, ROOT>>
        extends Root<ROOT>, HasApplications<APPLICATIONS>, HasUsers<USERS>, HasHardwares<HARDWARES>, HasTypes<TYPES>, HasDevices<DEVICES>, HasAutomations<AUTOMATIONS> {

    public final static String CLEAR_LOADED = "clear-loaded";

    /**
     * Gets the add user command
     * @return the add user command
     */
    public ADD_COMMAND getAddUserCommand();

    /**
     * Gets the add device command
     * @return the add device command
     */
    public ADD_COMMAND getAddHardwareCommand();

    /**
     * Gets the add device command
     * @return the add device command
     */
    public ADD_COMMAND getAddDeviceCommand();

    /**
     * Gets the add automation command
     * @return the add automation command
     */
    public ADD_COMMAND getAddAutomationCommand();
}
