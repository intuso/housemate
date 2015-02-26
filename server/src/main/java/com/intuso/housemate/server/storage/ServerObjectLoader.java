package com.intuso.housemate.server.storage;

import com.google.common.base.Joiner;
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
        loadDevices(Lists.newArrayList("", Root.DEVICES_ID), lifecycleHandler.createAddDeviceCommand(devices));
        loadAutomations(root.getAutomations());
    }

    private void loadApplications(ServerRealList<ApplicationData, ServerRealApplication> list) {
        try {
            for(String key : persistence.getValuesKeys(list.getPath())) {
                String[] path = new String[list.getPath().length + 1];
                System.arraycopy(list.getPath(), 0, path, 0, list.getPath().length);
                path[path.length - 1] = key;
                TypeInstanceMap details = persistence.getValues(path);
                ServerRealApplication application = new ServerRealApplication(log, listenersFactory, details.getChildren().get("id").getFirstValue(),
                        details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(),
                        injector.getInstance(ApplicationStatusType.class));
                loadApplicationInstances(Lists.newArrayList(path), application.getApplicationInstances(), application.getStatus());
                list.add(application);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(list.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
    }

    private void loadApplicationInstances(List<String> path, ServerRealList<ApplicationInstanceData, ServerRealApplicationInstance> realApplicationInstances, ApplicationStatus applicationStatus) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                path.add(key);
                TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                ServerRealApplicationInstance applicationInstance = new ServerRealApplicationInstance(log, listenersFactory,
                        details.getChildren().get("id").getFirstValue(), injector.getInstance(ApplicationInstanceStatusType.class),
                        applicationStatus);
                realApplicationInstances.add(applicationInstance);
                path.remove(path.size() - 1);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realApplicationInstances.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
    }

    private void loadUsers(ServerRealList<UserData, ServerRealUser> list) {
        try {
            for(String key : persistence.getValuesKeys(list.getPath())) {
                String[] path = new String[list.getPath().length + 1];
                System.arraycopy(list.getPath(), 0, path, 0, list.getPath().length);
                path[path.length - 1] = key;
                TypeInstanceMap details = persistence.getValues(path);
                ServerRealUser user = new ServerRealUser(log, listenersFactory, details.getChildren().get("id").getFirstValue(),
                        details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(), this);
                list.add(user);
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(list.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
        if(list.getChildren().size() == 0) {
            TypeInstanceMap toSave = new TypeInstanceMap();
            toSave.getChildren().put("id", new TypeInstances(new TypeInstance("admin")));
            toSave.getChildren().put("name", new TypeInstances(new TypeInstance("admin")));
            toSave.getChildren().put("description", new TypeInstances(new TypeInstance("admin")));

            ServerRealUser user = new ServerRealUser(log, listenersFactory, "admin", "admin", "Default admin user", this);
            try {
                String[] path = new String[list.getPath().length + 1];
                System.arraycopy(list.getPath(), 0, path, 0, list.getPath().length);
                path[path.length - 1] = user.getId();
                persistence.saveValues(path, toSave);
            } catch(HousemateException e) {
                log.e("Failed to save details for admin user, no one will be able to gain access");
            }
            list.add(user);
        }
    }

    private void loadDevices(List<String> path, Command<?, ?, ?> addDeviceCommand) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    addDeviceCommand.perform(persistence.getValues(path.toArray(new String[path.size()])),
                            new CommandPerformListener("Load device \"" + key + "\""));
                    path.remove(path.size() - 1);
                } catch(HousemateException e) {
                    log.e("Failed to load device", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved devices at " + Joiner.on("/").join(path));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing devices", e);
        }
    }

    private void loadAutomations(ServerRealList<AutomationData, ServerRealAutomation> list) {
        try {
            for(String key : persistence.getValuesKeys(list.getPath())) {
                try {
                    String[] path = new String[list.getPath().length + 1];
                    System.arraycopy(list.getPath(), 0, path, 0, list.getPath().length);
                    path[path.length - 1] = key;
                    TypeInstanceMap details = persistence.getValues(path);
                    ServerRealAutomation automation = new ServerRealAutomation(log, listenersFactory, details.getChildren().get("id").getFirstValue(),
                            details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(), this,
                            lifecycleHandler);
                    // automation is not yet initialised so we cannot use it's path to load conditions etc. Instead,
                    // we can manually build the path using the list's path as a base.
                    List<String> pathList = Lists.newArrayList(list.getPath());
                    pathList.add(automation.getId());
                    pathList.add(Automation.CONDITIONS_ID);
                    loadConditions(pathList, automation.getConditions(), automation);
                    pathList.remove(pathList.size() - 1);
                    pathList.add(Automation.SATISFIED_TASKS_ID);
                    loadTasks(pathList, automation.getSatisfiedTasks(), automation.getSatisfiedTaskOwner());
                    pathList.remove(pathList.size() - 1);
                    pathList.add(Automation.UNSATISFIED_TASKS_ID);
                    loadTasks(pathList, automation.getUnsatisfiedTasks(), automation.getUnsatisfiedTaskOwner());
                    pathList.remove(pathList.size() - 1);
                    list.add(automation);
                } catch(HousemateException e) {
                    log.e("Failed to load automation", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved automations " + Arrays.toString(list.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing automations", e);
        }
    }

    private void loadConditions(List<String> path, ServerRealList<ConditionData, ServerRealCondition> conditions, ServerRealConditionOwner owner) {
        try {
            for(String conditionName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(conditionName);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    ServerRealCondition condition = conditionFactory.createCondition(details, owner);
                    loadConditions(path, condition.getConditions(), condition);
                    conditions.add(condition);
                    path.remove(path.size() - 1);
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
                    path.add(taskName);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    tasks.add(taskFactory.createTask(details, owner));
                    path.remove(path.size() - 1);
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
