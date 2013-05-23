package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.authentication.AuthenticationMethod;
import com.intuso.housemate.api.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.root.RootWrappable;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.listeners.ListenerRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 12/03/13
 * Time: 18:21
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealRootObject
        extends BrokerRealObject<RootWrappable, HousemateObjectWrappable<?>, BrokerRealObject<?, ?, ?, ?>, RootListener<? super BrokerRealRootObject>>
        implements Root<BrokerRealRootObject, RootListener<? super BrokerRealRootObject>> {

    private final BrokerRealList<UserWrappable, BrokerRealUser> users;
    private final BrokerRealList<RuleWrappable, BrokerRealRule> rules;
    private final BrokerRealCommand addUserCommand;
    private final BrokerRealCommand addRuleCommand;

    public BrokerRealRootObject(BrokerRealResources resources) {
        super(resources, new RootWrappable());
        users = new BrokerRealList<UserWrappable, BrokerRealUser>(resources, USERS, USERS, "The defined users");
        rules = new BrokerRealList<RuleWrappable, BrokerRealRule>(resources, RULES, RULES, "The defined rules");
        addUserCommand = getResources().getLifecycleHandler().createAddUserCommand(users);
        addRuleCommand = getResources().getLifecycleHandler().createAddRuleCommand(rules);

        addWrapper(users);
        addWrapper(rules);
        addWrapper(addUserCommand);
        addWrapper(addRuleCommand);

        init(null);
    }

    @Override
    public void connect(AuthenticationMethod method, AuthenticationResponseHandler responseHandler) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void disconnect() {
        throw new HousemateRuntimeException("Cannot disconnect this type of root object");
    }

    @Override
    public ListenerRegistration<ObjectLifecycleListener> addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
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

    public BrokerRealList<RuleWrappable, BrokerRealRule> getRules() {
        return rules;
    }

    public BrokerRealCommand getAddUserCommand() {
        return addUserCommand;
    }

    public BrokerRealCommand getAddRuleCommand() {
        return addRuleCommand;
    }
}
