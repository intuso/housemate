package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.object.broker.proxy.BrokerProxyPrimaryObject;
import com.intuso.housemate.object.broker.real.BrokerRealAutomation;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;

/**
 */
public interface LifecycleHandler {
    BrokerRealCommand createAddUserCommand(BrokerRealList<UserWrappable, BrokerRealUser> users);
    BrokerRealCommand createRemoveUserCommand(BrokerRealUser user);
    RealCommand createAddDeviceCommand(RealList<DeviceWrappable, RealDevice> devices);
    BrokerRealCommand createAddAutomationCommand(BrokerRealList<AutomationWrappable, BrokerRealAutomation> automations);
    void automationRemoved(String[] path);
    <PO extends BrokerProxyPrimaryObject<?, ?, ?>> BrokerRealCommand createRemovePrimaryObjectCommand(
            Command<?, ?> originalCommand, PO source, BrokerProxyPrimaryObject.Remover<PO> remover);
    BrokerRealCommand createAddConditionCommand(BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions);
    BrokerRealCommand createAddSatisfiedTaskCommand(BrokerRealList<TaskWrappable, BrokerRealTask> tasks);
    BrokerRealCommand createAddUnsatisfiedTaskCommand(BrokerRealList<TaskWrappable, BrokerRealTask> tasks);
}
