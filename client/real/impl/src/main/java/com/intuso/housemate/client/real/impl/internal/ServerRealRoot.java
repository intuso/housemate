package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.impl.internal.factory.automation.AddAutomationCommand;
import com.intuso.housemate.client.real.impl.internal.factory.condition.ConditionFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.real.impl.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.task.TaskFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.user.AddUserCommand;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.Router;
import com.intuso.housemate.comms.api.internal.payload.AutomationData;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.UserData;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;

/**
 * Created by tomc on 20/03/15.
 */
public class ServerRealRoot
        extends BasicRealRoot
        implements RealApplication.Container,
        RealAutomation.Container,
        RealUser.Container,
        AddAutomationCommand.Callback,
        AddUserCommand.Callback {

    public final static String APPLICATIONS_ID = "applications";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String USERS_ID = "users";
    public final static String ADD_AUTOMATION_ID = "add-automation";
    public final static String ADD_USER_ID = "add-user";

    private final RealList<RealApplication> applications;
    private final RealList<RealAutomation> automations;
    private final RealList<RealUser> users;

    private final RealAutomation.Factory automationFactory;
    private final RealUser.Factory userFactory;

    private final RealCommandImpl addAutomationCommand;
    private final RealCommandImpl addUserCommand;

    @Inject
    public ServerRealRoot(ListenersFactory listenersFactory,
                          PropertyRepository properties,
                          Router<?> router,
                          AddHardwareCommand.Factory addHardwareCommandFactory,
                          AddDeviceCommand.Factory addDeviceCommandFactory,
                          AddAutomationCommand.Factory addAutomationCommandFactory,
                          AddUserCommand.Factory addUserCommandFactory,
                          ConditionFactoryType conditionFactoryType,
                          DeviceFactoryType deviceFactoryType,
                          HardwareFactoryType hardwareFactoryType,
                          TaskFactoryType taskFactoryType,
                          RealAutomation.Factory automationFactory,
                          RealDevice.Factory deviceFactory,
                          RealHardware.Factory hardwareFactory,
                          RealUser.Factory userFactory) {
        super(listenersFactory, properties, router, addHardwareCommandFactory, addDeviceCommandFactory,
                deviceFactoryType, hardwareFactoryType, deviceFactory, hardwareFactory);
        this.applications = (RealList)new RealListImpl<>(LoggerUtil.child(getLogger(), APPLICATIONS_ID), listenersFactory, APPLICATIONS_ID, "Applications", "Applications");
        this.automations = (RealList)new RealListImpl<>(LoggerUtil.child(getLogger(), AUTOMATIONS_ID), listenersFactory, AUTOMATIONS_ID, "Automations", "Automations");
        this.users = (RealList)new RealListImpl<>(LoggerUtil.child(getLogger(), USERS_ID), listenersFactory, USERS_ID, "Users", "Users");

        this.automationFactory = automationFactory;
        this.userFactory = userFactory;

        this.addAutomationCommand = addAutomationCommandFactory.create(LoggerUtil.child(LoggerUtil.child(getLogger(), ADD_AUTOMATION_ID), ADD_AUTOMATION_ID), ADD_AUTOMATION_ID, ADD_AUTOMATION_ID, "Add an automation", this, this);
        this.addUserCommand = addUserCommandFactory.create(LoggerUtil.child(LoggerUtil.child(getLogger(), ADD_USER_ID), ADD_USER_ID), ADD_USER_ID, ADD_USER_ID, "Add a user", this, this);

        addChild((RealListImpl<?, ?>)applications);
        ((RealListImpl<?, ?>) applications).init(this);
        addChild((RealListImpl<?, ?>) automations);
        ((RealListImpl<?, ?>) automations).init(this);
        addChild((RealListImpl<?, ?>)users);
        ((RealListImpl<?, ?>) users).init(this);

        addChild(addAutomationCommand);
        addAutomationCommand.init(this);
        addChild(addUserCommand);
        addUserCommand.init(this);

        addType(conditionFactoryType);
        conditionFactoryType.init((RealListImpl<?, ?>)getTypes());
        addType(taskFactoryType);
        taskFactoryType.init((RealListImpl<?, ?>)getTypes());
    }

    @Override
    public void sendMessage(Message<?> message) {
        if(message.getPayload() instanceof HousemateData)
            ((Message)message).setPayload(((HousemateData<?>) message.getPayload()).deepClone());
        super.sendMessage(message);
    }

    @Override
    public RealList<RealApplication> getApplications() {
        return applications;
    }

    @Override
    public void addApplication(RealApplication application) {
        applications.add(application);
    }

    @Override
    public void removeApplication(RealApplication application) {
        applications.remove(application.getId());
    }

    @Override
    public RealList<RealAutomation> getAutomations() {
        return automations;
    }

    @Override
    public final void addAutomation(RealAutomation automation) {
        automations.add(automation);
    }

    @Override
    public RealAutomation createAndAddAutomation(AutomationData data) {
        RealAutomation automation = automationFactory.create(LoggerUtil.child(getLogger(), AUTOMATIONS_ID, data.getId()), data, this);
        addAutomation(automation);
        return automation;
    }

    @Override
    public final void removeAutomation(RealAutomation realAutomation) {
        automations.remove(realAutomation.getId());
    }

    public RealCommand getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public RealList<RealUser> getUsers() {
        return users;
    }

    @Override
    public final void addUser(RealUser user) {
        users.add(user);
    }

    @Override
    public RealUser createAndAddUser(UserData data) {
        RealUser user = userFactory.create(LoggerUtil.child(getLogger(), USERS_ID, data.getId()), data, this);
        addUser(user);
        return user;
    }

    @Override
    public void removeUser(RealUser user) {
        users.remove(user.getId());
    }

    public RealCommand getAddUserCommand() {
        return addUserCommand;
    }
}
