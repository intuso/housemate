package com.intuso.housemate.object.server.real;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class ServerRealRoot
        extends ServerRealObject<RootData, HousemateData<?>, ServerRealObject<?, ?, ?, ?>, RootListener<? super ServerRealRoot>>
        implements Root<ServerRealRoot> {

    private final ServerRealList<ApplicationData, ServerRealApplication> applications;
    private final ServerRealList<UserData, ServerRealUser> users;
    private final ServerRealList<AutomationData, ServerRealAutomation> automations;
    private final ServerRealCommand addUserCommand;
    private final ServerRealCommand addAutomationCommand;

    /**
     * @param log {@inheritDoc}
     */
    @Inject
    public ServerRealRoot(Log log, ListenersFactory listenersFactory, LifecycleHandler lifecycleHandler) {
        super(log, listenersFactory, new RootData());
        applications = new ServerRealList<ApplicationData, ServerRealApplication>(log, listenersFactory, APPLICATIONS_ID, APPLICATIONS_ID, "The registered applications");
        users = new ServerRealList<UserData, ServerRealUser>(log, listenersFactory, USERS_ID, USERS_ID, "The defined users");
        automations = new ServerRealList<AutomationData, ServerRealAutomation>(log, listenersFactory, AUTOMATIONS_ID, AUTOMATIONS_ID, "The defined automations");
        addUserCommand = lifecycleHandler.createAddUserCommand(users);
        addAutomationCommand = lifecycleHandler.createAddAutomationCommand(automations);

        addChild(applications);
        addChild(users);
        addChild(automations);
        addChild(addUserCommand);
        addChild(addAutomationCommand);

        init(null);
    }

    @Override
    public ApplicationStatus getApplicationStatus() {
        return ApplicationStatus.AllowInstances;
    }

    @Override
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return ApplicationInstanceStatus.Allowed;
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void unregister() {
        throw new HousemateRuntimeException("Cannot disconnect this type of root object");
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("Whatever");
    }

    /**
     * Gets the applications list
     * @return the applications list
     */
    public ServerRealList<ApplicationData, ServerRealApplication> getApplications() {
        return applications;
    }

    /**
     * Gets the users list
     * @return the users list
     */
    public ServerRealList<UserData, ServerRealUser> getUsers() {
        return users;
    }

    /**
     * Gets the automations list
     * @return the automations list
     */
    public ServerRealList<AutomationData, ServerRealAutomation> getAutomations() {
        return automations;
    }

    /**
     * Gets the add user command
     * @return the add user command
     */
    public ServerRealCommand getAddUserCommand() {
        return addUserCommand;
    }

    /**
     * Gets the add automation command
     * @return the add automation command
     */
    public ServerRealCommand getAddAutomationCommand() {
        return addAutomationCommand;
    }
}
