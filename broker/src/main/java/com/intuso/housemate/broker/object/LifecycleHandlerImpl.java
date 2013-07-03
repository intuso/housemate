package com.intuso.housemate.broker.object;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.real.BrokerRealAutomation;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealParameter;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.impl.type.StringType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 */
public class LifecycleHandlerImpl implements LifecycleHandler {

    private final BrokerGeneralResources resources;

    private final ValueListener<Value<?, ?>> runningListener = new ValueListener<Value<?, ?>>() {

        @Override
        public void valueChanging(Value<?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(Value<?, ?> value) {
            try {
                resources.getStorage().saveTypeInstances(value.getPath(), value.getTypeInstances());
            } catch(HousemateException e) {
                resources.getLog().e("Failed to save running value of device");
                resources.getLog().st(e);
            }
        }
    };

    public LifecycleHandlerImpl(BrokerGeneralResources resources) {
        this.resources = resources;
    }

    @Override
    public BrokerRealCommand createAddUserCommand(final BrokerRealList<UserWrappable, BrokerRealUser> users) {
        return new BrokerRealCommand(resources.getRealResources(), Root.ADD_USER_ID, Root.ADD_USER_ID, "Add a new user", Arrays.<BrokerRealParameter<?>>asList(
                new BrokerRealParameter<String>(resources.getRealResources(), "username", "Username", "The username for the new user", new StringType(resources.getClientResources())),
                new BrokerRealParameter<String>(resources.getRealResources(), "password", "Password", "The password for the new user", new StringType(resources.getClientResources()))
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
                        toSave.get("name").getFirstValue(), toSave.get("description").getFirstValue());
                users.add(user);
                resources.getStorage().saveValues(users.getPath(), user.getId(), toSave);
            }
        };
    }

    @Override
    public BrokerRealCommand createRemoveUserCommand(final BrokerRealUser user) {
        return new BrokerRealCommand(resources.getRealResources(), User.REMOVE_COMMAND_ID, User.REMOVE_COMMAND_ID, "Remove the user", Lists.<BrokerRealParameter<?>>newArrayList()) {
                    @Override
                    public void perform(TypeInstanceMap values) throws HousemateException {
                        getResources().getRoot().getUsers().remove(user.getId());
                        resources.getStorage().removeValues(user.getPath());
                    }
                };
    }

    @Override
    public RealCommand createAddDeviceCommand(final RealList<DeviceWrappable, RealDevice> devices) {
        return resources.getDeviceFactory().createAddDeviceCommand(Root.ADD_DEVICE_ID, Root.ADD_DEVICE_ID, "Add a new device", devices);
    }

    @Override
    public BrokerRealCommand createAddAutomationCommand(final BrokerRealList<AutomationWrappable, BrokerRealAutomation> automations) {
        return new BrokerRealCommand(resources.getRealResources(), Root.ADD_AUTOMATION_ID, Root.ADD_AUTOMATION_ID, "Add a new automation", Arrays.<BrokerRealParameter<?>>asList(
                new BrokerRealParameter<String>(resources.getRealResources(), "name", "Name", "The name for the new automation", new StringType(resources.getClientResources())),
                new BrokerRealParameter<String>(resources.getRealResources(), "description", "Description", "The description for the new automation", new StringType(resources.getClientResources()))
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                values.put("id", values.get("name")); // todo figure out a better way of getting an id
                BrokerRealAutomation automation = new BrokerRealAutomation(getResources(), values.get("id").getFirstValue(),
                        values.get("name").getFirstValue(), values.get("description").getFirstValue());
                automations.add(automation);
                resources.getStorage().saveValues(automations.getPath(), automation.getId(), values);
                automation.getRunningValue().addObjectListener(runningListener);
            }
        };
    }

    @Override
    public void automationRemoved(String[] path) {
        try {
            resources.getStorage().removeValues(path);
        } catch(HousemateException e) {
            resources.getLog().e("Failed to remove stored details for automation " + Arrays.toString(path));
        }
    }

    @Override
    public BrokerRealCommand createAddConditionCommand(BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions) {
        return resources.getConditionFactory().createAddConditionCommand(Automation.ADD_CONDITION_ID, Automation.ADD_CONDITION_ID, "Add a new condition", conditions);
    }

    @Override
    public BrokerRealCommand createAddSatisfiedTaskCommand(BrokerRealList<TaskWrappable, BrokerRealTask> tasks) {
        return resources.getTaskFactory().createAddTaskCommand(Automation.ADD_SATISFIED_TASK_ID, Automation.ADD_SATISFIED_TASK_ID, "Add a new satisfied task", tasks);
    }

    @Override
    public BrokerRealCommand createAddUnsatisfiedTaskCommand(BrokerRealList<TaskWrappable, BrokerRealTask> tasks) {
        return resources.getTaskFactory().createAddTaskCommand(Automation.ADD_UNSATISFIED_TASK_ID, Automation.ADD_UNSATISFIED_TASK_ID, "Add a new unsatisfied task", tasks);
    }
}
