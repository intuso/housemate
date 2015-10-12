package com.intuso.housemate.server.object.real.persist;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.annotations.AnnotationProcessor;
import com.intuso.housemate.client.real.api.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.api.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.api.internal.impl.type.ApplicationInstanceStatusType;
import com.intuso.housemate.client.real.api.internal.impl.type.ApplicationStatusType;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Command;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.persistence.api.internal.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.internal.Persistence;
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
public class RealObjectWatcher {

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final Injector injector;
    private final RealRoot root;
    private final Persistence persistence;

    private final AutomationListWatcher automationListWatcher;
    private final ApplicationListWatcher applicationListWatcher;
    private final DeviceListWatcher deviceListWatcher;
    private final HardwareListWatcher hardwareListWatcher;
    private final UserListWatcher userListWatcher;

    private final RealAutomation.Factory automationFactory;
    private final DeviceFactoryType deviceFactoryType;
    private final RealDevice.Factory deviceFactory;
    private final HardwareFactoryType hardwareFactoryType;
    private final RealHardware.Factory hardwareFactory;
    private final RealUser.Factory userFactory;

    private final AnnotationProcessor annotationProcessor;

    @Inject
    public RealObjectWatcher(Log log, ListenersFactory listenersFactory, Injector injector, RealRoot root, Persistence persistence, AutomationListWatcher automationListWatcher, ApplicationListWatcher applicationListWatcher, DeviceListWatcher deviceListWatcher, HardwareListWatcher hardwareListWatcher, UserListWatcher userListWatcher, RealAutomation.Factory automationFactory, DeviceFactoryType deviceFactoryType, RealDevice.Factory deviceFactory, HardwareFactoryType hardwareFactoryType, RealHardware.Factory hardwareFactory, RealUser.Factory userFactory, AnnotationProcessor annotationProcessor) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.injector = injector;
        this.root = root;
        this.persistence = persistence;
        this.automationListWatcher = automationListWatcher;
        this.applicationListWatcher = applicationListWatcher;
        this.deviceListWatcher = deviceListWatcher;
        this.hardwareListWatcher = hardwareListWatcher;
        this.userListWatcher = userListWatcher;
        this.automationFactory = automationFactory;
        this.deviceFactoryType = deviceFactoryType;
        this.deviceFactory = deviceFactory;
        this.hardwareFactoryType = hardwareFactoryType;
        this.hardwareFactory = hardwareFactory;
        this.userFactory = userFactory;
        this.annotationProcessor = annotationProcessor;
    }

    public void start() {
        watchHardwares();
        watchDevices();
        watchApplications();
        watchUsers();
        watchAutomations();
    }

    private void watchApplications() {
        loadApplications();
        root.getApplications().addObjectListener(applicationListWatcher, true);
    }

    private void watchAutomations() {
        loadAutomations();
        root.getAutomations().addObjectListener(automationListWatcher, true);
    }

    private void watchDevices() {
        loadDevices();
        root.getDevices().addObjectListener(deviceListWatcher, true);
    }

    private void watchHardwares() {
        loadHardwares();
        root.getHardwares().addObjectListener(hardwareListWatcher, true);
    }

    private void watchUsers() {
        loadUsers();
        root.getUsers().addObjectListener(userListWatcher, true);
    }

    private void loadApplications() {
        RealList<ApplicationData, RealApplication> applications = root.getApplications();
        try {
            List<String> path = Lists.newArrayList(applications.getPath());
            for(String key : persistence.getValuesKeys(applications.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealApplication application = new RealApplication(log, listenersFactory, details.getChildren().get("id").getFirstValue(),
                            details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(),
                            injector.getInstance(ApplicationStatusType.class));
                    path.add(ApplicationData.APPLICATION_INSTANCES_ID);
                    try {
                        loadApplicationInstances(Lists.newArrayList(path), application.getApplicationInstances());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    applications.add(application);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved applications " + Arrays.toString(applications.getPath()));
        } catch(Throwable t) {
            log.e("Failed to get names of existing applications", t);
        }
    }

    private void loadApplicationInstances(java.util.List<String> path, RealList<ApplicationInstanceData, RealApplicationInstance> realApplicationInstances) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealApplicationInstance applicationInstance = new RealApplicationInstance(log, listenersFactory,
                            details.getChildren().get("id").getFirstValue(), injector.getInstance(ApplicationInstanceStatusType.class));
                    realApplicationInstances.add(applicationInstance);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(realApplicationInstances.getPath()));
        } catch(Throwable t) {
            log.e("Failed to get names of existing users", t);
        }
    }

    private void loadAutomations() {
        RealList<AutomationData, RealAutomation> automations = root.getAutomations();
        List<String> path = Lists.newArrayList(automations.getPath());
        try {
            for(String key : persistence.getValuesKeys(automations.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealAutomation automation = automationFactory.create(
                            new AutomationData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root);
                    // automation is not yet initialised so we cannot use it's path to load conditions etc. Instead,
                    // we can manually build the path using the list's path as a base.
                    try {
                        path.add(AutomationData.CONDITIONS_ID);
                        loadConditions(path, automation.getConditions(), automation.getAddConditionCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    try {
                        path.add(AutomationData.SATISFIED_TASKS_ID);
                        loadTasks(path, automation.getSatisfiedTasks(), automation.getAddSatisifedTaskCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    try {
                        path.add(AutomationData.UNSATISFIED_TASKS_ID);
                        loadTasks(path, automation.getUnsatisfiedTasks(), automation.getAddUnsatisifedTaskCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    automations.add(automation);
                } catch(Throwable t) {
                    log.e("Failed to load automation", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved automations " + Arrays.toString(automations.getPath()));
        } catch(Throwable t) {
            log.e("Failed to get names of existing automations", t);
        }
    }

    private void loadConditions(java.util.List<String> path, RealList<ConditionData, RealCondition<?>> conditions, RealCommand command) {
        try {
            for(String conditionName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(conditionName);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    command.perform(details);
                } catch(Throwable t) {
                    log.e("Failed to load condition", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved conditions " + Arrays.toString(conditions.getPath()));
        } catch(Throwable t) {
            log.e("Failed to get device names of existing conditions", t);
        }
    }

    private void loadDevices() {
        RealList<DeviceData, RealDevice<?>> devices = root.getDevices();
        List<String> path = Lists.newArrayList(devices.getPath());
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    devices.add(deviceFactory.create(
                            new DeviceData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root));
                } catch(Throwable t) {
                    log.e("Failed to load device", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved devices at " + Joiner.on("/").join(devices.getPath()));
        } catch(Throwable t) {
            log.e("Failed to get names of existing devices", t);
        }
    }

    private void loadHardwares() {
        RealList<HardwareData, RealHardware<?>> hardwares = root.getHardwares();
        List<String> path = Lists.newArrayList(hardwares.getPath());
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    hardwares.add(hardwareFactory.create(
                            new HardwareData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root));
                } catch(Throwable t) {
                    log.e("Failed to load hardware", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved hardwares at " + Joiner.on("/").join(hardwares.getPath()));
        } catch(Throwable t) {
            log.e("Failed to get names of existing hardwares", t);
        }
    }

    private void loadTasks(java.util.List<String> path, RealList<TaskData, RealTask<?>> tasks, RealCommand command) {
        try {
            for(String taskName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(taskName);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    command.perform(details);
                } catch(Throwable t) {
                    log.e("Failed to load task", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved tasks " + Arrays.toString(tasks.getPath()));
        } catch(Throwable t) {
            log.e("Failed to get device names of existing tasks", t);
        }
    }

    private void loadUsers() {
        RealList<UserData, RealUser> users = root.getUsers();
        List<String> path = Lists.newArrayList(users.getPath());
        try {
            for(String key : persistence.getValuesKeys(users.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealUser user = userFactory.create(
                            new UserData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root);
                    users.add(user);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(users.getPath()));
        } catch(Throwable t) {
            log.e("Failed to get names of existing users", t);
        }
        if(users.getChildren().size() == 0)
            users.add(userFactory.create(new UserData("admin", "admin", "Default admin user"), root));
    }

    private class CommandPerformListener implements Command.PerformListener<Command<?, ?, ?, ?>> {

        private final String description;

        private CommandPerformListener(String description) {
            this.description = description;
        }

        @Override
        public void commandStarted(Command<?, ?, ?, ?> command) {
            log.d("Doing " + description);
        }

        @Override
        public void commandFinished(Command<?, ?, ?, ?> command) {
            log.d("Done " + description);
        }

        @Override
        public void commandFailed(Command<?, ?, ?, ?> command, String error) {
            log.d(description + " failed: " + error);
        }
    }
}
