package com.intuso.housemate.server.object;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.automation.Automation;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.Email;
import com.intuso.housemate.object.real.impl.type.EmailType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.*;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.realclient.factory.DeviceFactory;
import com.intuso.housemate.realclient.factory.HardwareFactory;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.factory.TaskFactory;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;

public class LifecycleHandlerImpl implements LifecycleHandler {

    private final Log log;
    private final ListenersFactory listenersFactory;
    private final Persistence persistence;

    private final HardwareFactory hardwareFactory;
    private final DeviceFactory deviceFactory;
    private final ConditionFactory conditionFactory;
    private final TaskFactory taskFactory;

    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

    private final ValueListener<Value<?, ?>> runningListener = new ValueListener<Value<?, ?>>() {

        @Override
        public void valueChanging(Value<?, ?> value) {
            // do nothing
        }

        @Override
        public void valueChanged(Value<?, ?> value) {
            try {
                persistence.saveTypeInstances(value.getPath(), value.getTypeInstances());
            } catch(HousemateException e) {
                log.e("Failed to save running value of device", e);
            }
        }
    };

    @Inject
    public LifecycleHandlerImpl(Log log, ListenersFactory listenersFactory, Persistence persistence,
                                HardwareFactory hardwareFactory, DeviceFactory deviceFactory,
                                ConditionFactory conditionFactory, TaskFactory taskFactory,
                                RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        this.log = log;
        this.listenersFactory = listenersFactory;
        this.persistence = persistence;
        this.hardwareFactory = hardwareFactory;
        this.deviceFactory = deviceFactory;
        this.conditionFactory = conditionFactory;
        this.taskFactory = taskFactory;
        this.types = types;
    }

    @Override
    public ServerRealCommand createAddUserCommand(final ServerRealList<UserData, ServerRealUser> users) {
        return new ServerRealCommand(log, listenersFactory, Root.ADD_USER_ID, Root.ADD_USER_ID, "Add a new user", Arrays.<ServerRealParameter<?>>asList(
                new ServerRealParameter<String>(log, listenersFactory, "username", "Username", "The username for the new user", new StringType(log, listenersFactory)),
                new ServerRealParameter<Email>(log, listenersFactory, "email", "Email Address", "The new user's email address", new EmailType(log, listenersFactory))
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                String username = values.getChildren().get("username") != null ? values.getChildren().get("username").getFirstValue() : null;
                if(username == null)
                    throw new HousemateException("No username value set");
                Email email = values.getChildren().get("email") != null ? new Email(values.getChildren().get("username").getFirstValue()) : null;
                if(email == null)
                    throw new HousemateException("No email value set");
                ServerRealUser user = new ServerRealUser(log, listenersFactory, username, username, username, new ServerRealUserOwner() {
                    @Override
                    public void remove(ServerRealUser user) {
                        users.remove(user.getId());
                        try {
                            persistence.removeValues(user.getPath());
                        } catch(HousemateException e) {
                            log.e("Failed to remove stored details for user " + Arrays.toString(user.getPath()));
                        }
                    }
                });
                users.add(user);
                user.getEmailProperty().setTypedValue(email);
            }
        };
    }

    @Override
    public ServerRealCommand createAddAutomationCommand(final ServerRealList<AutomationData, ServerRealAutomation> list) {
        return new ServerRealCommand(log, listenersFactory, Root.ADD_AUTOMATION_ID, Root.ADD_AUTOMATION_ID, "Add a new automation", Arrays.<ServerRealParameter<?>>asList(
                new ServerRealParameter<String>(log, listenersFactory, "name", "Name", "The name for the new automation", new StringType(log, listenersFactory)),
                new ServerRealParameter<String>(log, listenersFactory, "description", "Description", "The description for the new automation", new StringType(log, listenersFactory))
        )) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                values.getChildren().put("id", values.getChildren().get("name")); // todo figure out a better way of getting an id
                ServerRealAutomation automation = new ServerRealAutomation(log, listenersFactory, values.getChildren().get("id").getFirstValue(),
                        values.getChildren().get("name").getFirstValue(), values.getChildren().get("description").getFirstValue(),
                        new ServerRealAutomationOwner() {
                            @Override
                            public void remove(ServerRealAutomation automation) {
                                list.remove(automation.getId());
                                try {
                                    persistence.removeValues(automation.getPath());
                                } catch(HousemateException e) {
                                    log.e("Failed to remove stored details for automation " + Arrays.toString(automation.getPath()));
                                }
                            }
                        }, LifecycleHandlerImpl.this);
                list.add(automation);
                String[] path = new String[list.getPath().length + 1];
                System.arraycopy(list.getPath(), 0, path, 0, list.getPath().length);
                path[path.length - 1] = automation.getId();
                persistence.saveValues(path, values);
                automation.getRunningValue().addObjectListener(runningListener);
            }
        };
    }

    @Override
    public ServerRealCommand createAddConditionCommand(ServerRealList<ConditionData, ServerRealCondition> conditions,
                                                       ServerRealConditionOwner owner) {
        return conditionFactory.createAddConditionCommand(Automation.ADD_CONDITION_ID, Automation.ADD_CONDITION_ID, "Add a new condition", owner, conditions);
    }

    @Override
    public ServerRealCommand createAddSatisfiedTaskCommand(ServerRealList<TaskData, ServerRealTask> tasks,
                                                           ServerRealTaskOwner owner) {
        return taskFactory.createAddTaskCommand(Automation.ADD_SATISFIED_TASK_ID, Automation.ADD_SATISFIED_TASK_ID, "Add a new satisfied task", owner, tasks);
    }

    @Override
    public ServerRealCommand createAddUnsatisfiedTaskCommand(ServerRealList<TaskData, ServerRealTask> tasks,
                                                             ServerRealTaskOwner owner) {
        return taskFactory.createAddTaskCommand(Automation.ADD_UNSATISFIED_TASK_ID, Automation.ADD_UNSATISFIED_TASK_ID, "Add a new unsatisfied task", owner, tasks);
    }
}
