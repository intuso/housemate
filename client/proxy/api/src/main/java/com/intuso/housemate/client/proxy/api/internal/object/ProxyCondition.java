package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Condition;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.housemate.client.proxy.api.internal.ProxyFailable;
import com.intuso.housemate.client.proxy.api.internal.ProxyRemoveable;
import com.intuso.housemate.client.proxy.api.internal.ProxyUsesDriver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <COMMAND> the type of the add command
 * @param <CONDITION> the type of the conditions
 * @param <CONDITIONS> the type of the conditions list
 */
public abstract class ProxyCondition<
        COMMAND extends ProxyCommand<?, ?, COMMAND>,
        VALUE extends ProxyValue<?, VALUE>,
        PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        CONDITION extends ProxyCondition<COMMAND, VALUE, PROPERTY, PROPERTIES, CONDITION, CONDITIONS>,
        CONDITIONS extends ProxyList<CONDITION, ?>>
        extends ProxyObject<Condition.Data, Condition.Listener<? super CONDITION>>
        implements Condition<COMMAND, COMMAND, VALUE, PROPERTY, VALUE, VALUE, PROPERTIES, COMMAND, CONDITIONS, CONDITION>,
        ProxyRemoveable<COMMAND>,
        ProxyFailable<VALUE>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final VALUE errorValue;
    private final PROPERTY driverProperty;
    private final VALUE driverLoadedValue;
    private final PROPERTIES properties;
    private final CONDITIONS conditions;
    private final COMMAND addConditionCommand;
    private final VALUE satisfiedValue;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyCondition(Logger logger,
                          ManagedCollectionFactory managedCollectionFactory,
                          ProxyObject.Factory<COMMAND> commandFactory,
                          ProxyObject.Factory<VALUE> valueFactory,
                          ProxyObject.Factory<PROPERTY> propertyFactory,
                          ProxyObject.Factory<PROPERTIES> propertiesFactory,
                          ProxyObject.Factory<CONDITIONS> conditionsFactory) {
        super(logger, Condition.Data.class, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID));
        conditions = conditionsFactory.create(ChildUtil.logger(logger, CONDITIONS_ID));
        addConditionCommand = commandFactory.create(ChildUtil.logger(logger, ADD_CONDITION_ID));
        satisfiedValue = valueFactory.create(ChildUtil.logger(logger, SATISFIED_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, REMOVE_ID), connection);
        errorValue.init(ChildUtil.name(name, ERROR_ID), connection);
        driverProperty.init(ChildUtil.name(name, DRIVER_ID), connection);
        driverLoadedValue.init(ChildUtil.name(name, DRIVER_LOADED_ID), connection);
        properties.init(ChildUtil.name(name, PROPERTIES_ID), connection);
        conditions.init(ChildUtil.name(name, CONDITIONS_ID), connection);
        addConditionCommand.init(ChildUtil.name(name, ADD_CONDITION_ID), connection);
        satisfiedValue.init(ChildUtil.name(name, SATISFIED_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        removeCommand.uninit();
        errorValue.uninit();
        driverProperty.uninit();
        driverLoadedValue.uninit();
        properties.uninit();
        conditions.uninit();
        addConditionCommand.uninit();
        satisfiedValue.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public COMMAND getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public final String getError() {
        return errorValue.getValue() != null ? errorValue.getValue().getFirstValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return errorValue;
    }

    @Override
    public PROPERTY getDriverProperty() {
        return driverProperty;
    }

    @Override
    public final boolean isDriverLoaded() {
        return driverLoadedValue.getValue() != null
                && driverLoadedValue.getValue().getFirstValue() != null
                && Boolean.parseBoolean(driverLoadedValue.getValue().getFirstValue());
    }

    @Override
    public VALUE getDriverLoadedValue() {
        return driverLoadedValue;
    }

    @Override
    public final PROPERTIES getProperties() {
        return properties;
    }

    @Override
    public COMMAND getAddConditionCommand() {
        return addConditionCommand;
    }

    @Override
    public CONDITIONS getConditions() {
        return conditions;
    }

    public final boolean isSatisfied() {
        return satisfiedValue.getValue() != null && satisfiedValue.getValue().getFirstValue() != null
                ? Boolean.parseBoolean(satisfiedValue.getValue().getFirstValue()) : false;
    }

    @Override
    public final VALUE getSatisfiedValue() {
        return satisfiedValue;
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(REMOVE_ID.equals(id))
            return removeCommand;
        else if(ERROR_ID.equals(id))
            return errorValue;
        else if(DRIVER_ID.equals(id))
            return driverProperty;
        else if(DRIVER_LOADED_ID.equals(id))
            return driverLoadedValue;
        else if(PROPERTIES_ID.equals(id))
            return properties;
        else if(CONDITIONS_ID.equals(id))
            return conditions;
        else if(ADD_CONDITION_ID.equals(id))
            return addConditionCommand;
        else if(SATISFIED_ID.equals(id))
            return satisfiedValue;
        return null;
    }
}
