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
        DEVICE_COMPONENTS extends ProxyList<? extends ProxyDeviceComponent<?, ?, ?>, ?>,
        DEVICE extends ProxyDevice<DATA, LISTENER, VIEW, COMMAND, DEVICE_COMPONENTS, DEVICE>>
        extends ProxyObject<DATA, LISTENER, VIEW>
        implements Device<DATA, LISTENER, COMMAND, DEVICE_COMPONENTS, VIEW, DEVICE>,
        ProxyRenameable<COMMAND> {

    private final ProxyObject.Factory<COMMAND> commandFactory;
    private final ProxyObject.Factory<DEVICE_COMPONENTS> componentsFactory;

    private COMMAND renameCommand;
    private DEVICE_COMPONENTS components;

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
                       Factory<DEVICE_COMPONENTS> componentsFactory) {
        super(logger, path, name, dataClass, managedCollectionFactory, receiverFactory);
        this.commandFactory = commandFactory;
        this.componentsFactory = componentsFactory;
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
                    result.getChildren().put(DEVICE_COMPONENTS_ID, components.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(DEVICE_COMPONENTS_ID, components.getTree(view.getComponents(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getRenameCommand() != null)
                        result.getChildren().put(RENAME_ID, renameCommand.getTree(view.getRenameCommand(), referenceHandler, listener, listenerRegistrations));
                    if(view.getComponents() != null)
                        result.getChildren().put(DEVICE_COMPONENTS_ID, components.getTree(view.getComponents(), referenceHandler, listener, listenerRegistrations));
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
                if(components == null)
                    components = componentsFactory.create(ChildUtil.logger(logger, DEVICE_COMPONENTS_ID), ChildUtil.path(path, DEVICE_COMPONENTS_ID), ChildUtil.name(name, DEVICE_COMPONENTS_ID));
                break;
            case SELECTION:
                if(renameCommand == null && view.getRenameCommand() != null)
                    renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
                if(components == null && view.getComponents() != null)
                    components = componentsFactory.create(ChildUtil.logger(logger, DEVICE_COMPONENTS_ID), ChildUtil.path(path, DEVICE_COMPONENTS_ID), ChildUtil.name(name, DEVICE_COMPONENTS_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                renameCommand.load(new CommandView(View.Mode.ANCESTORS));
                components.load(new ListView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getRenameCommand() != null)
                    renameCommand.load(view.getRenameCommand());
                if(view.getComponents() != null)
                    components.load(view.getComponents());
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
        if(components != null)
            components.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public final DEVICE_COMPONENTS getDeviceComponents() {
        return components;
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
        if(RENAME_ID.equals(id)) {
            if(renameCommand == null)
                renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID), ChildUtil.path(path, RENAME_ID), ChildUtil.name(name, RENAME_ID));
            return renameCommand;
        } else if(DEVICE_COMPONENTS_ID.equals(id)) {
            if(components == null)
                components = componentsFactory.create(ChildUtil.logger(logger, DEVICE_COMPONENTS_ID), ChildUtil.path(path, DEVICE_COMPONENTS_ID), ChildUtil.name(name, DEVICE_COMPONENTS_ID));
            return components;
        }
        return null;
    }
}
