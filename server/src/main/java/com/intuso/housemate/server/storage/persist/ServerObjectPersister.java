package com.intuso.housemate.server.storage.persist;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.application.Application;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.hardware.Hardware;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.realclient.storage.persist.DeviceListWatcher;
import com.intuso.housemate.realclient.storage.persist.HardwareListWatcher;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/11/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class ServerObjectPersister {

    private final ApplicationListWatcher applicationListWatcher;
    private final AutomationListWatcher automationListWatcher;
    private final DeviceListWatcher deviceListWatcher;
    private final HardwareListWatcher hardwareListWatcher;
    private final UserListWatcher userListWatcher;

    @Inject
    public ServerObjectPersister(ApplicationListWatcher applicationListWatcher, AutomationListWatcher automationListWatcher, DeviceListWatcher deviceListWatcher, HardwareListWatcher hardwareListWatcher, UserListWatcher userListWatcher) {
        this.applicationListWatcher = applicationListWatcher;
        this.automationListWatcher = automationListWatcher;
        this.deviceListWatcher = deviceListWatcher;
        this.hardwareListWatcher = hardwareListWatcher;
        this.userListWatcher = userListWatcher;
    }

    public void watchApplications(List<? extends Application<?, ?, ?, ?, ?>> applications) {
        applications.addObjectListener(applicationListWatcher, true);
    }

    public void watchAutomations(List<? extends Automation<?, ?, ?, ?, ?, ?,
            ? extends Condition<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
            ? extends Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?>> automations) {
        automations.addObjectListener(automationListWatcher, true);
    }

    public void watchDevices(List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> devices) {
        devices.addObjectListener(deviceListWatcher, true);
    }

    public void watchHardwares(List<? extends Hardware<?, ?>> hardwares) {
        hardwares.addObjectListener(hardwareListWatcher, true);
    }

    public void watchUsers(List<? extends User<?, ?>> users) {
        users.addObjectListener(userListWatcher);
    }

}
