package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.DeviceComponent;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.DeviceComponentView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/*
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the hardware
 */
public abstract class ProxyDeviceComponent<
        COMMANDS extends ProxyList<? extends ProxyCommand<?, ?, ?>, ?>,
        VALUES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        DEVICE_COMPONENT extends ProxyDeviceComponent<COMMANDS, VALUES, DEVICE_COMPONENT>>
        extends ProxyObject<DeviceComponent.Data, DeviceComponent.Listener<? super DEVICE_COMPONENT>, DeviceComponentView>
        implements DeviceComponent<COMMANDS, VALUES, DEVICE_COMPONENT> {

    private final Factory<COMMANDS> commandsFactory;
    private final Factory<VALUES> valuesFactory;

    private COMMANDS commands;
    private VALUES values;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDeviceComponent(Logger logger,
                                String path,
                                String name,
                                ManagedCollectionFactory managedCollectionFactory,
                                Receiver.Factory receiverFactory,
                                Factory<COMMANDS> commandsFactory,
                                Factory<VALUES> valuesFactory) {
        super(logger, path, name, DeviceComponent.Data.class, managedCollectionFactory, receiverFactory);
        this.commandsFactory = commandsFactory;
        this.valuesFactory = valuesFactory;
    }

    @Override
    public Set<String> getClasses() {
        return getData().getClasses();
    }

    @Override
    public Set<String> getAbilities() {
        return getData().getAbilities();
    }

    @Override
    public DeviceComponentView createView(View.Mode mode) {
        return new DeviceComponentView(mode);
    }

    @Override
    public Tree getTree(DeviceComponentView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

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
                    result.getChildren().put(COMMANDS_ID, commands.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(VALUES_ID, values.getTree(new ListView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(COMMANDS_ID, commands.getTree(view.getCommands(), referenceHandler, listener, listenerRegistrations));
                    result.getChildren().put(VALUES_ID, values.getTree(view.getValues(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
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
    public void load(DeviceComponentView view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(commands == null)
                    commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID), ChildUtil.path(path, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
                if(values == null)
                    values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.path(path, VALUES_ID), ChildUtil.name(name, VALUES_ID));
                break;
            case SELECTION:
                if(commands == null && view.getCommands() != null)
                    commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID), ChildUtil.path(path, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
                if(values == null && view.getValues() != null)
                    values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.path(path, VALUES_ID), ChildUtil.name(name, VALUES_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                commands.load(new ListView(View.Mode.ANCESTORS));
                values.load(new ListView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getCommands() != null)
                    commands.load(view.getCommands());
                if(view.getValues() != null)
                    values.load(view.getValues());
                break;
        }
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(commands != null)
            commands.uninit();
        if(values != null)
            values.uninit();
    }

    @Override
    public COMMANDS getCommands() {
        return commands;
    }

    @Override
    public VALUES getValues() {
        return values;
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(COMMANDS_ID.equals(id)) {
            if(commands == null)
                commands = commandsFactory.create(ChildUtil.logger(logger, COMMANDS_ID),  ChildUtil.path(path, COMMANDS_ID), ChildUtil.name(name, COMMANDS_ID));
            return commands;
        } else if(VALUES_ID.equals(id)) {
            if (values == null)
                values = valuesFactory.create(ChildUtil.logger(logger, VALUES_ID), ChildUtil.path(path, VALUES_ID), ChildUtil.name(name, VALUES_ID));
            return values;
        }
        return null;
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:21
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyDeviceComponent<
                    ProxyList.Simple<ProxyCommand.Simple>,
                    ProxyList.Simple<ProxyValue.Simple>,
                    Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted("path") String path,
                      @Assisted("name") String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyList.Simple<ProxyCommand.Simple>> commandsFactory,
                      Factory<ProxyList.Simple<ProxyValue.Simple>> valuesFactory) {
            super(logger, path, name, managedCollectionFactory, receiverFactory, commandsFactory, valuesFactory);
        }
    }
}
