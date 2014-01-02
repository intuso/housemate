package com.intuso.housemate.broker.storage;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.broker.factory.ConditionFactory;
import com.intuso.housemate.broker.factory.TaskFactory;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.real.*;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.utilities.log.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/11/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class BrokerObjectLoader {

    private final Storage storage;
    private final Log log;
    private final BrokerRealResources brokerRealResources;
    private final ConditionFactory conditionFactory;
    private final TaskFactory taskFactory;
    private final RealList<DeviceData, RealDevice> devices;
    private final LifecycleHandler lifecycleHandler;

    @Inject
    public BrokerObjectLoader(Log log, BrokerRealResources brokerRealResources, Storage storage,
                              ConditionFactory conditionFactory, TaskFactory taskFactory,
                              RealList<DeviceData, RealDevice> devices, LifecycleHandler lifecycleHandler) {
        this.storage = storage;
        this.log = log;
        this.brokerRealResources = brokerRealResources;
        this.conditionFactory = conditionFactory;
        this.taskFactory = taskFactory;
        this.devices = devices;
        this.lifecycleHandler = lifecycleHandler;
    }

    public void loadObjects() {
        loadUsers();
        loadDevices(new String[]{"", Root.DEVICES_ID}, lifecycleHandler.createAddDeviceCommand(devices));
        loadAutomations();
    }

    private void loadUsers() {
        BrokerRealList<UserData, BrokerRealUser> realUsers = brokerRealResources.getRoot().getUsers();
        try {
            for(String key : storage.getValuesKeys(realUsers.getPath())) {
                TypeInstanceMap details = storage.getValues(realUsers.getPath(), key);
                BrokerRealUser user = new BrokerRealUser(brokerRealResources, details.get("id").getFirstValue(),
                        details.get("name").getFirstValue(), details.get("description").getFirstValue(), lifecycleHandler);
                realUsers.add(user);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realUsers.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users");
            log.st(e);
        }
        if(realUsers.getChildren().size() == 0) {
            TypeInstanceMap toSave = new TypeInstanceMap();
            try {
                toSave.put("password-hash", new TypeInstances(new TypeInstance(new String(
                        MessageDigest.getInstance("MD5").digest("admin".getBytes())))));
            } catch(NoSuchAlgorithmException e) {
                log.e("Unable to hash the password for the default user to save it securely");
            }
            toSave.put("id", new TypeInstances(new TypeInstance("admin")));
            toSave.put("name", new TypeInstances(new TypeInstance("admin")));
            toSave.put("description", new TypeInstances(new TypeInstance("admin")));

            BrokerRealUser user = new BrokerRealUser(brokerRealResources, "admin", "admin", "Default admin user", lifecycleHandler);
            try {
                storage.saveValues(realUsers.getPath(), user.getId(), toSave);
            } catch(HousemateException e) {
                log.e("Failed to save details for admin user, no one will be able to login");
            }
            realUsers.add(user);
        }
    }

    private void loadDevices(String[] path, Command<?, ?> addDeviceCommand) {
        try {
            for(String key : storage.getValuesKeys(path)) {
                try {
                    addDeviceCommand.perform(storage.getValues(path, key),
                            new CommandListener("Load device \"" + key + "\""));
                } catch(HousemateException e) {
                    log.e("Failed to load device");
                    log.st(e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved devices " + Arrays.toString(path));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing devices");
            log.st(e);
        }
    }

    private void loadAutomations() {
        BrokerRealList<AutomationData, BrokerRealAutomation> realAutomations = brokerRealResources.getRoot().getAutomations();
        try {
            for(String id : storage.getValuesKeys(realAutomations.getPath())) {
                try {
                    TypeInstanceMap details = storage.getValues(realAutomations.getPath(), id);
                    BrokerRealAutomation automation = new BrokerRealAutomation(brokerRealResources, details.get("id").getFirstValue(),
                            details.get("name").getFirstValue(), details.get("description").getFirstValue(), lifecycleHandler);
                    automation.init(realAutomations);
                    loadAutomationInfo(automation);
                    realAutomations.add(automation);
                } catch(HousemateException e) {
                    log.e("Failed to load automation");
                    log.st(e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved automations " + Arrays.toString(realAutomations.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing automations");
            log.st(e);
        }
    }

    private void loadAutomationInfo(BrokerRealAutomation automation) throws HousemateException {
        loadConditions(automation.getConditions(), automation);
        loadTasks(automation.getSatisfiedTasks(), automation.getSatisfiedTaskOwner());
        loadTasks(automation.getUnsatisfiedTasks(), automation.getUnsatisfiedTaskOwner());
    }

    private void loadConditions(BrokerRealList<ConditionData, BrokerRealCondition> conditions, BrokerRealConditionOwner owner) {
        try {
            for(String conditionName : storage.getValuesKeys(conditions.getPath())) {
                try {
                    TypeInstanceMap details = storage.getValues(conditions.getPath(), conditionName);
                    BrokerRealCondition condition = conditionFactory.createCondition(details, owner);
                    conditions.add(condition);
                    loadConditions(condition.getConditions(), condition);
                } catch(HousemateException e) {
                    log.e("Failed to load condition");
                    log.st(e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved conditions " + Arrays.toString(conditions.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get device names of existing conditions");
            log.st(e);
        }
    }

    private void loadTasks(BrokerRealList<TaskData, BrokerRealTask> tasks, BrokerRealTaskOwner owner) {
        try {
            for(String taskName : storage.getValuesKeys(tasks.getPath())) {
                try {
                    TypeInstanceMap details = storage.getValues(tasks.getPath(), taskName);
                    tasks.add(taskFactory.createTask(details, owner));
                } catch(HousemateException e) {
                    log.e("Failed to load task");
                    log.st(e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved tasks " + Arrays.toString(tasks.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get device names of existing tasks");
            log.st(e);
        }
    }

    private class CommandListener implements com.intuso.housemate.api.object.command.CommandListener<Command<?, ?>> {

        private final String description;

        private CommandListener(String description) {
            this.description = description;
        }

        @Override
        public void commandStarted(Command<?, ?> command) {
            log.d("Doing " + description);
        }

        @Override
        public void commandFinished(Command<?, ?> command) {
            log.d("Done " + description);
        }

        @Override
        public void commandFailed(Command<?, ?> command, String error) {
            log.d(description + " failed: " + error);
        }
    }
}
