package com.intuso.housemate.server.storage;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.Condition;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/11/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class ServerObjectPersister {

    private final Storage storage;
    private final Log log;

    private final WatchDeviceListListener watchDeviceListListener = new WatchDeviceListListener();
    private final WatchAutomationListListener watchAutomationListListener = new WatchAutomationListListener();
    private final WatchConditionListListener watchConditionListListener = new WatchConditionListListener();
    private final WatchTaskListListener watchTaskListListener = new WatchTaskListListener();
    private final WatchPropertyListListener watchPropertyListListener = new WatchPropertyListListener();
    private final WatchValueListener watchValueListener = new WatchValueListener();

    @Inject
    public ServerObjectPersister(Storage storage, Log log) {
        this.storage = storage;
        this.log = log;
    }

    public void watchDevices(List<? extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> devices) {
        devices.addObjectListener(watchDeviceListListener, true);
    }

    public void watchAutomations(List<? extends Automation<?, ?, ?, ?, ?,
            ? extends Condition<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
            ? extends Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?>> automations) {
        automations.addObjectListener(watchAutomationListListener, true);
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
                storage.removeValues(device.getPath());
            } catch(HousemateException e) {
                log.e("Failed to delete device properties");
                log.st(e);
            }
        }
    }

    private class WatchAutomationListListener implements ListListener<Automation<?, ?, ?, ?, ?,
            ? extends Condition<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
            ? extends Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?>> {

        private final Map<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> runningListeners = Maps.newHashMap();
        private final Map<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> conditionListeners = Maps.newHashMap();
        private final Map<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> satisfiedTaskListeners = Maps.newHashMap();
        private final Map<Automation<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> unsatisfiedTaskListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Automation<?, ?, ?, ?, ?,
                ? extends Condition<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
                ? extends Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?> automation) {
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
                ? extends Condition<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>, ?,
                ? extends Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>, ?, ?> automation) {
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
                storage.removeValues(automation.getPath());
            } catch(HousemateException e) {
                log.e("Failed to delete automation properties");
                log.st(e);
            }
        }
    }

    private class WatchConditionListListener implements ListListener<Condition<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?>> {

        private final Map<Condition<?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> propertyListeners = Maps.newHashMap();
        private final Map<Condition<?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> conditionListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Condition<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?> condition) {
            propertyListeners.put(condition, condition.getProperties().addObjectListener(watchPropertyListListener, true));
            conditionListeners.put(condition, condition.getConditions().addObjectListener(watchConditionListListener, true));
        }

        @Override
        public void elementRemoved(Condition<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?, ?, ?> condition) {
            ListenerRegistration registration = propertyListeners.remove(condition);
            if(registration != null)
                registration.removeListener();
            registration = conditionListeners.remove(condition);
            if(registration != null)
                registration.removeListener();
        }
    }

    private class WatchTaskListListener implements ListListener<Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?>> {

        private final Map<Task<?, ?, ?, ?, ?>, ListenerRegistration> propertyListeners = Maps.newHashMap();

        @Override
        public void elementAdded(Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?> task) {
            propertyListeners.put(task, task.getProperties().addObjectListener(watchPropertyListListener, true));
        }

        @Override
        public void elementRemoved(Task<?, ?, ?, ? extends List<? extends Property<?, ?, ?>>, ?> task) {
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
