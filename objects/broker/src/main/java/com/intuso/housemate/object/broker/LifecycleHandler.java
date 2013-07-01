package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.object.broker.real.BrokerRealAutomation;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;

public interface LifecycleHandler {

    /**
     * Creates a command for adding users
     * @param users the list to add the users to
     * @return a command that when performed (correctly) will add a user to the given list
     */
    BrokerRealCommand createAddUserCommand(BrokerRealList<UserWrappable, BrokerRealUser> users);

    /**
     * Creates a command to remove the user
     * @param user the user to remove when the command is performed
     * @return a command that when performed (correctly) will remove the given user
     */
    BrokerRealCommand createRemoveUserCommand(BrokerRealUser user);

    /**
     * Creates a command for adding devices
     * @param devices the list to add the devices to
     * @return a command that when performed (correctly) will add a device to the given list
     */
    RealCommand createAddDeviceCommand(RealList<DeviceWrappable, RealDevice> devices);

    /**
     * Creates a command for adding automations
     * @param automations list to add the automations to
     * @return a command that when performed (correctly) will add an automation to the given list
     */
    BrokerRealCommand createAddAutomationCommand(BrokerRealList<AutomationWrappable, BrokerRealAutomation> automations);

    /**
     * Notifies that an automation was removed
     * @param path the path of the automation that was removed
     */
    void automationRemoved(String[] path);

    /**
     * Creates a command for adding conditions
     * @param conditions the list to add the conditions to
     * @return a command that when performed (correctly) will add a condition to the given list
     */
    BrokerRealCommand createAddConditionCommand(BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions);

    /**
     * Creates a command for adding satisfied tasks
     * @param tasks the list to add the satisfied tasks to
     * @return a command that when performed (correctly) will add a satisfied task to the given list
     */
    BrokerRealCommand createAddSatisfiedTaskCommand(BrokerRealList<TaskWrappable, BrokerRealTask> tasks);

    /**
     * Creates a command for adding unsatisfied tasks
     * @param tasks the list to add the unsatisfied tasks to
     * @return a command that when performed (correctly) will add an unsatisfied task to the given list
     */
    BrokerRealCommand createAddUnsatisfiedTaskCommand(BrokerRealList<TaskWrappable, BrokerRealTask> tasks);
}
