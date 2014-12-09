package com.intuso.housemate.server.storage;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.instance.ApplicationInstanceData;
import com.intuso.housemate.api.object.automation.Automation;
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
import com.intuso.housemate.persistence.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

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
    private final Persistence persistence;
    private final ConditionFactory conditionFactory;
    private final TaskFactory taskFactory;
    private final RealList<DeviceData, RealDevice> devices;
    private final LifecycleHandler lifecycleHandler;

    @Inject
    public ServerObjectLoader(Log log, ListenersFactory listenersFactory, Injector injector, ServerRealRoot root,
                              Persistence persistence, ConditionFactory conditionFactory, TaskFactory taskFactory,
                              RealList<DeviceData, RealDevice> devices, LifecycleHandler lifecycleHandler) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.injector = injector;
        this.root = root;
        this.persistence = persistence;
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
            for(String key : persistence.getValuesKeys(realApplications.getPath())) {
                TypeInstanceMap details = persistence.getValues(realApplications.getPath(), key);
                ServerRealApplication application = new ServerRealApplication(log, listenersFactory, details.getChildren().get("id").getFirstValue(),
                        details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(),
                        injector.getInstance(ApplicationStatusType.class));
                List<String> path = Lists.newArrayList(realApplications.getPath());
                path.add(application.getId());
                loadApplicationInstances(path, application.getApplicationInstances(), application.getStatus());
                realApplications.add(application);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realApplications.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
    }

    private void loadApplicationInstances(List<String> path, ServerRealList<ApplicationInstanceData, ServerRealApplicationInstance> realApplicationInstances, ApplicationStatus applicationStatus) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]), key);
                ServerRealApplicationInstance applicationInstance = new ServerRealApplicationInstance(log, listenersFactory,
                        details.getChildren().get("id").getFirstValue(), injector.getInstance(ApplicationInstanceStatusType.class),
                        applicationStatus);
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
            for(String key : persistence.getValuesKeys(realUsers.getPath())) {
                TypeInstanceMap details = persistence.getValues(realUsers.getPath(), key);
                ServerRealUser user = new ServerRealUser(log, listenersFactory, details.getChildren().get("id").getFirstValue(),
                        details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(), this);
                realUsers.add(user);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realUsers.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
        if(realUsers.getChildren().size() == 0) {
            TypeInstanceMap toSave = new TypeInstanceMap();
            toSave.getChildren().put("id", new TypeInstances(new TypeInstance("admin")));
            toSave.getChildren().put("name", new TypeInstances(new TypeInstance("admin")));
            toSave.getChildren().put("description", new TypeInstances(new TypeInstance("admin")));

            ServerRealUser user = new ServerRealUser(log, listenersFactory, "admin", "admin", "Default admin user", this);
            try {
                persistence.saveValues(realUsers.getPath(), user.getId(), toSave);
            } catch(HousemateException e) {
                log.e("Failed to save details for admin user, no one will be able to gain access");
            }
            realUsers.add(user);
        }
    }

    private void loadDevices(String[] path, Command<?, ?, ?> addDeviceCommand) {
        try {
            for(String key : persistence.getValuesKeys(path)) {
                try {
                    addDeviceCommand.perform(persistence.getValues(path, key),
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
            for(String id : persistence.getValuesKeys(realAutomations.getPath())) {
                try {
                    TypeInstanceMap details = persistence.getValues(realAutomations.getPath(), id);
                    ServerRealAutomation automation = new ServerRealAutomation(log, listenersFactory, details.getChildren().get("id").getFirstValue(),
                            details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(), this,
                            lifecycleHandler);
                    // automation is not yet initialised so we cannot use it's path to load conditions etc. Instead,
                    // we can manually build the path using the list's path as a base.
                    List<String> path = Lists.newArrayList(realAutomations.getPath());
                    path.add(automation.getId());
                    path.add(Automation.CONDITIONS_ID);
                    loadConditions(path, automation.getConditions(), automation);
                    path.remove(path.size() - 1);
                    path.add(Automation.SATISFIED_TASKS_ID);
                    loadTasks(path, automation.getSatisfiedTasks(), automation.getSatisfiedTaskOwner());
                    path.remove(path.size() - 1);
                    path.add(Automation.UNSATISFIED_TASKS_ID);
                    loadTasks(path, automation.getUnsatisfiedTasks(), automation.getUnsatisfiedTaskOwner());
                    path.remove(path.size() - 1);
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

    private void loadConditions(List<String> path, ServerRealList<ConditionData, ServerRealCondition> conditions, ServerRealConditionOwner owner) {
        try {
            for(String conditionName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]), conditionName);
                    ServerRealCondition condition = conditionFactory.createCondition(details, owner);
                    path.add(condition.getId());
                    loadConditions(path, condition.getConditions(), condition);
                    path.remove(path.size() - 1);
                    conditions.add(condition);
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

    private void loadTasks(List<String> path, ServerRealList<TaskData, ServerRealTask> tasks, ServerRealTaskOwner owner) {
        try {
            for(String taskName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]), taskName);
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
