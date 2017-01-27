package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.intuso.housemate.client.api.bridge.v1_0.object.NodeMapper;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ServerBaseNode;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class RealNodeBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Node.Data, Node.Data, Node.Listener<? super RealNodeBridge>>
        implements Node<RealCommandBridge, RealListBridge<RealTypeBridge>, RealListBridge<RealHardwareBridge>, RealNodeBridge>,
        ServerBaseNode<RealCommandBridge, RealListBridge<RealTypeBridge>, RealListBridge<RealHardwareBridge>, RealNodeBridge> {

    private final String id;
    private final String versionName;

    private final RealListBridge<RealTypeBridge> types;
    private final RealListBridge<RealHardwareBridge> hardwares;
    private final RealCommandBridge addHardwareCommand;

    @AssistedInject
    protected RealNodeBridge(@Assisted("id") String id,
                             @Assisted Logger logger,
                             @Assisted("versionName") String versionName,
                             NodeMapper nodeMapper,
                             RealObjectBridge.Factory<RealListBridge<RealTypeBridge>> typesFactory,
                             RealObjectBridge.Factory<RealListBridge<RealHardwareBridge>> hardwaresFactory,
                             RealObjectBridge.Factory<RealCommandBridge> commandFactory,
                             ManagedCollectionFactory managedCollectionFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Node.Data.class, nodeMapper, managedCollectionFactory);
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
    public RealListBridge<RealTypeBridge> getTypes() {
        return types;
    }

    @Override
    public RealListBridge<RealHardwareBridge> getHardwares() {
        return hardwares;
    }

    @Override
    public RealCommandBridge getAddHardwareCommand() {
        return addHardwareCommand;
    }

    public interface Factory {
        RealNodeBridge create(@Assisted("id") String id, Logger logger, @Assisted("versionName") String versionName);
    }
}
