package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.Failable;
import com.intuso.housemate.client.api.internal.Removeable;
import com.intuso.housemate.client.api.internal.Renameable;
import com.intuso.housemate.client.api.internal.UsesDriver;
import com.intuso.housemate.client.api.internal.object.Task;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.housemate.client.proxy.api.internal.ProxyFailable;
import com.intuso.housemate.client.proxy.api.internal.ProxyRemoveable;
import com.intuso.housemate.client.proxy.api.internal.ProxyUsesDriver;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * @param <VALUE> the type of the value
 * @param <PROPERTIES> the type of the properties list
 * @param <TASK> the type of the task
 */
public abstract class ProxyTask<
            COMMAND extends ProxyCommand<?, ?, COMMAND>,
            VALUE extends ProxyValue<?, VALUE>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
            TASK extends ProxyTask<COMMAND, VALUE, PROPERTY, PROPERTIES, TASK>>
        extends ProxyObject<Task.Data, Task.Listener<? super TASK>>
        implements Task<COMMAND, COMMAND, VALUE, PROPERTY, VALUE, VALUE, PROPERTIES, TASK>,
        ProxyFailable<VALUE>,
        ProxyRemoveable<COMMAND>,
        ProxyUsesDriver<PROPERTY, VALUE> {

    private final COMMAND renameCommand;
    private final COMMAND removeCommand;
    private final VALUE errorValue;
    private final PROPERTY driverProperty;
    private final VALUE driverLoadedValue;
    private final PROPERTIES properties;
    private final VALUE executingValue;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyTask(Logger logger,
                          ListenersFactory listenersFactory,
                          ProxyObject.Factory<COMMAND> commandFactory,
                          ProxyObject.Factory<VALUE> valueFactory,
                          ProxyObject.Factory<PROPERTY> propertyFactory,
                          ProxyObject.Factory<PROPERTIES> propertiesFactory) {
        super(logger, Task.Data.class, listenersFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, Renameable.RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, Removeable.REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, Failable.ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, UsesDriver.DRIVER_LOADED_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, Task.PROPERTIES_ID));
        executingValue = valueFactory.create(ChildUtil.logger(logger, Task.EXECUTING_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, Renameable.RENAME_ID), connection);
        removeCommand.init(ChildUtil.name(name, Removeable.REMOVE_ID), connection);
        errorValue.init(ChildUtil.name(name, Failable.ERROR_ID), connection);
        driverProperty.init(ChildUtil.name(name, UsesDriver.DRIVER_ID), connection);
        driverLoadedValue.init(ChildUtil.name(name, UsesDriver.DRIVER_LOADED_ID), connection);
        properties.init(ChildUtil.name(name, Task.PROPERTIES_ID), connection);
        executingValue.init(ChildUtil.name(name, Task.EXECUTING_ID), connection);
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
        executingValue.uninit();
    }

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

    public final boolean isExecuting() {
        VALUE executing = getExecutingValue();
        return executing.getValue() != null && executing.getValue().getFirstValue() != null
                ? Boolean.parseBoolean(executing.getValue().getFirstValue()) : false;
    }

    @Override
    public final VALUE getExecutingValue() {
        return executingValue;
    }
}
