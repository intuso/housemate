package com.intuso.housemate.broker.storage;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.Consequence;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.rule.Rule;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealRule;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
    private final WatchRuleListListener watchRuleListListener = new WatchRuleListListener();
    private final WatchConditionListListener watchConditionListListener = new WatchConditionListListener();
    private final WatchConsequenceListListener watchConsequenceListListener = new WatchConsequenceListListener();
    private final WatchPropertyListListener watchPropertyListListener = new WatchPropertyListListener();
    private final WatchValueListener watchValueListener = new WatchValueListener();

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

    private void loadUsers() {
        BrokerRealList<UserWrappable, BrokerRealUser> realUsers = resources.getRealResources().getRoot().getUsers();
        try {
            for(String key : storage.getValuesKeys(realUsers.getPath())) {
                TypeInstances details = getValues(realUsers.getPath(), key);
                BrokerRealUser user = new BrokerRealUser(resources.getRealResources(), details.get("id").getValue(),
                        details.get("name").getValue(), details.get("description").getValue());
                realUsers.add(user);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realUsers.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users");
            log.st(e);
        }
        if(realUsers.getWrappers().size() == 0) {
            TypeInstances toSave = new TypeInstances();
            try {
                toSave.put("password-hash", new TypeInstance(new String(MessageDigest.getInstance("MD5").digest(
                        "admin".getBytes()))));
            } catch(NoSuchAlgorithmException e) {
                resources.getLog().e("Unable to hash the password for the default user to save it securely");
            }
            toSave.put("id", new TypeInstance("admin"));
            toSave.put("name", new TypeInstance("admin"));
            toSave.put("description", new TypeInstance("admin"));

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

    private void loadRules() {
        BrokerRealList<RuleWrappable, BrokerRealRule> realRules = resources.getRealResources().getRoot().getRules();
        try {
            for(String id : storage.getValuesKeys(realRules.getPath())) {
                try {
                    TypeInstances details = getValues(realRules.getPath(), id);
                    BrokerRealRule rule = new BrokerRealRule(resources.getRealResources(), details.get("id").getValue(),
                            details.get("name").getValue(), details.get("description").getValue());
                    rule.init(realRules);
                    loadRuleInfo(rule);
                    realRules.add(rule);
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
    }

    private void loadConditions(BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions) {
        try {
            for(String conditionName : storage.getValuesKeys(conditions.getPath())) {
                try {
                    TypeInstances details = storage.getValues(conditions.getPath(), conditionName);
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

    private void loadConsequences(BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> consequences) {
        try {
            for(String consequenceName : storage.getValuesKeys(consequences.getPath())) {
                try {
                    TypeInstances details = storage.getValues(consequences.getPath(), consequenceName);
                    consequences.add(resources.getConsequenceFactory().createConsequence(details));
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

    public void watchDevices(List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> devices) {
        devices.addObjectListener(watchDeviceListListener, true);
    }

    public void watchRules(List<? extends Rule<?, ?, ?, ?, ?, ?, ?,
            ? extends Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
            ? extends Consequence<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?>> rules) {
        rules.addObjectListener(watchRuleListListener, true);
    }

    @Override
    public TypeInstance getValue(String[] path) throws DetailsNotFoundException, HousemateException {
        return storage.getValue(path);
    }

    @Override
    public void saveValue(String[] path, TypeInstance value) throws HousemateException {
        storage.saveValue(path, value);
    }

    @Override
    public Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException, HousemateException {
        return storage.getValuesKeys(path);
    }

    @Override
    public TypeInstances getValues(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException {
        return storage.getValues(path, detailsKey);
    }

    @Override
    public void saveValues(String[] path, String detailsKey, TypeInstances details) throws HousemateException {
        storage.saveValues(path, detailsKey, details);
    }

    public void removeValues(String[] path) throws HousemateException {
        storage.removeValues(path);
    }

    private class WatchDeviceListListener implements ListListener<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> {

        private final Map<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> runningListeners = Maps.newHashMap();
        private final Map<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> propertyListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
            runningListeners.put(device, device.getRunningValue().addObjectListener(watchValueListener));
            propertyListeners.put(device, device.getProperties().addObjectListener(watchPropertyListListener, true));
            try {
                TypeInstance value = storage.getValue(device.getRunningValue().getPath());
                if(BooleanType.SERIALISER.deserialise(value))
                    device.getStartCommand().perform(new TypeInstances(),
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
            ListenerRegistration registration = runningListeners.remove(device);
            if(registration != null)
                registration.removeListener();
            registration = propertyListeners.remove(device);
            if(registration != null)
                registration.removeListener();
        }
    }

    private class WatchRuleListListener implements ListListener<Rule<?, ?, ?, ?, ?, ?, ?,
            ? extends Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
            ? extends Consequence<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?>> {

        private final Map<Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> runningListeners = Maps.newHashMap();
        private final Map<Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> conditionListeners = Maps.newHashMap();
        private final Map<Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> satisfiedConsequenceListeners = Maps.newHashMap();
        private final Map<Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> unsatisfiedConsequenceListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Rule<?, ?, ?, ?, ?, ?, ?,
                ? extends Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
                ? extends Consequence<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?> rule) {
            runningListeners.put(rule, rule.getRunningValue().addObjectListener(watchValueListener));
            conditionListeners.put(rule, rule.getConditions().addObjectListener(watchConditionListListener, true));
            satisfiedConsequenceListeners.put(rule, rule.getSatisfiedConsequences().addObjectListener(watchConsequenceListListener, true));
            unsatisfiedConsequenceListeners.put(rule, rule.getUnsatisfiedConsequences().addObjectListener(watchConsequenceListListener, true));
            try {
                TypeInstance value = storage.getValue(rule.getRunningValue().getPath());
                if(BooleanType.SERIALISER.deserialise(value))
                    rule.getStartCommand().perform(new TypeInstances(),
                            new CommandListener("Start rule \"" + rule.getId() + "\""));
            } catch(DetailsNotFoundException e) {
                log.w("No details found for whether the device was previously running" + Arrays.toString(rule.getPath()));
            } catch(HousemateException e) {
                log.e("Failed to check value for whether the device was previously running");
                log.st(e);
            }
        }

        @Override
        public void elementRemoved(Rule<?, ?, ?, ?, ?, ?, ?,
                ? extends Condition<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
                ? extends Consequence<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?> rule) {
            ListenerRegistration registration = runningListeners.remove(rule);
            if(registration != null)
                registration.removeListener();
            registration = conditionListeners.remove(rule);
            if(registration != null)
                registration.removeListener();
            registration = satisfiedConsequenceListeners.remove(rule);
            if(registration != null)
                registration.removeListener();
            registration = unsatisfiedConsequenceListeners.remove(rule);
            if(registration != null)
                registration.removeListener();
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

    private class WatchConsequenceListListener implements ListListener<Consequence<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>> {

        private final Map<Consequence<?, ?, ?, ?>, ListenerRegistration> propertyListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Consequence<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?> consequence) {
            propertyListeners.put(consequence, consequence.getProperties().addObjectListener(watchPropertyListListener, true));
        }

        @Override
        public void elementRemoved(Consequence<?, ?, ? extends List<? extends Property<?, ?, ?>>, ?> consequence) {
            ListenerRegistration registration = propertyListeners.remove(consequence);
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
        public void valueChanging(Value<?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(Value<?, ?> property) {
            try {
                storage.saveValue(property.getPath(), property.getTypeInstance());
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
