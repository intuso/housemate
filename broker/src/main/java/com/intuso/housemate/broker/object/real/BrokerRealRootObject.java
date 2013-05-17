package com.intuso.housemate.broker.object.real;

import com.google.common.collect.Maps;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.HousemateRuntimeException;
import com.intuso.housemate.core.authentication.AuthenticationMethod;
import com.intuso.housemate.core.authentication.AuthenticationResponseHandler;
import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.root.Root;
import com.intuso.housemate.core.object.root.RootListener;
import com.intuso.housemate.core.object.root.RootWrappable;
import com.intuso.housemate.core.object.rule.RuleWrappable;
import com.intuso.housemate.core.object.user.UserWrappable;
import com.intuso.housemate.real.impl.type.StringType;

import java.util.Arrays;
import java.util.Map;

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
    private final BrokerRealCommand addDeviceCommand;
    private final BrokerRealCommand addRuleCommand;

    public BrokerRealRootObject(BrokerRealResources resources) {
        super(resources, new RootWrappable());
        users = new BrokerRealList<UserWrappable, BrokerRealUser>(resources, USERS, USERS, "The defined users");
        rules = new BrokerRealList<RuleWrappable, BrokerRealRule>(resources, RULES, RULES, "The defined rules");
        addUserCommand = new BrokerRealCommand(getResources(), ADD_USER, ADD_USER, "Add a new user", Arrays.<BrokerRealArgument<?>>asList(
                new BrokerRealArgument<String>(getResources(), "username", "Username", "The username for the new user", new StringType(getResources().getGeneralResources().getClientResources())),
                new BrokerRealArgument<String>(getResources(), "password", "Password", "The password for the new user", new StringType(getResources().getGeneralResources().getClientResources()))
        )) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                Map<String, String> toSave = Maps.newHashMap();
                toSave.put("id", values.get("username"));
                toSave.put("name", values.get("username"));
                toSave.put("description", values.get("username"));
                // todo hash the password
                toSave.put("password-hash", values.get("password"));
                BrokerRealUser user = new BrokerRealUser(getResources(), toSave.get("id"), toSave.get("name"), toSave.get("description"));
                users.add(user);
                getResources().getGeneralResources().getStorage().saveDetails(users.getPath(), user.getId(), values);
            }
        };
        addDeviceCommand = getResources().getGeneralResources().getDeviceFactory().createAddDeviceCommand(ADD_DEVICE, ADD_DEVICE, "Add a new device", getResources().getGeneralResources().getClient().getRoot().getDevices());
        addRuleCommand = new BrokerRealCommand(getResources(), ADD_RULE, ADD_RULE, "Add a new rule", Arrays.<BrokerRealArgument<?>>asList(
                new BrokerRealArgument<String>(getResources(), "name", "Name", "The name for the new rule", new StringType(getResources().getGeneralResources().getClientResources())),
                new BrokerRealArgument<String>(getResources(), "description", "Description", "The description for the new rule", new StringType(getResources().getGeneralResources().getClientResources()))
        )) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                values.put("id", values.get("name")); // todo figure out a better way of getting an id
                BrokerRealRule rule = new BrokerRealRule(getResources(), values.get("id"), values.get("name"), values.get("description"));
                rules.add(rule);
                getResources().getGeneralResources().getStorage().saveDetails(rules.getPath(), rule.getId(), values);
            }
        };

        addWrapper(users);
        addWrapper(rules);
        addWrapper(addUserCommand);
        addWrapper(addDeviceCommand);
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
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("Whatever");
    }

    public void loadObjects() {
        getResources().getGeneralResources().getStorage().loadUsers(users);
        getResources().getGeneralResources().getStorage().loadDevices(new String[]{"", DEVICES}, addDeviceCommand);
        getResources().getGeneralResources().getStorage().loadRules(rules);
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

    public BrokerRealCommand getAddDeviceCommand() {
        return addDeviceCommand;
    }

    public BrokerRealCommand getAddRuleCommand() {
        return addRuleCommand;
    }
}
