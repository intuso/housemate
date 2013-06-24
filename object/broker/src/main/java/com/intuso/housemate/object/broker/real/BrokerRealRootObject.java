package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.RootWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 */
public class BrokerRealRootObject
        extends BrokerRealObject<RootWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>, RootListener<? super BrokerRealRootObject>>
        implements Root<BrokerRealRootObject, RootListener<? super BrokerRealRootObject>> {

    private final BrokerRealList<UserWrappable, BrokerRealUser> users;
    private final BrokerRealList<AutomationWrappable, BrokerRealAutomation> automations;
    private final BrokerRealCommand addUserCommand;
    private final BrokerRealCommand addAutomationCommand;

    public BrokerRealRootObject(BrokerRealResources resources) {
        super(resources, new RootWrappable());
        users = new BrokerRealList<UserWrappable, BrokerRealUser>(resources, USERS_ID, USERS_ID, "The defined users");
        automations = new BrokerRealList<AutomationWrappable, BrokerRealAutomation>(resources, AUTOMATIONS_ID, AUTOMATIONS_ID, "The defined automations");
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

    public BrokerRealList<UserWrappable, BrokerRealUser> getUsers() {
        return users;
    }

    public BrokerRealList<AutomationWrappable, BrokerRealAutomation> getAutomations() {
        return automations;
    }

    public BrokerRealCommand getAddUserCommand() {
        return addUserCommand;
    }

    public BrokerRealCommand getAddAutomationCommand() {
        return addAutomationCommand;
    }
}
