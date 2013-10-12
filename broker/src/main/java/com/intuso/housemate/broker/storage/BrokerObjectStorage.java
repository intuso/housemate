package com.intuso.housemate.broker.storage;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.BrokerRealAutomation;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 */
public class BrokerObjectStorage implements Storage {
    
    private final Storage storage;
    private final Log log;
    private final BrokerGeneralResources resources;
    private final WatchDeviceListListener watchDeviceListListener = new WatchDeviceListListener();
    private final WatchAutomationListListener watchAutomationListListener = new WatchAutomationListListener();
    private final WatchConditionListListener watchConditionListListener = new WatchConditionListListener();
    private final WatchTaskListListener watchTaskListListener = new WatchTaskListListener();
    private final WatchPropertyListListener watchPropertyListListener = new WatchPropertyListListener();
    private final WatchValueListener watchValueListener = new WatchValueListener();

    public BrokerObjectStorage(Storage storage, BrokerGeneralResources resources) {
        this.storage = storage;
        this.resources = resources;
        log = resources.getLog();
    }

    public void loadObjects() {
        loadUsers();
        loadDevices(new String[]{"", Root.DEVICES_ID}, resources.getRealResources().getLifecycleHandler().createAddDeviceCommand(
                resources.getClient().getRoot().getDevices()));
        loadAutomations();
    }

    private void loadUsers() {
        BrokerRealList<UserData, BrokerRealUser> realUsers = resources.getRealResources().getRoot().getUsers();
        try {
            for(String key : storage.getValuesKeys(realUsers.getPath())) {
                TypeInstanceMap details = getValues(realUsers.getPath(), key);
                BrokerRealUser user = new BrokerRealUser(resources.getRealResources(), details.get("id").getFirstValue(),
                        details.get("name").getFirstValue(), details.get("description").getFirstValue());
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
                resources.getLog().e("Unable to hash the password for the default user to save it securely");
            }
            toSave.put("id", new TypeInstances(new TypeInstance("admin")));
            toSave.put("name", new TypeInstances(new TypeInstance("admin")));
            toSave.put("description", new TypeInstances(new TypeInstance("admin")));

            BrokerRealUser user = new BrokerRealUser(resources.getRealResources(), "admin", "admin", "Default admin user");
            try {
                resources.getStorage().saveValues(realUsers.getPath(), user.getId(), toSave);
            } catch(HousemateException e) {
                resources.getLog().e("Failed to save details for admin user, no one will be able to login");
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
        BrokerRealList<AutomationData, BrokerRealAutomation> realAutomations = resources.getRealResources().getRoot().getAutomations();
        try {
            for(String id : storage.getValuesKeys(realAutomations.getPath())) {
                try {
                    TypeInstanceMap details = getValues(realAutomations.getPath(), id);
                    BrokerRealAutomation automation = new BrokerRealAutomation(resources.getRealResources(), details.get("id").getFirstValue(),
                            details.get("name").getFirstValue(), details.get("description").getFirstValue());
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
        loadConditions(automation.getConditions());
        loadTasks(automation.getSatisfiedTasks());
        loadTasks(automation.getUnsatisfiedTasks());
    }

    private void loadConditions(BrokerRealList<ConditionData, BrokerRealCondition> conditions) {
        try {
            for(String conditionName : storage.getValuesKeys(conditions.getPath())) {
                try {
                    TypeInstanceMap details = storage.getValues(conditions.getPath(), conditionName);
                    BrokerRealCondition condition = resources.getConditionFactory().createCondition(details);
                    conditions.add(condition);
                    loadConditions(condition.getConditions());
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

    private void loadTasks(BrokerRealList<TaskData, BrokerRealTask> tasks) {
        try {
            for(String taskName : storage.getValuesKeys(tasks.getPath())) {
                try {
                    TypeInstanceMap details = storage.getValues(tasks.getPath(), taskName);
                    tasks.add(resources.getTaskFactory().createTask(details));
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

    public void watchDevices(List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> devices) {
        devices.addObjectListener(watchDeviceListListener, true);
    }

    public void watchAutomations(List<? extends Automation<?, ?, ?, ?, ?,
                ? extends Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
                ? extends Task<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?>> automations) {
        automations.addObjectListener(watchAutomationListListener, true);
    }

    @Override
    public TypeInstances getTypeInstances(String[] path) throws DetailsNotFoundException, HousemateException {
        return storage.getTypeInstances(path);
    }

    @Override
    public void saveTypeInstances(String[] path, TypeInstances value) throws HousemateException {
        storage.saveTypeInstances(path, value);
    }

    @Override
    public Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException, HousemateException {
        return storage.getValuesKeys(path);
    }

    @Override
    public TypeInstanceMap getValues(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException {
        return storage.getValues(path, detailsKey);
    }

    @Override
    public void saveValues(String[] path, String detailsKey, TypeInstanceMap details) throws HousemateException {
        storage.saveValues(path, detailsKey, details);
    }

    public void removeValues(String[] path) throws HousemateException {
        storage.removeValues(path);
    }

    private class WatchDeviceListListener implements ListListener<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> {

        private final Map<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> runningListeners = Maps.newHashMap();
        private final Map<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> propertyListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
            runningListeners.put(device, device.getRunningValue().addObjectListener(watchValueListener));
            propertyListeners.put(device, device.getProperties().addObjectListener(watchPropertyListListener, true));
            try {
                TypeInstances instances = storage.getTypeInstances(device.getRunningValue().getPath());
                if(instances.size() > 0 && BooleanType.SERIALISER.deserialise(instances.get(0)))
                    device.getStartCommand().perform(new TypeInstanceMap(),
                            new CommandListener("Start device \"" + device.getId() + "\""));
            } catch(DetailsNotFoundException e) {
                log.w("No details found for whether the device was previously running" + Arrays.toString(device.getPath()));
            } catch(HousemateException e) {
                log.e("Failed to check value for whether the device was previously running");
                log.st(e);
            }
        }

        @Override
        public void elementRemoved(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
            ListenerRegistration registration = runningListeners.remove(device);
            if(registration != null)
                registration.removeListener();
            registration = propertyListeners.remove(device);
            if(registration != null)
                registration.removeListener();
            try {
                removeValues(device.getPath());
            } catch(HousemateException e) {
                log.e("Failed to delete device properties");
                log.st(e);
            }
        }
    }

    private class WatchAutomationListListener implements ListListener<Automation<?, ?, ?, ?, ?,
                ? extends Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
                ? extends Task<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?>> {

        private final Map<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> runningListeners = Maps.newHashMap();
        private final Map<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> conditionListeners = Maps.newHashMap();
        private final Map<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> satisfiedTaskListeners = Maps.newHashMap();
        private final Map<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> unsatisfiedTaskListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Automation<?, ?, ?, ?, ?,
                        ? extends Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
                        ? extends Task<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?> automation) {
            runningListeners.put(automation, automation.getRunningValue().addObjectListener(watchValueListener));
            conditionListeners.put(automation, automation.getConditions().addObjectListener(watchConditionListListener, true));
            satisfiedTaskListeners.put(automation, automation.getSatisfiedTasks().addObjectListener(watchTaskListListener, true));
            unsatisfiedTaskListeners.put(automation, automation.getUnsatisfiedTasks().addObjectListener(watchTaskListListener, true));
            try {
                TypeInstances instances = storage.getTypeInstances(automation.getRunningValue().getPath());
                if(instances.size() > 0 && BooleanType.SERIALISER.deserialise(instances.get(0)))
                    automation.getStartCommand().perform(new TypeInstanceMap(),
                            new CommandListener("Start automation \"" + automation.getId() + "\""));
            } catch(DetailsNotFoundException e) {
                log.w("No details found for whether the device was previously running" + Arrays.toString(automation.getPath()));
            } catch(HousemateException e) {
                log.e("Failed to check value for whether the device was previously running");
                log.st(e);
            }
        }

        @Override
        public void elementRemoved(Automation<?, ?, ?, ?, ?,
                        ? extends Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
                        ? extends Task<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?> automation) {
            ListenerRegistration registration = runningListeners.remove(automation);
            if(registration != null)
                registration.removeListener();
            registration = conditionListeners.remove(automation);
            if(registration != null)
                registration.removeListener();
            registration = satisfiedTaskListeners.remove(automation);
            if(registration != null)
                registration.removeListener();
            registration = unsatisfiedTaskListeners.remove(automation);
            if(registration != null)
                registration.removeListener();
            try {
                removeValues(automation.getPath());
            } catch(HousemateException e) {
                log.e("Failed to delete automation properties");
                log.st(e);
            }
        }
    }

    private class WatchConditionListListener implements ListListener<Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ? extends List<? extends Condition<?, ?, ?, ?, ?, ?>>>> {

        private final Map<Condition<?, ?, ?, ?, ?, ?>, ListenerRegistration> propertyListeners = Maps.newHashMap();
        private final Map<Condition<?, ?, ?, ?, ?, ?>, ListenerRegistration> conditionListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ? extends List<? extends Condition<?, ?, ?, ?, ?, ?>>> condition) {
            propertyListeners.put(condition, condition.getProperties().addObjectListener(watchPropertyListListener, true));
            conditionListeners.put(condition, condition.getConditions().addObjectListener(watchConditionListListener, true));
        }

        @Override
        public void elementRemoved(Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ? extends List<? extends Condition<?, ?, ?, ?, ?, ?>>> condition) {
            ListenerRegistration registration = propertyListeners.remove(condition);
            if(registration != null)
                registration.removeListener();
            registration = conditionListeners.remove(condition);
            if(registration != null)
                registration.removeListener();
        }
    }

    private class WatchTaskListListener implements ListListener<Task<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>> {

        private final Map<Task<?, ?, ?, ?>, ListenerRegistration> propertyListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Task<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?> task) {
            propertyListeners.put(task, task.getProperties().addObjectListener(watchPropertyListListener, true));
        }

        @Override
        public void elementRemoved(Task<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?> task) {
            ListenerRegistration registration = propertyListeners.remove(task);
            if(registration != null)
                registration.removeListener();
        }
    }

    private class WatchPropertyListListener implements ListListener<Property<?, ?, ?>> {

        private final WatchValueListener watchPropertyListener = new WatchValueListener();
        private final Map<Property<?, ?, ?>, ListenerRegistration> listeners = Maps.newHashMap();

        @Override
        public void elementAdded(Property<?, ?, ?> property) {
            try {
                property.set(storage.getTypeInstances(property.getPath()),
                        new CommandListener("Set property value " + Arrays.toString(property.getPath())));
            } catch(DetailsNotFoundException e) {
                log.w("No details found for property value " + Arrays.toString(property.getPath()));
            } catch(HousemateException e) {
                log.e("Failed to set property value " + Arrays.toString(property.getPath()));
                log.st(e);
            }
            listeners.put(property, property.addObjectListener(watchPropertyListener));
        }

        @Override
        public void elementRemoved(Property<?, ?, ?> property) {
            ListenerRegistration registration = listeners.remove(property);
            if(registration != null)
                registration.removeListener();
        }
    }

    private class WatchValueListener implements ValueListener<Value<?, ?>> {

        @Override
        public void valueChanging(Value<?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(Value<?, ?> property) {
            try {
                storage.saveTypeInstances(property.getPath(), property.getTypeInstances());
            } catch(HousemateException e) {
                log.e("Failed to save property value");
                log.st(e);
            }
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
