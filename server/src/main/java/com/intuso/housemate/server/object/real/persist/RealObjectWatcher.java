package com.intuso.housemate.server.object.real.persist;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.impl.internal.*;
import com.intuso.housemate.client.real.impl.internal.type.ApplicationInstanceStatusType;
import com.intuso.housemate.client.real.impl.internal.type.ApplicationStatusType;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.persistence.api.internal.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

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

    private final ListenersFactory listenersFactory;
    private final Injector injector;
    private final ServerRealRoot root;
    private final Persistence persistence;

    private final AutomationListWatcher automationListWatcher;
    private final ApplicationListWatcher applicationListWatcher;
    private final DeviceListWatcher deviceListWatcher;
    private final HardwareListWatcher hardwareListWatcher;
    private final UserListWatcher userListWatcher;

    private final RealAutomation.Factory automationFactory;
    private final RealDevice.Factory deviceFactory;
    private final RealHardware.Factory hardwareFactory;
    private final RealUser.Factory userFactory;

    @Inject
    public RealObjectWatcher(ListenersFactory listenersFactory, Injector injector, ServerRealRoot root, Persistence persistence, AutomationListWatcher automationListWatcher, ApplicationListWatcher applicationListWatcher, DeviceListWatcher deviceListWatcher, HardwareListWatcher hardwareListWatcher, UserListWatcher userListWatcher, RealAutomation.Factory automationFactory, RealDevice.Factory deviceFactory, RealHardware.Factory hardwareFactory, RealUser.Factory userFactory) {
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
        this.deviceFactory = deviceFactory;
        this.hardwareFactory = hardwareFactory;
        this.userFactory = userFactory;
    }

    public void start() {
        watchHardwares();
        watchDevices();
        watchApplications();
        watchUsers();
        watchAutomations();
    }

    private void watchApplications() {
        loadApplications(LoggerUtil.child(root.getLogger(), ServerRealRoot.APPLICATIONS_ID));
        root.getApplications().addObjectListener(applicationListWatcher, true);
    }

    private void watchAutomations() {
        loadAutomations(LoggerUtil.child(root.getLogger(), ServerRealRoot.AUTOMATIONS_ID));
        root.getAutomations().addObjectListener(automationListWatcher, true);
    }

    private void watchDevices() {
        loadDevices(LoggerUtil.child(root.getLogger(), BasicRealRoot.DEVICES_ID));
        root.getDevices().addObjectListener(deviceListWatcher, true);
    }

    private void watchHardwares() {
        loadHardwares(LoggerUtil.child(root.getLogger(), BasicRealRoot.HARDWARES_ID));
        root.getHardwares().addObjectListener(hardwareListWatcher, true);
    }

    private void watchUsers() {
        loadUsers(LoggerUtil.child(root.getLogger(), ServerRealRoot.USERS_ID));
        root.getUsers().addObjectListener(userListWatcher, true);
    }

    private void loadApplications(Logger logger) {
        RealList<? extends RealApplication> applications = root.getApplications();
        try {
            List<String> path = Lists.newArrayList(applications.getPath());
            for(String key : persistence.getValuesKeys(applications.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    Logger applicationLogger = LoggerUtil.child(logger, details.getChildren().get("id").getFirstValue());
                    RealApplication application = new RealApplicationImpl(applicationLogger,
                            listenersFactory, details.getChildren().get("id").getFirstValue(),
                            details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue(),
                            injector.getInstance(ApplicationStatusType.class));
                    path.add(ApplicationData.APPLICATION_INSTANCES_ID);
                    try {
                        loadApplicationInstances(LoggerUtil.child(applicationLogger, ApplicationData.APPLICATION_INSTANCES_ID), Lists.newArrayList(path), application.getApplicationInstances());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    ((RealList<RealApplication>)applications).add(application);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for saved applications " + Arrays.toString(applications.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to get names of existing applications", t);
        }
    }

    private void loadApplicationInstances(Logger logger, java.util.List<String> path, RealList<? extends RealApplicationInstance> realApplicationInstances) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealApplicationInstance applicationInstance = new RealApplicationInstanceImpl(LoggerUtil.child(logger, details.getChildren().get("id").getFirstValue()),
                            listenersFactory,
                            details.getChildren().get("id").getFirstValue(), injector.getInstance(ApplicationInstanceStatusType.class));
                    ((RealList<RealApplicationInstance>)realApplicationInstances).add(applicationInstance);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for saved users " + Arrays.toString(realApplicationInstances.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to get names of existing users", t);
        }
    }

    private void loadAutomations(Logger logger) {
        RealList<? extends RealAutomation> automations = root.getAutomations();
        List<String> path = Lists.newArrayList(automations.getPath());
        try {
            for(String key : persistence.getValuesKeys(automations.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    Logger automationLogger = LoggerUtil.child(logger, details.getChildren().get("id").getFirstValue());
                    RealAutomation automation = automationFactory.create(automationLogger,
                            new AutomationData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root);
                    // automation is not yet initialised so we cannot use it's path to load conditions etc. Instead,
                    // we can manually build the path using the list's path as a base.
                    try {
                        path.add(AutomationData.CONDITIONS_ID);
                        loadConditions(automationLogger, path, automation.getConditions(), automation.getAddConditionCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    try {
                        path.add(AutomationData.SATISFIED_TASKS_ID);
                        loadTasks(automationLogger, path, automation.getSatisfiedTasks(), automation.getAddSatisifedTaskCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    try {
                        path.add(AutomationData.UNSATISFIED_TASKS_ID);
                        loadTasks(automationLogger, path, automation.getUnsatisfiedTasks(), automation.getAddUnsatisifedTaskCommand());
                    } finally {
                        path.remove(path.size() - 1);
                    }
                    ((RealList<RealAutomation>)automations).add(automation);
                } catch(Throwable t) {
                    logger.error("Failed to load automation", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for saved automations " + Arrays.toString(automations.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to get names of existing automations", t);
        }
    }

    private void loadConditions(Logger logger, java.util.List<String> path, RealList<? extends RealCondition<?>> conditions, RealCommand command) {
        try {
            for(String conditionName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(conditionName);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    command.perform(details);
                } catch(Throwable t) {
                    logger.error("Failed to load condition", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for saved conditions " + Arrays.toString(conditions.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to get device names of existing conditions", t);
        }
    }

    private void loadDevices(Logger logger) {
        RealList<? extends RealDevice<?>> devices = root.getDevices();
        List<String> path = Lists.newArrayList(devices.getPath());
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    ((RealList<RealDevice<?>>)devices).add(deviceFactory.create(LoggerUtil.child(logger, details.getChildren().get("id").getFirstValue()),
                            new DeviceData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root));
                } catch(Throwable t) {
                    logger.error("Failed to load device", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for saved devices at " + Joiner.on("/").join(devices.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to get names of existing devices", t);
        }
    }

    private void loadHardwares(Logger logger) {
        RealList<? extends RealHardware<?>> hardwares = root.getHardwares();
        List<String> path = Lists.newArrayList(hardwares.getPath());
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    ((RealList<RealHardware<?>>)hardwares).add(hardwareFactory.create(LoggerUtil.child(logger, details.getChildren().get("id").getFirstValue()),
                            new HardwareData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root));
                } catch(Throwable t) {
                    logger.error("Failed to load hardware", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for saved hardwares at " + Joiner.on("/").join(hardwares.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to get names of existing hardwares", t);
        }
    }

    private void loadTasks(Logger logger, java.util.List<String> path, RealList<? extends RealTask<?>> tasks, RealCommand command) {
        try {
            for(String taskName : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(taskName);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    command.perform(details);
                } catch(Throwable t) {
                    logger.error("Failed to load task", t);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for saved tasks " + Arrays.toString(tasks.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to get device names of existing tasks", t);
        }
    }

    private void loadUsers(Logger logger) {
        RealList<? extends RealUser> users = root.getUsers();
        List<String> path = Lists.newArrayList(users.getPath());
        try {
            for(String key : persistence.getValuesKeys(users.getPath())) {
                try {
                    path.add(key);
                    TypeInstanceMap details = persistence.getValues(path.toArray(new String[path.size()]));
                    RealUser user = userFactory.create(LoggerUtil.child(logger, details.getChildren().get("id").getFirstValue()),
                            new UserData(details.getChildren().get("id").getFirstValue(), details.getChildren().get("name").getFirstValue(), details.getChildren().get("description").getFirstValue()),
                            root);
                    ((RealList<RealUser>)users).add(user);
                } finally {
                    path.remove(path.size() - 1);
                }
            }
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for saved users " + Arrays.toString(users.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to get names of existing users", t);
        }
        if(users.size() == 0)
            ((RealList<RealUser>)users).add(userFactory.create(LoggerUtil.child(logger, "admin"), new UserData("admin", "admin", "Default admin user"), root));
    }
}
