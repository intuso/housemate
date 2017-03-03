package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Node;
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
        extends ProxyObject<Node.Data, Node.Listener<? super NODE>>
        implements Node<COMMAND, TYPES, HARDWARES, NODE> {

    private final TYPES types;
    private final HARDWARES hardwares;
    private final COMMAND addHardwareCommand;

    public ProxyNode(Logger logger,
                     ManagedCollectionFactory managedCollectionFactory,
                     Receiver.Factory receiverFactory,
                     Factory<COMMAND> commandFactory,
                     Factory<TYPES> typesFactory,
                     Factory<HARDWARES> hardwaresFactory) {
        super(logger, Node.Data.class, managedCollectionFactory, receiverFactory);
        types = typesFactory.create(ChildUtil.logger(logger, TYPES_ID));
        hardwares = hardwaresFactory.create(ChildUtil.logger(logger, HARDWARES_ID));
        addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, ADD_HARDWARE_ID));
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        types.init(ChildUtil.name(name, TYPES_ID));
        hardwares.init(ChildUtil.name(name, HARDWARES_ID));
        addHardwareCommand.init(ChildUtil.name(name, ADD_HARDWARE_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        types.uninit();
        hardwares.uninit();
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
    public ProxyObject<?, ?> getChild(String id) {
        if(ADD_HARDWARE_ID.equals(id))
            return addHardwareCommand;
        else if(HARDWARES_ID.equals(id))
            return hardwares;
        else if(TYPES_ID.equals(id))
            return types;
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
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyType.Simple>> typesFactory,
                      Factory<ProxyList.Simple<ProxyHardware.Simple>> hardwaresFactory) {
            super(logger, managedCollectionFactory, receiverFactory, commandFactory, typesFactory, hardwaresFactory);
        }
    }
}
