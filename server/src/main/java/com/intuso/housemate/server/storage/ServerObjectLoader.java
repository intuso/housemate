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
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.factory.automation.RealAutomationFactory;
import com.intuso.housemate.object.real.factory.user.RealUserFactory;
import com.intuso.housemate.object.real.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.object.real.impl.type.ApplicationStatusType;
import com.intuso.housemate.persistence.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.Persistence;
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
public class ServerObjectLoader {

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final Injector injector;
    private final RealRoot root;
    private final Persistence persistence;
    private final RealAutomationFactory realAutomationFactory;
    private final RealUserFactory realUserFactory;

    @Inject
    public ServerObjectLoader(Log log, ListenersFactory listenersFactory, Injector injector, RealRoot root,
                              Persistence persistence, RealAutomationFactory realAutomationFactory, RealUserFactory realUserFactory) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.injector = injector;
        this.root = root;
        this.persistence = persistence;
        this.realAutomationFactory = realAutomationFactory;
        this.realUserFactory = realUserFactory;
    }

    public void loadObjects() {
        loadHardwares(Lists.newArrayList(root.getHardwares().getPath()), root.getHardwares(), root.getAddHardwareCommand());
        loadDevices(Lists.newArrayList(root.getDevices().getPath()), root.getDevices(), root.getAddDeviceCommand());
        loadApplications(Lists.newArrayList(root.getApplications().getPath()), root.getApplications());
        loadUsers(Lists.newArrayList(root.getUsers().getPath()), root.getUsers());
        loadAutomations(Lists.newArrayList(root.getAutomations().getPath()), root.getAutomations());
    }

    private void loadApplications(List<String> path, RealList<ApplicationData, RealApplication> list) {
        try {
            for(String key : persistence.getValuesKeys(list.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealApplication application = new RealApplication(log, listenersFactory, details.getChildren().get("id").getFirstValue(),
                            details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(),
                            injector.getInstance(ApplicationStatusType.class));
                    loadApplicationInstances(Lists.newArrayList(path), application.getApplicationInstances(), application.getStatus());
                    list.add(application);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(list.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
    }

    private void loadApplicationInstances(List<String> path, RealList<ApplicationInstanceData, RealApplicationInstance> realApplicationInstances, ApplicationStatus applicationStatus) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealApplicationInstance applicationInstance = new RealApplicationInstance(log, listenersFactory,
                            details.getChildren().get("id").getFirstValue(), injector.getInstance(ApplicationInstanceStatusType.class),
                            applicationStatus);
                    realApplicationInstances.add(applicationInstance);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realApplicationInstances.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
    }

    private void loadUsers(List<String> path, RealList<UserData, RealUser> list) {
        try {
            for(String key : persistence.getValuesKeys(list.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealUser user = realUserFactory.create(
                            new UserData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root);
                    list.add(user);
                } finally {
                    path.remove(path.size() - 1);
                }
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

            RealUser user = realUserFactory.create(new UserData("admin", "admin", "Default admin user"), root);
            try {
                path.add(user.getId());
                persistence.saveValues(path.toArray(new String[path.size()]), toSave);
            } catch(HousemateException e) {
                log.e("Failed to save details for admin user, no one will be able to gain access");
            } finally {
                path.remove(path.size() - 1);
            }
            list.add(user);
        }
    }

    private void loadHardwares(List<String> path, RealList<HardwareData, RealHardware> hardwares, Command<?, ?, ?> addHardwareCommand) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    addHardwareCommand.perform(persistence.getValues(path.toArray(new String[path.size()])),
                            new CommandPerformListener("Load hardware \"" + key + "\""));
                    path.remove(path.size() - 1);
                } catch(HousemateException e) {
                    log.e("Failed to load hardware", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved hardwares at " + Joiner.on("/").join(hardwares.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing hardwares", e);
        }
    }

    private void loadDevices(List<String> path, RealList<DeviceData, RealDevice> devices, Command<?, ?, ?> addDeviceCommand) {
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
            log.w("No details found for saved devices at " + Joiner.on("/").join(devices.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing devices", e);
        }
    }

    private void loadAutomations(List<String> path, RealList<AutomationData, RealAutomation> list) {
        try {
            for(String key : persistence.getValuesKeys(list.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealAutomation automation = realAutomationFactory.create(
                            new AutomationData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root);
                    // automation is not yet initialised so we cannot use it's path to load conditions etc. Instead,
                    // we can manually build the path using the list's path as a base.
                    try {
                        path.add(Automation.CONDITIONS_ID);
                        loadConditions(path, automation.getConditions(), automation.getAddConditionCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    try {
                        path.add(Automation.SATISFIED_TASKS_ID);
                        loadTasks(path, automation.getSatisfiedTasks(), automation.getAddSatisifedTaskCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    try {
                        path.add(Automation.UNSATISFIED_TASKS_ID);
                        loadTasks(path, automation.getUnsatisfiedTasks(), automation.getAddUnsatisifedTaskCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    list.add(automation);
                } catch(HousemateException e) {
                    log.e("Failed to load automation", e);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved automations " + Arrays.toString(list.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing automations", e);
        }
    }

    private void loadConditions(List<String> path, RealList<ConditionData, RealCondition> conditions, RealCommand command) {
        try {
            for(String conditionName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(conditionName);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    command.perform(details);
                } catch(HousemateException e) {
                    log.e("Failed to load condition", e);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved conditions " + Arrays.toString(conditions.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get device names of existing conditions", e);
        }
    }

    private void loadTasks(List<String> path, RealList<TaskData, RealTask> tasks, RealCommand command) {
        try {
            for(String taskName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(taskName);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    command.perform(details);
                } catch(HousemateException e) {
                    log.e("Failed to load task", e);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved tasks " + Arrays.toString(tasks.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get device names of existing tasks", e);
        }
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
