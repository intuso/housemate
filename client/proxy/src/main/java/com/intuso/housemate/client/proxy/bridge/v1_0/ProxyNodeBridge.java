package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.NodeMapper;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyNodeBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Node.Data, Node.Data, Node.Listener<? super ProxyNodeBridge>>
        implements Node<ProxyCommandBridge, ProxyListBridge<ProxyTypeBridge>, ProxyListBridge<ProxyHardwareBridge>, ProxyNodeBridge> {

    private final ProxyListBridge<ProxyTypeBridge> types;
    private final ProxyListBridge<ProxyHardwareBridge> hardwares;
    private final ProxyCommandBridge addHardwareCommand;

    @Inject
    protected ProxyNodeBridge(@Assisted Logger logger,
                              NodeMapper nodeMapper,
                              ProxyObjectBridge.Factory<ProxyListBridge<ProxyTypeBridge>> typesFactory,
                              ProxyObjectBridge.Factory<ProxyListBridge<ProxyHardwareBridge>> hardwaresFactory,
                              ProxyObjectBridge.Factory<ProxyCommandBridge> commandFactory,
                              ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Node.Data.class, nodeMapper, managedCollectionFactory);
        types = typesFactory.create(ChildUtil.logger(logger, Node.TYPES_ID));
        hardwares = hardwaresFactory.create(ChildUtil.logger(logger, Node.HARDWARES_ID));
        addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, Node.ADD_HARDWARE_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        types.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.TYPES_ID),
                ChildUtil.name(internalName, Node.TYPES_ID),
                connection);
        hardwares.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.HARDWARES_ID),
                ChildUtil.name(internalName, Node.HARDWARES_ID),
                connection);
        addHardwareCommand.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.ADD_HARDWARE_ID),
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
    public ProxyListBridge<ProxyTypeBridge> getTypes() {
        return types;
    }

    @Override
    public ProxyListBridge<ProxyHardwareBridge> getHardwares() {
        return hardwares;
    }

    @Override
    public ProxyCommandBridge getAddHardwareCommand() {
        return addHardwareCommand;
    }
}
