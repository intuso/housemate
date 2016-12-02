package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.intuso.housemate.client.api.bridge.v1_0.NodeMapper;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ServerBaseNode;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class NodeBridge
        extends BridgeObject<com.intuso.housemate.client.v1_0.api.object.Node.Data, Node.Data, Node.Listener<? super NodeBridge>>
        implements Node<CommandBridge, ListBridge<TypeBridge>, ListBridge<HardwareBridge>, NodeBridge>,
        ServerBaseNode<CommandBridge, ListBridge<TypeBridge>, ListBridge<HardwareBridge>, NodeBridge> {

    private final String id;
    private final String versionName;

    private final ListBridge<TypeBridge> types;
    private final ListBridge<HardwareBridge> hardwares;
    private final CommandBridge addHardwareCommand;

    @AssistedInject
    protected NodeBridge(@Assisted("id") String id,
                         @Assisted Logger logger,
                         @Assisted("versionName") String versionName,
                         NodeMapper nodeMapper,
                         BridgeObject.Factory<ListBridge<TypeBridge>> typesFactory,
                         BridgeObject.Factory<ListBridge<HardwareBridge>> hardwaresFactory,
                         BridgeObject.Factory<CommandBridge> commandFactory,
                         ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Node.Data.class, nodeMapper, listenersFactory);
        this.id = id;
        this.versionName = versionName;
        types = typesFactory.create(ChildUtil.logger(logger, Node.TYPES_ID));
        hardwares = hardwaresFactory.create(ChildUtil.logger(logger, Node.HARDWARES_ID));
        addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, Node.ADD_HARDWARE_ID));
    }

    @Override
    public void init(String name, Connection connection) throws JMSException {
        init(versionName, name, connection);
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        types.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.TYPES_ID),
                ChildUtil.name(internalName, Node.TYPES_ID),
                connection);
        hardwares.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.HARDWARES_ID),
                ChildUtil.name(internalName, Node.HARDWARES_ID),
                connection);
        addHardwareCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.ADD_HARDWARE_ID),
                ChildUtil.name(internalName, Node.ADD_HARDWARE_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        types.uninit();
        hardwares.uninit();
        addHardwareCommand.uninit();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ListBridge<TypeBridge> getTypes() {
        return types;
    }

    @Override
    public ListBridge<HardwareBridge> getHardwares() {
        return hardwares;
    }

    @Override
    public CommandBridge getAddHardwareCommand() {
        return addHardwareCommand;
    }

    public interface Factory {
        NodeBridge create(@Assisted("id") String id, Logger logger, @Assisted("versionName") String versionName);
    }
}
