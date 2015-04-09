package com.intuso.housemate.server.object.real.persist;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.object.application.Application;
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
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.real.*;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.factory.automation.RealAutomationFactory;
import com.intuso.housemate.object.real.factory.device.DeviceFactoryType;
import com.intuso.housemate.object.real.factory.device.RealDeviceFactory;
import com.intuso.housemate.object.real.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.object.real.factory.hardware.RealHardwareFactory;
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

    private final RealAutomationFactory realAutomationFactory;
    private final DeviceFactoryType deviceFactoryType;
    private final HardwareFactoryType hardwareFactoryType;
    private final RealUserFactory realUserFactory;

    private final AnnotationProcessor annotationProcessor;

    @Inject
    public RealObjectWatcher(Log log, ListenersFactory listenersFactory, Injector injector, RealRoot root, Persistence persistence, AutomationListWatcher automationListWatcher, ApplicationListWatcher applicationListWatcher, DeviceListWatcher deviceListWatcher, HardwareListWatcher hardwareListWatcher, UserListWatcher userListWatcher, RealAutomationFactory realAutomationFactory, DeviceFactoryType deviceFactoryType, HardwareFactoryType hardwareFactoryType, RealUserFactory realUserFactory, AnnotationProcessor annotationProcessor) {
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
        this.realAutomationFactory = realAutomationFactory;
        this.deviceFactoryType = deviceFactoryType;
        this.hardwareFactoryType = hardwareFactoryType;
        this.realUserFactory = realUserFactory;
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
                    path.add(Application.APPLICATION_INSTANCES_ID);
                    try {
                        loadApplicationInstances(Lists.newArrayList(path), application.getApplicationInstances(), application.getStatus());
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
        } catch(HousemateException e) {
            log.e("Failed to get names of existing applications", e);
        }
    }

    private void loadApplicationInstances(java.util.List<String> path, RealList<ApplicationInstanceData, RealApplicationInstance> realApplicationInstances, ApplicationStatus applicationStatus) {
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

    private void loadAutomations() {
        RealList<AutomationData, RealAutomation> automations = root.getAutomations();
        List<String> path = Lists.newArrayList(automations.getPath());
        try {
            for(String key : persistence.getValuesKeys(automations.getPath())) {
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
                    automations.add(automation);
                } catch(HousemateException e) {
                    log.e("Failed to load automation", e);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved automations " + Arrays.toString(automations.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing automations", e);
        }
    }

    private void loadConditions(java.util.List<String> path, RealList<ConditionData, RealCondition> conditions, RealCommand command) {
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

    private void loadDevices() {
        RealList<DeviceData, RealDevice> devices = root.getDevices();
        List<String> path = Lists.newArrayList(devices.getPath());
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    if(!details.getChildren().containsKey("type"))
                        log.e("No type found for persisted device " + key);
                    else if(details.getChildren().get("type").getElements().size() != 1)
                        log.e("Type for persisted device " + key + " does not have a single value");
                    else {
                        TypeInstance type = details.getChildren().get("type").getElements().get(0);
                        RealDeviceFactory realDeviceFactory = deviceFactoryType.deserialise(type);
                        if(realDeviceFactory == null) {
                            log.e("Could not find factory for device type " + type.getValue());
                        } else {
                            RealDevice device = realDeviceFactory.create(
                                    new DeviceData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                                    root);
                            annotationProcessor.process(root.getTypes(), device);
                            devices.add(device);
                        }
                    }
                } catch(HousemateException e) {
                    log.e("Failed to load device", e);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved devices at " + Joiner.on("/").join(devices.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing devices", e);
        }
    }

    private void loadHardwares() {
        RealList<HardwareData, RealHardware> hardwares = root.getHardwares();
        List<String> path = Lists.newArrayList(hardwares.getPath());
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    if(!details.getChildren().containsKey("type"))
                        log.e("No type found for persisted hardware " + key);
                    else if(details.getChildren().get("type").getElements().size() != 1)
                        log.e("Type for persisted hardware " + key + " does not have a single value");
                    else {
                        TypeInstance type = details.getChildren().get("type").getElements().get(0);
                        RealHardwareFactory realHardwareFactory = hardwareFactoryType.deserialise(type);
                        if(realHardwareFactory == null) {
                            log.e("Could not find factory for hardware type " + type.getValue());
                        } else {
                            RealHardware hardware = realHardwareFactory.create(
                                    new HardwareData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                                    root);
                            annotationProcessor.process(root.getTypes(), hardware);
                            hardwares.add(hardware);
                        }
                    }
                } catch(HousemateException e) {
                    log.e("Failed to load hardware", e);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved hardwares at " + Joiner.on("/").join(hardwares.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing hardwares", e);
        }
    }

    private void loadTasks(java.util.List<String> path, RealList<TaskData, RealTask> tasks, RealCommand command) {
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

    private void loadUsers() {
        RealList<UserData, RealUser> users = root.getUsers();
        List<String> path = Lists.newArrayList(users.getPath());
        try {
            for(String key : persistence.getValuesKeys(users.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealUser user = realUserFactory.create(
                            new UserData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root);
                    users.add(user);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved users " + Arrays.toString(users.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing users", e);
        }
        if(users.getChildren().size() == 0)
            users.add(realUserFactory.create(new UserData("admin", "admin", "Default admin user"), root));
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
