package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.NodeView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <COMMAND> the type of the command
 * @param <TYPES> the type of the types list
 * @param <HARDWARES> the type of the hardwares list
 * @param <NODE> the type of the node
 */
public abstract class ProxyNode<
        COMMAND extends ProxyCommand<?, ?, ?>,
        TYPES extends ProxyList<? extends ProxyType<?>, ?>,
        HARDWARES extends ProxyList<? extends ProxyHardware<?, ?, ?, ?, ?, ?, ?, ?>, ?>,
        NODE extends ProxyNode<COMMAND, TYPES, HARDWARES, NODE>>
        extends ProxyObject<Node.Data, Node.Listener<? super NODE>, NodeView>
        implements Node<COMMAND, TYPES, HARDWARES, NODE> {

    private final ProxyObject.Factory<TYPES> typesFactory;
    private final ProxyObject.Factory<HARDWARES> hardwaresFactory;
    private final ProxyObject.Factory<COMMAND> commandFactory;

    private TYPES types;
    private HARDWARES hardwares;
    private COMMAND addHardwareCommand;

    public ProxyNode(Logger logger,
                     String name,
                     ManagedCollectionFactory managedCollectionFactory,
                     Receiver.Factory receiverFactory,
                     Factory<COMMAND> commandFactory,
                     Factory<TYPES> typesFactory,
                     Factory<HARDWARES> hardwaresFactory) {
        super(logger, name, Node.Data.class, managedCollectionFactory, receiverFactory);
        this.typesFactory = typesFactory;
        this.hardwaresFactory = hardwaresFactory;
        this.commandFactory = commandFactory;
    }

    @Override
    public NodeView createView(View.Mode mode) {
        return new NodeView(mode);
    }

    @Override
    public Tree getTree(NodeView view, ValueBase.Listener listener) {

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(TYPES_ID, types.getTree(new ListView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(HARDWARES_ID, hardwares.getTree(new ListView(View.Mode.ANCESTORS), listener));
                    result.getChildren().put(ADD_HARDWARE_ID, addHardwareCommand.getTree(new CommandView(View.Mode.ANCESTORS), listener));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(TYPES_ID, types.getTree(view.getTypes(), listener));
                    result.getChildren().put(HARDWARES_ID, hardwares.getTree(view.getHardwares(), listener));
                    result.getChildren().put(ADD_HARDWARE_ID, addHardwareCommand.getTree(view.getAddHardwareCommand(), listener));
                    break;

                case SELECTION:
                    if(view.getTypes() != null)
                        result.getChildren().put(TYPES_ID, types.getTree(view.getTypes(), listener));
                    if(view.getHardwares() != null)
                        result.getChildren().put(HARDWARES_ID, hardwares.getTree(view.getHardwares(), listener));
                    if(view.getAddHardwareCommand() != null)
                        result.getChildren().put(ADD_HARDWARE_ID, addHardwareCommand.getTree(view.getAddHardwareCommand(), listener));
                    break;
            }

        }

        return result;
    }

    @Override
    public void load(NodeView view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(types == null)
                    types = typesFactory.create(ChildUtil.logger(logger, TYPES_ID), ChildUtil.name(name, TYPES_ID));
                if(hardwares == null)
                    hardwares = hardwaresFactory.create(ChildUtil.logger(logger, HARDWARES_ID), ChildUtil.name(name, HARDWARES_ID));
                if(addHardwareCommand == null)
                    addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, ADD_HARDWARE_ID), ChildUtil.name(name, ADD_HARDWARE_ID));
                break;
            case SELECTION:
                if(types == null && view.getTypes() != null)
                    types = typesFactory.create(ChildUtil.logger(logger, TYPES_ID), ChildUtil.name(name, TYPES_ID));
                if(hardwares == null && view.getHardwares() != null)
                    hardwares = hardwaresFactory.create(ChildUtil.logger(logger, HARDWARES_ID), ChildUtil.name(name, HARDWARES_ID));
                if(addHardwareCommand == null && view.getAddHardwareCommand() != null)
                    addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, ADD_HARDWARE_ID), ChildUtil.name(name, ADD_HARDWARE_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                types.load(new ListView(View.Mode.ANCESTORS));
                hardwares.load(new ListView(View.Mode.ANCESTORS));
                addHardwareCommand.load(new CommandView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getTypes() != null)
                    types.load(view.getTypes());
                if(view.getHardwares() != null)
                    hardwares.load(view.getHardwares());
                if(view.getAddHardwareCommand() != null)
                    addHardwareCommand.load(view.getAddHardwareCommand());
                break;
        }
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(types != null)
            types.uninit();
        if(hardwares != null)
            hardwares.uninit();
        if(addHardwareCommand != null)
            addHardwareCommand.uninit();
    }

    @Override
    public TYPES getTypes() {
        return types;
    }

    @Override
    public HARDWARES getHardwares() {
        return hardwares;
    }

    @Override
    public COMMAND getAddHardwareCommand() {
        return addHardwareCommand;
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        if(ADD_HARDWARE_ID.equals(id)) {
            if(addHardwareCommand == null)
                addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, ADD_HARDWARE_ID), ChildUtil.name(name, ADD_HARDWARE_ID));
            return addHardwareCommand;
        } else if(HARDWARES_ID.equals(id)) {
            if(hardwares == null)
                hardwares = hardwaresFactory.create(ChildUtil.logger(logger, HARDWARES_ID), ChildUtil.name(name, HARDWARES_ID));
            return hardwares;
        } else if(TYPES_ID.equals(id)) {
            if(types == null)
                types = typesFactory.create(ChildUtil.logger(logger, TYPES_ID), ChildUtil.name(name, TYPES_ID));
            return types;
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
    public static final class Simple extends ProxyNode<
            ProxyCommand.Simple,
            ProxyList.Simple<ProxyType.Simple>,
            ProxyList.Simple<ProxyHardware.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyType.Simple>> typesFactory,
                      Factory<ProxyList.Simple<ProxyHardware.Simple>> hardwaresFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, commandFactory, typesFactory, hardwaresFactory);
        }
    }
}
