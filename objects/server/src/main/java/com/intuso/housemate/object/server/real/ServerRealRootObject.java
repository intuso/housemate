package com.intuso.housemate.object.server.real;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.utilities.listener.ListenerRegistration;

@Singleton
public class ServerRealRootObject
        extends ServerRealObject<RootData, HousemateData<?>, ServerRealObject<?, ?, ?, ?>, RootListener<? super ServerRealRootObject>>
        implements Root<ServerRealRootObject> {

    private final ServerRealList<UserData, ServerRealUser> users;
    private final ServerRealList<AutomationData, ServerRealAutomation> automations;
    private final ServerRealCommand addUserCommand;
    private final ServerRealCommand addAutomationCommand;

    /**
     * @param resources {@inheritDoc}
     */
    @Inject
    public ServerRealRootObject(ServerRealResources resources, LifecycleHandler lifecycleHandler) {
        super(resources, new RootData());
        users = new ServerRealList<UserData, ServerRealUser>(resources, USERS_ID, USERS_ID, "The defined users");
        automations = new ServerRealList<AutomationData, ServerRealAutomation>(resources, AUTOMATIONS_ID, AUTOMATIONS_ID, "The defined automations");
        addUserCommand = lifecycleHandler.createAddUserCommand(users);
        addAutomationCommand = lifecycleHandler.createAddAutomationCommand(automations);

        addChild(users);
        addChild(automations);
        addChild(addUserCommand);
        addChild(addAutomationCommand);

        init(null);
    }

    @Override
    public ConnectionStatus getStatus() {
        return ConnectionStatus.Authenticated;
    }

    @Override
    public String getConnectionId() {
        return null;
    }

    @Override
    public void login(AuthenticationMethod method) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void logout() {
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
