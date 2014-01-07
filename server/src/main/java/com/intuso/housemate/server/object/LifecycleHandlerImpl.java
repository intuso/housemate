package com.intuso.housemate.server.object;

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
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.*;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.DeviceFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.log.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LifecycleHandlerImpl implements LifecycleHandler {

    private final Log log;
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
                log.e("Failed to save running value of device", e);
            }
        }
    };

    @Inject
    public LifecycleHandlerImpl(Log log,
                                Storage storage, DeviceFactory deviceFactory, ConditionFactory conditionFactory,
                                TaskFactory taskFactory, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        this.log = log;
        this.storage = storage;
        this.deviceFactory = deviceFactory;
        this.conditionFactory = conditionFactory;
        this.taskFactory = taskFactory;
        this.types = types;
    }

    @Override
    public ServerRealCommand createAddUserCommand(final ServerRealList<UserData, ServerRealUser> users) {
        return new ServerRealCommand(log, Root.ADD_USER_ID, Root.ADD_USER_ID, "Add a new user", Arrays.<ServerRealParameter<?>>asList(
                new ServerRealParameter<String>(log, "username", "Username", "The username for the new user", new StringType(log)),
                new ServerRealParameter<String>(log, "password", "Password", "The password for the new user", new StringType(log))
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
                ServerRealUser user = new ServerRealUser(log, toSave.get("id").getFirstValue(),
                        toSave.get("name").getFirstValue(), toSave.get("description").getFirstValue(), new ServerRealUserOwner() {
                    @Override
                    public void remove(ServerRealUser user) {
                        users.remove(user.getId());
                        try {
                            storage.removeValues(user.getPath());
                        } catch(HousemateException e) {
                            log.e("Failed to remove stored details for user " + Arrays.toString(user.getPath()));
                        }
                    }
                });
                users.add(user);
                storage.saveValues(users.getPath(), user.getId(), toSave);
            }
        };
    }

    @Override
    public RealCommand createAddDeviceCommand(final RealList<DeviceData, RealDevice> devices) {
        return deviceFactory.createAddDeviceCommand(Root.ADD_DEVICE_ID, Root.ADD_DEVICE_ID, "Add a new device",
                types, devices);
    }

    @Override
    public ServerRealCommand createAddAutomationCommand(final ServerRealList<AutomationData, ServerRealAutomation> automations) {
        return new ServerRealCommand(log, Root.ADD_AUTOMATION_ID, Root.ADD_AUTOMATION_ID, "Add a new automation", Arrays.<ServerRealParameter<?>>asList(
                new ServerRealParameter<String>(log, "name", "Name", "The name for the new automation", new StringType(log)),
                new ServerRealParameter<String>(log, "description", "Description", "The description for the new automation", new StringType(log))
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                values.put("id", values.get("name")); // todo figure out a better way of getting an id
                ServerRealAutomation automation = new ServerRealAutomation(log, values.get("id").getFirstValue(),
                        values.get("name").getFirstValue(), values.get("description").getFirstValue(),
                        new ServerRealAutomationOwner() {
                            @Override
                            public void remove(ServerRealAutomation automation) {
                                automations.remove(automation.getId());
                                try {
                                    storage.removeValues(automation.getPath());
                                } catch(HousemateException e) {
                                    log.e("Failed to remove stored details for automation " + Arrays.toString(automation.getPath()));
                                }
                            }
                        }, LifecycleHandlerImpl.this);
                automations.add(automation);
                storage.saveValues(automations.getPath(), automation.getId(), values);
                automation.getRunningValue().addObjectListener(runningListener);
            }
        };
    }

    @Override
    public ServerRealCommand createAddConditionCommand(ServerRealList<ConditionData, ServerRealCondition> conditions,
                                                       ServerRealConditionOwner owner) {
        return conditionFactory.createAddConditionCommand(Automation.ADD_CONDITION_ID, Automation.ADD_CONDITION_ID, "Add a new condition", owner, conditions);
    }

    @Override
    public ServerRealCommand createAddSatisfiedTaskCommand(ServerRealList<TaskData, ServerRealTask> tasks,
                                                           ServerRealTaskOwner owner) {
        return taskFactory.createAddTaskCommand(Automation.ADD_SATISFIED_TASK_ID, Automation.ADD_SATISFIED_TASK_ID, "Add a new satisfied task", owner, tasks);
    }

    @Override
    public ServerRealCommand createAddUnsatisfiedTaskCommand(ServerRealList<TaskData, ServerRealTask> tasks,
                                                             ServerRealTaskOwner owner) {
        return taskFactory.createAddTaskCommand(Automation.ADD_UNSATISFIED_TASK_ID, Automation.ADD_UNSATISFIED_TASK_ID, "Add a new unsatisfied task", owner, tasks);
    }
}
