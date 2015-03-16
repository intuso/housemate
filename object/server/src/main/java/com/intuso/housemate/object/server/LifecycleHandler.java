package com.intuso.housemate.object.server;

import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealHardware;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.server.real.*;

public interface LifecycleHandler {

    /**
     * Creates a command for adding users
     * @param users the list to add the users to
     * @return a command that when performed (correctly) will add a user to the given list
     */
    ServerRealCommand createAddUserCommand(ServerRealList<UserData, ServerRealUser> users);

    /**
     * Creates a command for adding hardware
     * @param hardwares the list to add the hardware to
     * @return a command that when performed (correctly) will add a hardware to the given list
     */
    RealCommand createAddHardwareCommand(RealList<HardwareData, RealHardware> hardwares);

    /**
     * Creates a command for adding devices
     * @param devices the list to add the devices to
     * @return a command that when performed (correctly) will add a device to the given list
     */
    RealCommand createAddDeviceCommand(RealList<DeviceData, RealDevice> devices);

    /**
     * Creates a command for adding automations
     * @param automations list to add the automations to
     * @return a command that when performed (correctly) will add an automation to the given list
     */
    ServerRealCommand createAddAutomationCommand(ServerRealList<AutomationData, ServerRealAutomation> automations);

    /**
     * Creates a command for adding conditions
     * @param conditions the list to add the conditions to
     * @return a command that when performed (correctly) will add a condition to the given list
     */
    ServerRealCommand createAddConditionCommand(ServerRealList<ConditionData, ServerRealCondition> conditions,
                                                ServerRealConditionOwner owner);

    /**
     * Creates a command for adding satisfied tasks
     * @param tasks the list to add the satisfied tasks to
     * @return a command that when performed (correctly) will add a satisfied task to the given list
     */
    ServerRealCommand createAddSatisfiedTaskCommand(ServerRealList<TaskData, ServerRealTask> tasks,
                                                    ServerRealTaskOwner owner);

    /**
     * Creates a command for adding unsatisfied tasks
     * @param tasks the list to add the unsatisfied tasks to
     * @return a command that when performed (correctly) will add an unsatisfied task to the given list
     */
    ServerRealCommand createAddUnsatisfiedTaskCommand(ServerRealList<TaskData, ServerRealTask> tasks,
                                                      ServerRealTaskOwner owner);
}
