package com.intuso.housemate.broker.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.rule.Rule;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.proxy.BrokerProxyPrimaryObject;
import com.intuso.housemate.object.broker.real.BrokerRealArgument;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealRule;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/05/13
 * Time: 23:40
 * To change this template use File | Settings | File Templates.
 */
public class LifecycleHandlerImpl implements LifecycleHandler {

    private final BrokerGeneralResources resources;

    private final ValueListener<Value<?, ?>> runningListener = new ValueListener<Value<?, ?>>() {
        @Override
        public void valueChanged(Value<?, ?> value) {
            try {
                resources.getStorage().saveValue(value.getPath(), value.getValue());
            } catch(HousemateException e) {
                resources.getLog().e("Failed to save running value of device");
                resources.getLog().st(e);
            }
        }
    };

    public LifecycleHandlerImpl(BrokerGeneralResources resources) {
        this.resources = resources;
    }

    @Override
    public BrokerRealCommand createAddUserCommand(final BrokerRealList<UserWrappable, BrokerRealUser> users) {
        return new BrokerRealCommand(resources.getRealResources(), Root.ADD_USER, Root.ADD_USER, "Add a new user", Arrays.<BrokerRealArgument<?>>asList(
                new BrokerRealArgument<String>(resources.getRealResources(), "username", "Username", "The username for the new user", new StringType(resources.getClientResources())),
                new BrokerRealArgument<String>(resources.getRealResources(), "password", "Password", "The password for the new user", new StringType(resources.getClientResources()))
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
                resources.getStorage().saveDetails(users.getPath(), user.getId(), values);
            }
        };
    }

    @Override
    public BrokerRealCommand createRemoveUserCommand(final BrokerRealUser user) {
        return new BrokerRealCommand(resources.getRealResources(), User.REMOVE_COMMAND, User.REMOVE_COMMAND, "Remove the user", Lists.<BrokerRealArgument<?>>newArrayList()) {
                    @Override
                    public void perform(Map<String, String> values) throws HousemateException {
                        getResources().getRoot().getUsers().remove(user.getId());
                        resources.getStorage().removeDetails(user.getPath());
                    }
                };
    }

    @Override
    public RealCommand createAddDeviceCommand(final RealList<DeviceWrappable, RealDevice> devices) {
        final Map<RealDevice, ListenerRegistration> listeners = Maps.newHashMap();
        devices.addObjectListener(new ListListener<RealDevice>() {
            @Override
            public void elementAdded(RealDevice device) {
                listeners.put(device, device.getRunningValue().addObjectListener(runningListener));
            }

            @Override
            public void elementRemoved(RealDevice device) {
                ListenerRegistration registration = listeners.remove(device);
                if(registration != null)
                    registration.removeListener();
            }
        });
        return resources.getDeviceFactory().createAddDeviceCommand(Root.ADD_DEVICE, Root.ADD_DEVICE, "Add a new device", devices);
    }

    @Override
    public BrokerRealCommand createAddRuleCommand(final BrokerRealList<RuleWrappable, BrokerRealRule> rules) {
        return new BrokerRealCommand(resources.getRealResources(), Root.ADD_RULE, Root.ADD_RULE, "Add a new rule", Arrays.<BrokerRealArgument<?>>asList(
                new BrokerRealArgument<String>(resources.getRealResources(), "name", "Name", "The name for the new rule", new StringType(resources.getClientResources())),
                new BrokerRealArgument<String>(resources.getRealResources(), "description", "Description", "The description for the new rule", new StringType(resources.getClientResources()))
        )) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                values.put("id", values.get("name")); // todo figure out a better way of getting an id
                BrokerRealRule rule = new BrokerRealRule(getResources(), values.get("id"), values.get("name"), values.get("description"));
                rules.add(rule);
                resources.getStorage().saveDetails(rules.getPath(), rule.getId(), values);
                rule.getRunningValue().addObjectListener(runningListener);
            }
        };
    }

    @Override
    public void ruleRemoved(String[] path) {
        try {
            resources.getStorage().removeDetails(path);
        } catch(HousemateException e) {
            resources.getLog().e("Failed to remove stored details for rule " + Arrays.toString(path));
        }
    }

    @Override
    public <PO extends BrokerProxyPrimaryObject<?, ?, ?>> BrokerRealCommand createRemovePrimaryObjectCommand(
                final Command<?, ?> originalCommand,
                final PO source,
                final BrokerProxyPrimaryObject.Remover<PO> remover) {
        return new BrokerRealCommand(resources.getRealResources(), originalCommand.getId(), originalCommand.getName(), originalCommand.getDescription(),
                new ArrayList<BrokerRealArgument<?>>()) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                originalCommand.perform(new HashMap<String, String>(), new CommandListener<Command<?, ?>>() {
                    @Override
                    public void commandStarted(Command<?, ?> command) {
                        // do nothing
                    }

                    @Override
                    public void commandFinished(Command<?, ?> command) {
                        remover.remove(source);
                    }

                    @Override
                    public void commandFailed(Command<?, ?> command, String error) {
                        getLog().d("Failed to remove device on client: " + error);
                        remover.remove(source);
                    }
                });
                try {
                    resources.getStorage().removeDetails(source.getPath());
                } catch(HousemateException e) {
                    getResources().getLog().e("Failed to remove stored details for primary object " + Arrays.toString(source.getPath()));
                }
            }
        };
    }

    @Override
    public BrokerRealCommand createAddConditionCommand(BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions) {
        return resources.getConditionFactory().createAddConditionCommand(Rule.ADD_CONDITION, Rule.ADD_CONDITION, "Add a new condition", conditions);
    }

    @Override
    public BrokerRealCommand createAddSatisfiedConsequenceCommand(BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> consequences) {
        return resources.getConsequenceFactory().createAddConsequenceCommand(Rule.ADD_SATISFIED_CONSEQUENCE, Rule.ADD_SATISFIED_CONSEQUENCE, "Add a new satisfied consequence", consequences);
    }

    @Override
    public BrokerRealCommand createAddUnsatisfiedConsequenceCommand(BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> consequences) {
        return resources.getConsequenceFactory().createAddConsequenceCommand(Rule.ADD_UNSATISFIED_CONSEQUENCE, Rule.ADD_UNSATISFIED_CONSEQUENCE, "Add a new unsatisfied consequence", consequences);
    }
}
