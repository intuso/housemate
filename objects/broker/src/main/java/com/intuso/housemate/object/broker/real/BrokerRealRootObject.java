package com.intuso.housemate.object.broker.real;

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
import com.intuso.utilities.listener.ListenerRegistration;

public class BrokerRealRootObject
        extends BrokerRealObject<RootData, HousemateData<?>, BrokerRealObject<?, ?, ?, ?>, RootListener<? super BrokerRealRootObject>>
        implements Root<BrokerRealRootObject> {

    private final BrokerRealList<UserData, BrokerRealUser> users;
    private final BrokerRealList<AutomationData, BrokerRealAutomation> automations;
    private final BrokerRealCommand addUserCommand;
    private final BrokerRealCommand addAutomationCommand;

    /**
     * @param resources {@inheritDoc}
     */
    public BrokerRealRootObject(BrokerRealResources resources) {
        super(resources, new RootData());
        users = new BrokerRealList<UserData, BrokerRealUser>(resources, USERS_ID, USERS_ID, "The defined users");
        automations = new BrokerRealList<AutomationData, BrokerRealAutomation>(resources, AUTOMATIONS_ID, AUTOMATIONS_ID, "The defined automations");
        addUserCommand = getResources().getLifecycleHandler().createAddUserCommand(users);
        addAutomationCommand = getResources().getLifecycleHandler().createAddAutomationCommand(automations);

        addWrapper(users);
        addWrapper(automations);
        addWrapper(addUserCommand);
        addWrapper(addAutomationCommand);

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
    public BrokerRealList<UserData, BrokerRealUser> getUsers() {
        return users;
    }

    /**
     * Gets the automations list
     * @return the automations list
     */
    public BrokerRealList<AutomationData, BrokerRealAutomation> getAutomations() {
        return automations;
    }

    /**
     * Gets the add user command
     * @return the add user command
     */
    public BrokerRealCommand getAddUserCommand() {
        return addUserCommand;
    }

    /**
     * Gets the add automation command
     * @return the add automation command
     */
    public BrokerRealCommand getAddAutomationCommand() {
        return addAutomationCommand;
    }
}
