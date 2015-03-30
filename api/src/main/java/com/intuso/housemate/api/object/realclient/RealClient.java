package com.intuso.housemate.api.object.realclient;

import com.intuso.housemate.api.object.BaseHousemateObject;
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
public interface RealClient<
            APPLICATIONS extends List<? extends Application<?, ?, ?, ?, ?>>,
            AUTOMATIONS extends List<? extends Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            DEVICES extends List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>,
            HARDWARES extends List<? extends Hardware<?, ?>>,
            TYPES extends List<? extends Type>,
            USERS extends List<? extends User<?, ?>>,
            COMMAND extends Command<?, ?, ?>,
            REAL_CLIENT extends RealClient<APPLICATIONS, AUTOMATIONS, DEVICES, HARDWARES, TYPES, USERS, COMMAND, REAL_CLIENT>>
        extends BaseHousemateObject<RealClientListener<? super REAL_CLIENT>>,
            HasTypes<TYPES>,
            HasHardwares<HARDWARES>,
            HasDevices<DEVICES>,
            HasAutomations<AUTOMATIONS>,
            HasApplications<APPLICATIONS>,
            HasUsers<USERS> {

    public COMMAND getAddAutomationCommand();
    public COMMAND getAddDeviceCommand();
    public COMMAND getAddHardwareCommand();
    public COMMAND getAddUserCommand();
}
