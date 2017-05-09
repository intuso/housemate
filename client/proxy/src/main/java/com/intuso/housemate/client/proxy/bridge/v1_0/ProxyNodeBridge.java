package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.NodeMapper;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

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
                              ManagedCollectionFactory managedCollectionFactory,
                              com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                              Sender.Factory v1_0SenderFactory,
                              Factory<ProxyListBridge<ProxyTypeBridge>> typesFactory,
                              Factory<ProxyListBridge<ProxyHardwareBridge>> hardwaresFactory,
                              Factory<ProxyCommandBridge> commandFactory) {
        super(logger, Node.Data.class, nodeMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        types = typesFactory.create(ChildUtil.logger(logger, Node.TYPES_ID));
        hardwares = hardwaresFactory.create(ChildUtil.logger(logger, Node.HARDWARES_ID));
        addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, Node.ADD_HARDWARE_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        types.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.TYPES_ID),
                ChildUtil.name(internalName, Node.TYPES_ID)
        );
        hardwares.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.HARDWARES_ID),
                ChildUtil.name(internalName, Node.HARDWARES_ID)
        );
        addHardwareCommand.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.ADD_HARDWARE_ID),
                ChildUtil.name(internalName, Node.ADD_HARDWARE_ID)
        );
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

    @Override
    public ProxyObjectBridge<?, ?, ?> getChild(String id) {
        if(ADD_HARDWARE_ID.equals(id))
            return addHardwareCommand;
        else if(HARDWARES_ID.equals(id))
            return hardwares;
        else if(TYPES_ID.equals(id))
            return types;
        return null;
    }
}
