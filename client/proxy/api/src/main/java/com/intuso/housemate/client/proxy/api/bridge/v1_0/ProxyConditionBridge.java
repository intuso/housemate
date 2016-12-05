package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.ConditionMapper;
import com.intuso.housemate.client.api.internal.*;
import com.intuso.housemate.client.api.internal.Runnable;
import com.intuso.housemate.client.api.internal.object.Condition;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyConditionBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Condition.Data, Condition.Data, Condition.Listener<? super ProxyConditionBridge>>
        implements Condition<ProxyCommandBridge, ProxyCommandBridge, ProxyValueBridge, ProxyPropertyBridge, ProxyValueBridge, ProxyValueBridge, ProxyListBridge<ProxyPropertyBridge>, ProxyCommandBridge, ProxyListBridge<ProxyConditionBridge>, ProxyConditionBridge> {

    private final ProxyCommandBridge renameCommand;
    private final ProxyCommandBridge removeCommand;
    private final ProxyValueBridge errorValue;
    private final ProxyPropertyBridge driverProperty;
    private final ProxyValueBridge driverLoadedValue;
    private final ProxyValueBridge satisfiedValue;
    private final ProxyListBridge<ProxyPropertyBridge> properties;
    private final ProxyCommandBridge addConditionCommand;
    private final ProxyListBridge<ProxyConditionBridge> conditions;

    @Inject
    protected ProxyConditionBridge(@Assisted Logger logger,
                                   ConditionMapper conditionMapper,
                                   Factory<ProxyCommandBridge> commandFactory,
                                   Factory<ProxyValueBridge> valueFactory,
                                   Factory<ProxyPropertyBridge> propertyFactory,
                                   Factory<ProxyListBridge<ProxyPropertyBridge>> propertiesFactory,
                                   Factory<ProxyListBridge<ProxyConditionBridge>> conditionsFactory,
                                   ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Condition.Data.class, conditionMapper, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        satisfiedValue = valueFactory.create(ChildUtil.logger(logger, Condition.SATISFIED_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Condition.PROPERTIES_ID));
        addConditionCommand = commandFactory.create(ChildUtil.logger(logger, Condition.ADD_CONDITION_ID));
        conditions = conditionsFactory.create(ChildUtil.logger(logger, Condition.CONDITIONS_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        renameCommand.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Renameable.RENAME_ID),
                ChildUtil.name(internalName, Renameable.RENAME_ID),
                connection);
        removeCommand.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Removeable.REMOVE_ID),
                ChildUtil.name(internalName, Removeable.REMOVE_ID),
                connection);
        errorValue.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.Failable.ERROR_ID),
                ChildUtil.name(internalName, Failable.ERROR_ID),
                connection);
        driverProperty.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_ID),
                connection);
        driverLoadedValue.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.UsesDriver.DRIVER_LOADED_ID),
                ChildUtil.name(internalName, UsesDriver.DRIVER_LOADED_ID),
                connection);
        satisfiedValue.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Condition.SATISFIED_ID),
                ChildUtil.name(internalName, Runnable.RUNNING_ID),
                connection);
        properties.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Condition.PROPERTIES_ID),
                ChildUtil.name(internalName, Condition.PROPERTIES_ID),
                connection);
        addConditionCommand.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Condition.ADD_CONDITION_ID),
                ChildUtil.name(internalName, Condition.ADD_CONDITION_ID),
                connection);
        conditions.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Condition.CONDITIONS_ID),
                ChildUtil.name(internalName, Condition.CONDITIONS_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        satisfiedValue.uninit();
        properties.uninit();
        addConditionCommand.uninit();
        conditions.uninit();
    }

    @Override
    public ProxyCommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public ProxyCommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public ProxyValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public ProxyPropertyBridge getDriverProperty() {
        return driverProperty;
    }

    @Override
    public ProxyValueBridge getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public ProxyValueBridge getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public ProxyListBridge<ProxyPropertyBridge> getProperties() {
        return properties;
    }

    @Override
    public ProxyCommandBridge getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public ProxyListBridge<ProxyConditionBridge> getConditions() {
        return conditions;
    }
}
