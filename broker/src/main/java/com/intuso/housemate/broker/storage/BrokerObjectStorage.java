package com.intuso.housemate.broker.storage;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealRule;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 15/02/13
 * Time: 23:07
 * To change this template use File | Settings | File Templates.
 */
public class BrokerObjectStorage implements Storage {
    
    private final Storage storage;
    private final Log log;
    private final BrokerGeneralResources resources;
    private final WatchDeviceListListener watchDeviceListListener = new WatchDeviceListListener();
    private final WatchPropertyListListener watchPropertyListListener = new WatchPropertyListListener();

    public BrokerObjectStorage(Storage storage, BrokerGeneralResources resources) {
        this.storage = storage;
        this.resources = resources;
        log = resources.getLog();
    }

    public void loadObjects() {
        loadUsers();
        loadDevices(new String[]{"", Root.DEVICES}, resources.getRealResources().getLifecycleHandler().createAddDeviceCommand(
                resources.getClient().getRoot().getDevices()));
        loadRules();
    }

    public void loadUsers() {
        BrokerRealList<UserWrappable, BrokerRealUser> realUsers = resources.getRealResources().getRoot().getUsers();
        try {
            for(String key : storage.getDetailsKeys(realUsers.getPath())) {
                Map<String, String> details = getDetails(realUsers.getPath(), key);
                BrokerRealUser user = new BrokerRealUser(resources.getRealResources(), details.get("id"), details.get("name"), details.get("description"));
                realUsers.add(user);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realUsers.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users");
            log.st(e);
        }
        if(realUsers.getWrappers().size() == 0)
            realUsers.add(new BrokerRealUser(resources.getRealResources(), "admin", "admin", "Default admin user"));
    }

    public void loadDevices(String[] path, Command<?, ?> addDeviceCommand) {
        try {
            for(String key : storage.getDetailsKeys(path)) {
                try {
                    addDeviceCommand.perform(storage.getDetails(path, key),
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

    public void watchDevices(List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> devices) {
        devices.addObjectListener(watchDeviceListListener, true);
    }

    public void loadRules() {
        BrokerRealList<RuleWrappable, BrokerRealRule> realRules = resources.getRealResources().getRoot().getRules();
        try {
            for(String id : storage.getDetailsKeys(realRules.getPath())) {
                try {
                    Map<String, String> details = getDetails(realRules.getPath(), id);
                    BrokerRealRule rule = new BrokerRealRule(resources.getRealResources(), details.get("id"), details.get("name"), details.get("description"));
                    realRules.add(rule);
                    loadRuleInfo(rule);
                } catch(HousemateException e) {
                    log.e("Failed to load rule");
                    log.st(e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved rules " + Arrays.toString(realRules.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing rules");
            log.st(e);
        }
    }

    private void loadRuleInfo(BrokerRealRule rule) throws HousemateException {
        loadConditions(rule.getConditions());
        loadConsequences(rule.getSatisfiedConsequences());
        loadConsequences(rule.getUnsatisfiedConsequences());
        try {
            String value = storage.getValue(rule.getRunningValue().getPath());
            if(Boolean.parseBoolean(value))
                rule.getStartCommand().perform(new HashMap<String, String>(),
                        new CommandListener("Start rule \"" + rule.getId() + "\""));
        } catch(DetailsNotFoundException e) {
            log.w("No details found for rule info " + Arrays.toString(rule.getPath()));
        }
    }

    private void loadConditions(BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions) {
        try {
            for(String conditionName : storage.getDetailsKeys(conditions.getPath())) {
                try {
                    Map<String, String> details = storage.getDetails(conditions.getPath(), conditionName);
                    BrokerRealCondition condition = resources.getConditionFactory().createCondition(details);
                    conditions.add(condition);
                    watchPropertyValues(condition.getProperties());
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

    private void loadConsequences(BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> consequences) {
        try {
            for(String consequenceName : storage.getDetailsKeys(consequences.getPath())) {
                try {
                    Map<String, String> details = storage.getDetails(consequences.getPath(), consequenceName);
                    BrokerRealConsequence consequence = resources.getConsequenceFactory().createConsequence(details);
                    consequences.add(consequence);
                    watchPropertyValues(consequence.getProperties());
                } catch(HousemateException e) {
                    log.e("Failed to load consequence");
                    log.st(e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved consequences " + Arrays.toString(consequences.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get device names of existing consequences");
            log.st(e);
        }
    }

    public void watchPropertyValues(List<? extends Property<?, ?, ?>> properties) {
        properties.addObjectListener(watchPropertyListListener, true);
    }

    @Override
    public String getValue(String[] path) throws DetailsNotFoundException, HousemateException {
        return storage.getValue(path);
    }

    @Override
    public void saveValue(String[] path, String value) throws HousemateException {
        storage.saveValue(path, value);
    }

    @Override
    public Set<String> getDetailsKeys(String[] path) throws DetailsNotFoundException, HousemateException {
        return storage.getDetailsKeys(path);
    }

    @Override
    public Map<String, String> getDetails(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException {
        return storage.getDetails(path, detailsKey);
    }

    @Override
    public void saveDetails(String[] path, String detailsKey, Map<String, String> details) throws HousemateException {
        storage.saveDetails(path, detailsKey, details);
    }

    public void removeDetails(String[] path) throws HousemateException {
        storage.removeDetails(path);
    }

    private class WatchDeviceListListener implements ListListener<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> {

        private final WatchPropertyListListener watchPropertyListListener = new WatchPropertyListListener();
        private final WatchValueListener watchPropertyListener = new WatchValueListener();
        private final Map<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> propertyListeners = Maps.newHashMap();
        private final Map<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> runningListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
            propertyListeners.put(device, device.getProperties().addObjectListener(watchPropertyListListener, true));
            runningListeners.put(device, device.getRunningValue().addObjectListener(watchPropertyListener));
            try {
                String value = storage.getValue(device.getRunningValue().getPath());
                if(Boolean.parseBoolean(value))
                    device.getStartCommand().perform(new HashMap<String, String>(),
                            new CommandListener("Start device \"" + device.getId() + "\""));
            } catch(DetailsNotFoundException e) {
                log.w("No details found for whether the device was previously running" + Arrays.toString(device.getPath()));
            } catch(HousemateException e) {
                log.e("Failed to check value for whether the device was previously running");
                log.st(e);
            }
        }

        @Override
        public void elementRemoved(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
            ListenerRegistration registration = propertyListeners.remove(device);
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
                property.set(storage.getValue(property.getPath()),
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
        public void valueChanged(Value<?, ?> property) {
            try {
                storage.saveValue(property.getPath(), property.getValue());
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
