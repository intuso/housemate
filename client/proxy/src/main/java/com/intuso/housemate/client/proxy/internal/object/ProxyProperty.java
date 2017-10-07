package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.object.view.CommandView;
import com.intuso.housemate.client.proxy.internal.object.view.PropertyView;
import com.intuso.housemate.client.proxy.internal.object.view.View;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <TYPE> the type of the type
 * @param <COMMAND> the type of the command
 * @param <PROPERTY> the type of the property
 */
public abstract class ProxyProperty<TYPE extends ProxyType<?>,
        COMMAND extends ProxyCommand<?, ?, COMMAND>,
        PROPERTY extends ProxyProperty<TYPE, COMMAND, PROPERTY>>
        extends ProxyValueBase<Property.Data, Property.Listener<? super PROPERTY>, PropertyView, TYPE, PROPERTY>
        implements Property<Type.Instances, TYPE, COMMAND, PROPERTY> {

    private final ProxyObject.Factory<COMMAND> commandFactory;

    private COMMAND setCommand;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyProperty(Logger logger,
                         String name,
                         ManagedCollectionFactory managedCollectionFactory,
                         Receiver.Factory receiverFactory,
                         ProxyObject.Factory<COMMAND> commandFactory) {
        super(logger, name, Property.Data.class, managedCollectionFactory, receiverFactory);
        this.commandFactory = commandFactory;
    }

    @Override
    public PropertyView createView() {
        return new PropertyView();
    }

    @Override
    public void view(PropertyView view) {
        super.view(view);

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if (setCommand == null)
                    setCommand = commandFactory.create(ChildUtil.logger(logger, SET_COMMAND_ID), ChildUtil.name(name, SET_COMMAND_ID));
                break;
            case SELECTION:
                if (setCommand == null && view.getSetCommandView() != null)
                    setCommand = commandFactory.create(ChildUtil.logger(logger, SET_COMMAND_ID), ChildUtil.name(name, SET_COMMAND_ID));
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                setCommand.view(new CommandView(View.Mode.ANCESTORS));
            case CHILDREN:
            case SELECTION:
                if (view.getSetCommandView() != null)
                    setCommand.view(view.getSetCommandView());
        }
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(setCommand != null)
            setCommand.uninit();
    }

    @Override
    public COMMAND getSetCommand() {
        return setCommand;
    }

    @Override
    public void set(final Type.Instances value, Command.PerformListener<? super COMMAND> listener) {
        Type.InstanceMap values = new Type.InstanceMap();
        values.getChildren().put(VALUE_ID, value);
        getSetCommand().perform(values, listener);
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(SET_COMMAND_ID.equals(id)) {
            if (setCommand == null)
                setCommand = commandFactory.create(ChildUtil.logger(logger, SET_COMMAND_ID), ChildUtil.name(name, SET_COMMAND_ID));
            return setCommand;
        }
        return null;
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:17
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxyProperty<
            ProxyType.Simple,
            ProxyCommand.Simple,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, commandFactory);
        }
    }
}
