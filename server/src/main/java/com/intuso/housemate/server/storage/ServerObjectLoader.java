package com.intuso.housemate.server.storage;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
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
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.object.real.impl.type.ApplicationStatusType;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.*;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/11/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class ServerObjectLoader implements ServerRealAutomationOwner, ServerRealUserOwner {

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final Injector injector;
    private final ServerRealRoot root;
    private final Storage storage;
    private final ConditionFactory conditionFactory;
    private final TaskFactory taskFactory;
    private final RealList<DeviceData, RealDevice> devices;
    private final LifecycleHandler lifecycleHandler;

    @Inject
    public ServerObjectLoader(Log log, ListenersFactory listenersFactory, Injector injector, ServerRealRoot root, Storage storage,
                              ConditionFactory conditionFactory, TaskFactory taskFactory,
                              RealList<DeviceData, RealDevice> devices, LifecycleHandler lifecycleHandler) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.injector = injector;
        this.root = root;
        this.storage = storage;
        this.conditionFactory = conditionFactory;
        this.taskFactory = taskFactory;
        this.devices = devices;
        this.lifecycleHandler = lifecycleHandler;
    }

    public void loadObjects() {
        loadApplications(root.getApplications());
        loadUsers(root.getUsers());
        loadDevices(new String[]{"", Root.DEVICES_ID}, lifecycleHandler.createAddDeviceCommand(devices));
        loadAutomations(root.getAutomations());
    }

    private void loadApplications(ServerRealList<ApplicationData, ServerRealApplication> realApplications) {
        try {
            for(String key : storage.getValuesKeys(realApplications.getPath())) {
                TypeInstanceMap details = storage.getValues(realApplications.getPath(), key);
                ServerRealApplication application = new ServerRealApplication(log, listenersFactory, details.get("id").getFirstValue(),
                        details.get("name").getFirstValue(), details.get("description").getFirstValue(),
                        injector.getInstance(ApplicationStatusType.class), ApplicationStatus.SomeInstances);
                realApplications.add(application);
                loadApplicationInstances(application.getApplicationInstances(), application.getStatus());
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realApplications.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
    }

    private void loadApplicationInstances(ServerRealList<ApplicationInstanceData, ServerRealApplicationInstance> realApplicationInstances, ApplicationStatus applicationStatus) {
        try {
            for(String key : storage.getValuesKeys(realApplicationInstances.getPath())) {
                TypeInstanceMap details = storage.getValues(realApplicationInstances.getPath(), key);
                ServerRealApplicationInstance applicationInstance = new ServerRealApplicationInstance(log, listenersFactory,
                        details.get("id").getFirstValue(), injector.getInstance(ApplicationInstanceStatusType.class),
                        applicationStatus, ApplicationInstanceStatus.Pending);
                realApplicationInstances.add(applicationInstance);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realApplicationInstances.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
    }

    private void loadUsers(ServerRealList<UserData, ServerRealUser> realUsers) {
        try {
            for(String key : storage.getValuesKeys(realUsers.getPath())) {
                TypeInstanceMap details = storage.getValues(realUsers.getPath(), key);
                ServerRealUser user = new ServerRealUser(log, listenersFactory, details.get("id").getFirstValue(),
                        details.get("name").getFirstValue(), details.get("description").getFirstValue(), this);
                realUsers.add(user);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realUsers.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
        if(realUsers.getChildren().size() == 0) {
            TypeInstanceMap toSave = new TypeInstanceMap();
            toSave.put("id", new TypeInstances(new TypeInstance("admin")));
            toSave.put("name", new TypeInstances(new TypeInstance("admin")));
            toSave.put("description", new TypeInstances(new TypeInstance("admin")));

            ServerRealUser user = new ServerRealUser(log, listenersFactory, "admin", "admin", "Default admin user", this);
            try {
                storage.saveValues(realUsers.getPath(), user.getId(), toSave);
            } catch(HousemateException e) {
                log.e("Failed to save details for admin user, no one will be able to gain access");
            }
            realUsers.add(user);
        }
    }

    private void loadDevices(String[] path, Command<?, ?, ?> addDeviceCommand) {
        try {
            for(String key : storage.getValuesKeys(path)) {
                try {
                    addDeviceCommand.perform(storage.getValues(path, key),
                            new CommandPerformListener("Load device \"" + key + "\""));
                } catch(HousemateException e) {
                    log.e("Failed to load device", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved devices " + Arrays.toString(path));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing devices", e);
        }
    }

    private void loadAutomations(ServerRealList<AutomationData, ServerRealAutomation> realAutomations) {
        try {
            for(String id : storage.getValuesKeys(realAutomations.getPath())) {
                try {
                    TypeInstanceMap details = storage.getValues(realAutomations.getPath(), id);
                    ServerRealAutomation automation = new ServerRealAutomation(log, listenersFactory, details.get("id").getFirstValue(),
                            details.get("name").getFirstValue(), details.get("description").getFirstValue(), this,
                            lifecycleHandler);
                    automation.init(realAutomations);
                    loadConditions(automation.getConditions(), automation);
                    loadTasks(automation.getSatisfiedTasks(), automation.getSatisfiedTaskOwner());
                    loadTasks(automation.getUnsatisfiedTasks(), automation.getUnsatisfiedTaskOwner());
                    realAutomations.add(automation);
                } catch(HousemateException e) {
                    log.e("Failed to load automation", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved automations " + Arrays.toString(realAutomations.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing automations", e);
        }
    }

    private void loadConditions(ServerRealList<ConditionData, ServerRealCondition> conditions, ServerRealConditionOwner owner) {
        try {
            for(String conditionName : storage.getValuesKeys(conditions.getPath())) {
                try {
                    TypeInstanceMap details = storage.getValues(conditions.getPath(), conditionName);
                    ServerRealCondition condition = conditionFactory.createCondition(details, owner);
                    conditions.add(condition);
                    loadConditions(condition.getConditions(), condition);
                } catch(HousemateException e) {
                    log.e("Failed to load condition", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved conditions " + Arrays.toString(conditions.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get device names of existing conditions", e);
        }
    }

    private void loadTasks(ServerRealList<TaskData, ServerRealTask> tasks, ServerRealTaskOwner owner) {
        try {
            for(String taskName : storage.getValuesKeys(tasks.getPath())) {
                try {
                    TypeInstanceMap details = storage.getValues(tasks.getPath(), taskName);
                    tasks.add(taskFactory.createTask(details, owner));
                } catch(HousemateException e) {
                    log.e("Failed to load task", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved tasks " + Arrays.toString(tasks.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get device names of existing tasks", e);
        }
    }

    @Override
    public void remove(ServerRealAutomation automation) {
        root.getAutomations().remove(automation.getId());
    }

    @Override
    public void remove(ServerRealUser user) {
        root.getUsers().remove(user.getId());
    }

    private class CommandPerformListener implements com.intuso.housemate.api.object.command.CommandPerformListener<Command<?, ?, ?>> {

        private final String description;

        private CommandPerformListener(String description) {
            this.description = description;
        }

        @Override
        public void commandStarted(Command<?, ?, ?> command) {
            log.d("Doing " + description);
        }

        @Override
        public void commandFinished(Command<?, ?, ?> command) {
            log.d("Done " + description);
        }

        @Override
        public void commandFailed(Command<?, ?, ?> command, String error) {
            log.d(description + " failed: " + error);
        }
    }
}
