package com.intuso.housemate.broker.object;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.factory.ConditionFactory;
import com.intuso.housemate.broker.factory.DeviceFactory;
import com.intuso.housemate.broker.factory.TaskFactory;
import com.intuso.housemate.broker.storage.Storage;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.real.*;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.log.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 */
public class LifecycleHandlerImpl implements LifecycleHandler {

    private final Log log;
    private final BrokerRealResources brokerRealResources;
    private final RealResources realResources;
    private final Storage storage;

    private final DeviceFactory deviceFactory;
    private final ConditionFactory conditionFactory;
    private final TaskFactory taskFactory;

    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

    private final ValueListener<Value<?, ?>> runningListener = new ValueListener<Value<?, ?>>() {

        @Override
        public void valueChanging(Value<?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(Value<?, ?> value) {
            try {
                storage.saveTypeInstances(value.getPath(), value.getTypeInstances());
            } catch(HousemateException e) {
                log.e("Failed to save running value of device");
                log.st(e);
            }
        }
    };

    @Inject
    public LifecycleHandlerImpl(Log log, BrokerRealResources brokerRealResources, RealResources realResources,
                                Storage storage, DeviceFactory deviceFactory, ConditionFactory conditionFactory,
                                TaskFactory taskFactory, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        this.log = log;
        this.brokerRealResources = brokerRealResources;
        this.realResources = realResources;
        this.storage = storage;
        this.deviceFactory = deviceFactory;
        this.conditionFactory = conditionFactory;
        this.taskFactory = taskFactory;
        this.types = types;
    }

    @Override
    public BrokerRealCommand createAddUserCommand(final BrokerRealList<UserData, BrokerRealUser> users) {
        return new BrokerRealCommand(brokerRealResources, Root.ADD_USER_ID, Root.ADD_USER_ID, "Add a new user", Arrays.<BrokerRealParameter<?>>asList(
                new BrokerRealParameter<String>(brokerRealResources, "username", "Username", "The username for the new user", new StringType(realResources)),
                new BrokerRealParameter<String>(brokerRealResources, "password", "Password", "The password for the new user", new StringType(realResources))
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                TypeInstanceMap toSave = new TypeInstanceMap();
                try {
                    toSave.put("password-hash", new TypeInstances(new TypeInstance(new String(
                            MessageDigest.getInstance("MD5").digest(values.get("password").getFirstValue().getBytes())))));
                } catch(NoSuchAlgorithmException e) {
                    throw new HousemateException("Unable to hash the password to save it securely");
                }
                toSave.put("id", values.get("username"));
                toSave.put("name", values.get("username"));
                toSave.put("description", values.get("username"));
                BrokerRealUser user = new BrokerRealUser(getResources(), toSave.get("id").getFirstValue(),
                        toSave.get("name").getFirstValue(), toSave.get("description").getFirstValue(), LifecycleHandlerImpl.this);
                users.add(user);
                storage.saveValues(users.getPath(), user.getId(), toSave);
            }
        };
    }

    @Override
    public BrokerRealCommand createRemoveUserCommand(final BrokerRealUser user) {
        return new BrokerRealCommand(brokerRealResources, User.REMOVE_COMMAND_ID, User.REMOVE_COMMAND_ID, "Remove the user", Lists.<BrokerRealParameter<?>>newArrayList()) {
                    @Override
                    public void perform(TypeInstanceMap values) throws HousemateException {
                        getResources().getRoot().getUsers().remove(user.getId());
                        storage.removeValues(user.getPath());
                    }
                };
    }

    @Override
    public RealCommand createAddDeviceCommand(final RealList<DeviceData, RealDevice> devices) {
        return deviceFactory.createAddDeviceCommand(Root.ADD_DEVICE_ID, Root.ADD_DEVICE_ID, "Add a new device",
                types, devices);
    }

    @Override
    public BrokerRealCommand createAddAutomationCommand(final BrokerRealList<AutomationData, BrokerRealAutomation> automations) {
        return new BrokerRealCommand(brokerRealResources, Root.ADD_AUTOMATION_ID, Root.ADD_AUTOMATION_ID, "Add a new automation", Arrays.<BrokerRealParameter<?>>asList(
                new BrokerRealParameter<String>(brokerRealResources, "name", "Name", "The name for the new automation", new StringType(realResources)),
                new BrokerRealParameter<String>(brokerRealResources, "description", "Description", "The description for the new automation", new StringType(realResources))
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                values.put("id", values.get("name")); // todo figure out a better way of getting an id
                BrokerRealAutomation automation = new BrokerRealAutomation(getResources(), values.get("id").getFirstValue(),
                        values.get("name").getFirstValue(), values.get("description").getFirstValue(), LifecycleHandlerImpl.this);
                automations.add(automation);
                storage.saveValues(automations.getPath(), automation.getId(), values);
                automation.getRunningValue().addObjectListener(runningListener);
            }
        };
    }

    @Override
    public void automationRemoved(String[] path) {
        try {
            storage.removeValues(path);
        } catch(HousemateException e) {
            log.e("Failed to remove stored details for automation " + Arrays.toString(path));
        }
    }

    @Override
    public BrokerRealCommand createAddConditionCommand(BrokerRealList<ConditionData, BrokerRealCondition> conditions,
                                                       BrokerRealConditionOwner owner) {
        return conditionFactory.createAddConditionCommand(Automation.ADD_CONDITION_ID, Automation.ADD_CONDITION_ID, "Add a new condition", owner, conditions);
    }

    @Override
    public BrokerRealCommand createAddSatisfiedTaskCommand(BrokerRealList<TaskData, BrokerRealTask> tasks,
                                                           BrokerRealTaskOwner owner) {
        return taskFactory.createAddTaskCommand(Automation.ADD_SATISFIED_TASK_ID, Automation.ADD_SATISFIED_TASK_ID, "Add a new satisfied task", owner, tasks);
    }

    @Override
    public BrokerRealCommand createAddUnsatisfiedTaskCommand(BrokerRealList<TaskData, BrokerRealTask> tasks,
                                                             BrokerRealTaskOwner owner) {
        return taskFactory.createAddTaskCommand(Automation.ADD_UNSATISFIED_TASK_ID, Automation.ADD_UNSATISFIED_TASK_ID, "Add a new unsatisfied task", owner, tasks);
    }
}
