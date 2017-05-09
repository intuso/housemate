package com.intuso.housemate.client.proxy.internal.object;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyRenameable;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Base interface for all proxy features
 */
public abstract class ProxyDevice<
        DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        COMMAND extends ProxyCommand<?, ?, ?>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        DEVICE extends ProxyDevice<DATA, LISTENER, COMMAND, COMMANDS, VALUES, DEVICE>>
        extends ProxyObject<DATA, LISTENER>
        implements Device<LISTENER, COMMAND, COMMANDS, VALUES, DEVICE>,
        ProxyRenameable<COMMAND> {

    private final COMMAND renameCommand;
    private final COMMANDS commands;
    private final VALUES values;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDevice(Logger logger,
                       Class<DATA> dataClass,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory,
                       Factory<COMMAND> commandFactory,
                       Factory<COMMANDS> commandsFactory,
                       Factory<VALUES> valuesFactory) {
        super(logger, dataClass, managedCollectionFactory, receiverFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID));
        values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID));
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        renameCommand.init(ChildUtil.name(name, RENAME_ID));
        commands.init(ChildUtil.name(name, COMMANDS_ID));
        values.init(ChildUtil.name(name, VALUES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        commands.uninit();
        values.uninit();
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
    public Object<?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(COMMANDS_ID.equals(id))
            return commands;
        else if(VALUES_ID.equals(id))
            return values;
        return null;
    }
}
