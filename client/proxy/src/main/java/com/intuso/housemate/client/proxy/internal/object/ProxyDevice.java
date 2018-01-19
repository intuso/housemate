package com.intuso.housemate.client.proxy.internal.object;

import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyRenameable;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * Base interface for all proxy features
 */
public abstract class ProxyDevice<
        DATA extends Device.Data,
        LISTENER extends Device.Listener<? super DEVICE>,
        VIEW extends DeviceView<?>,
        COMMAND extends ProxyCommand<?, ?, ?>,
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        DEVICE extends ProxyDevice<DATA, LISTENER, VIEW, COMMAND, COMMANDS, VALUES, DEVICE>>
        extends ProxyObject<DATA, LISTENER, VIEW>
        implements Device<DATA, LISTENER, COMMAND, COMMANDS, VALUES, VIEW, DEVICE>,
        ProxyRenameable<COMMAND> {

    private final ProxyObject.Factory<COMMAND> commandFactory;
    private final ProxyObject.Factory<COMMANDS> commandsFactory;
    private final ProxyObject.Factory<VALUES> valuesFactory;

    private COMMAND renameCommand;
    private COMMANDS commands;
    private VALUES values;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDevice(Logger logger,
                       String path,
                       String name,
                       Class<DATA> dataClass,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory,
                       Factory<COMMAND> commandFactory,
                       Factory<COMMANDS> commandsFactory,
                       Factory<VALUES> valuesFactory) {
        super(logger, path, name, dataClass, managedCollectionFactory, receiverFactory);
        this.commandFactory = commandFactory;
        this.commandsFactory = commandsFactory;
        this.valuesFactory = valuesFactory;
    }

    @Override
    public Tree getTree(VIEW view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(new CommandView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(COMMANDS_ID, commands.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(VALUES_ID, values.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getCommands() != null)
                        result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), referenceHandler, listener, listenerRegistrations));
                    if(view.getValues() != null)
                        result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), referenceHandler, listener, listenerRegistrations));
                    break;
            }
        }

        return result;
    }

    @Override
    public void load(VIEW view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(renameCommand == null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(commands == null)
                    commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID), ChildUtil.path(path, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
                if(values == null)
                    values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.path(path, VALUES_ID), ChildUtil.name(name, VALUES_ID));
                break;
            case SELECTION:
                if(renameCommand == null && view.getRenameCommand() != null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(commands == null && view.getCommands() != null)
                    commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID), ChildUtil.path(path, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
                if(values == null && view.getValues() != null)
                    values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.path(path, VALUES_ID), ChildUtil.name(name, VALUES_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                renameCommand.load(new CommandView(View.Mode.ANCESTORS));
                commands.load(new ListView(View.Mode.ANCESTORS));
                values.load(new ListView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getRenameCommand() != null)
                    renameCommand.load(view.getRenameCommand());
                if(view.getCommands() != null)
                    commands.load(view.getCommands());
                if(view.getValues() != null)
                    values.load(view.getValues());
                break;
        }
    }

    @Override
    public void loadRenameCommand(CommandView commandView) {
        if(renameCommand == null)
            renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
        renameCommand.load(commandView);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(renameCommand != null)
            renameCommand.uninit();
        if(commands != null)
            commands.uninit();
        if(values != null)
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
    public Object<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id)) {
            if(renameCommand == null)
                renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
            return renameCommand;
        } else if(COMMANDS_ID.equals(id)) {
            if(commands == null)
                commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID), ChildUtil.path(path, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
            return commands;
        } else if(VALUES_ID.equals(id)) {
            if(values == null)
                values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.path(path, VALUES_ID), ChildUtil.name(name, VALUES_ID));
            return values;
        }
        return null;
    }
}
