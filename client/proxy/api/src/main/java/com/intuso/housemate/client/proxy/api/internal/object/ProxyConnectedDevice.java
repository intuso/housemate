package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.ConnectedDevice;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.housemate.client.proxy.api.internal.ProxyRenameable;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Base interface for all proxy features
 * @param <FEATURE> the feature type
 */
public abstract class ProxyConnectedDevice<COMMAND extends ProxyCommand<?, ?, ?>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        PROPERTIES extends ProxyList<? extends ProxyProperty<?, ?, ?>, ?>,
        FEATURE extends ProxyConnectedDevice<COMMAND, COMMANDS, VALUES, PROPERTIES, FEATURE>>
        extends ProxyObject<ConnectedDevice.Data, ConnectedDevice.Listener<? super FEATURE>>
        implements ConnectedDevice<COMMAND, COMMANDS, VALUES, PROPERTIES, FEATURE>,
        ProxyRenameable<COMMAND> {

    private final COMMAND renameCommand;
    private final COMMANDS commands;
    private final VALUES values;
    private final PROPERTIES properties;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyConnectedDevice(Logger logger,
                                ManagedCollectionFactory managedCollectionFactory,
                                Factory<COMMAND> commandFactory,
                                Factory<COMMANDS> commandsFactory,
                                Factory<VALUES> valuesFactory,
                                Factory<PROPERTIES> propertiesFactory) {
        super(logger, ConnectedDevice.Data.class, managedCollectionFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID));
        properties = propertiesFactory.create(ChildUtil.logger(logger, PROPERTIES_ID));
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, RENAME_ID), connection);
        commands.init(ChildUtil.name(name, COMMANDS_ID), connection);
        values.init(ChildUtil.name(name, VALUES_ID), connection);
        properties.init(ChildUtil.name(name, PROPERTIES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
        properties.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public final COMMANDS getCommands() {
        return commands;
    }

    @Override
    public final VALUES getValues() {
        return values;
    }

    @Override
    public final PROPERTIES getProperties() {
        return properties;
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(COMMANDS_ID.equals(id))
            return commands;
        else if(PROPERTIES_ID.equals(id))
            return properties;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }
}
