package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Task;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyFailable;
import com.intuso.housemate.client.proxy.internal.ProxyRemoveable;
import com.intuso.housemate.client.proxy.internal.ProxyUsesDriver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
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
                     ManagedCollectionFactory managedCollectionFactory,
                     ProxyObject.Factory<COMMAND> commandFactory,
                     ProxyObject.Factory<VALUE> valueFactory,
                     ProxyObject.Factory<PROPERTY> propertyFactory,
                     ProxyObject.Factory<PROPERTIES> propertiesFactory) {
        super(logger, Task.Data.class, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        removeCommand = commandFactory.create(ChildUtil.logger(logger, REMOVE_ID));
        errorValue = valueFactory.create(ChildUtil.logger(logger, ERROR_ID));
        driverProperty = propertyFactory.create(ChildUtil.logger(logger, DRIVER_ID));
        driverLoadedValue = valueFactory.create(ChildUtil.logger(logger, DRIVER_LOADED_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID));
        executingValue = valueFactory.create(ChildUtil.logger(logger, EXECUTING_ID));
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
        executingValue.init(ChildUtil.name(name, EXECUTING_ID), connection);
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
        else if(EXECUTING_ID.equals(id))
            return executingValue;
        return null;
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:21
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxyTask<
            ProxyCommand.Simple,
            ProxyValue.Simple,
            ProxyProperty.Simple,
            ProxyList.Simple<ProxyProperty.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyValue.Simple> valueFactory,
                      Factory<ProxyProperty.Simple> propertyFactory,
                      Factory<ProxyList.Simple<ProxyProperty.Simple>> propertiesFactory) {
            super(logger, managedCollectionFactory, commandFactory, valueFactory, propertyFactory, propertiesFactory);
        }
    }
}
