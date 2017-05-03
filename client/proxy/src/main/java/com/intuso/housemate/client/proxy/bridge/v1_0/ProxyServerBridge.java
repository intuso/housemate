package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.bridge.v1_0.object.ServerMapper;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.proxy.bridge.v1_0.ioc.ProxyV1_0;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyServerBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Server.Data, Server.Data, Server.Listener<? super ProxyServerBridge>>
        implements Server<ProxyCommandBridge, List<Device<?, ?, ?, ?, ?>, ?>, ProxyListBridge<ProxyAutomationBridge>, ProxyListBridge<ProxyDeviceCombiBridge>, ProxyListBridge<ProxyUserBridge>, ProxyListBridge<ProxyNodeBridge>, ProxyServerBridge> {

    private final ProxyListBridge<ProxyValueBridge> deviceReferences;
    private final ProxyCommandBridge addAutomationCommand;
    private final ProxyListBridge<ProxyAutomationBridge> automations;
    private final ProxyCommandBridge addSystemCommand;
    private final ProxyListBridge<ProxyDeviceCombiBridge> combiDevices;
    private final ProxyListBridge<ProxyNodeBridge> nodes;
    private final ProxyCommandBridge addUserCommand;
    private final ProxyListBridge<ProxyUserBridge> users;

    @Inject
    protected ProxyServerBridge(@ProxyV1_0 Logger logger,
                                ServerMapper serverMapper,
                                ManagedCollectionFactory managedCollectionFactory,
                                com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                Sender.Factory v1_0SenderFactory,
                                Factory<ProxyCommandBridge> commandFactory,
                                Factory<ProxyListBridge<ProxyValueBridge>> valuesFactory,
                                Factory<ProxyListBridge<ProxyAutomationBridge>> automationsFactory,
                                Factory<ProxyListBridge<ProxyDeviceCombiBridge>> systemsFactory,
                                Factory<ProxyListBridge<ProxyNodeBridge>> nodesFactory,
                                Factory<ProxyListBridge<ProxyUserBridge>> usersFactory) {
        super(logger, Server.Data.class, serverMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        deviceReferences = valuesFactory.create(ChildUtil.logger(logger, Server.DEVICES_ID));
        addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_AUTOMATION_ID));
        automations = automationsFactory.create(ChildUtil.logger(logger, Server.AUTOMATIONS_ID));
        addSystemCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_SYSTEM_ID));
        combiDevices = systemsFactory.create(ChildUtil.logger(logger, Server.DEVICE_COMBIS_ID));
        nodes = nodesFactory.create(ChildUtil.logger(logger, Server.NODES_ID));
        addUserCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_USER_ID));
        users = usersFactory.create(ChildUtil.logger(logger, Server.USERS_ID));
    }

    public void start() {
        init(com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(null, com.intuso.housemate.client.v1_0.proxy.object.ProxyObject.PROXY, com.intuso.housemate.client.v1_0.api.object.Object.VERSION),
                // don't put "proxy" in the internal name - this way real and proxy link up together
                ChildUtil.name("server")
        );
    }

    public void stop() {
        uninit();
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        deviceReferences.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.DEVICES_ID),
                ChildUtil.name(internalName, Server.DEVICES_ID)
        );
        addAutomationCommand.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.ADD_AUTOMATION_ID),
                ChildUtil.name(internalName, Server.ADD_AUTOMATION_ID)
        );
        automations.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.AUTOMATIONS_ID),
                ChildUtil.name(internalName, Server.AUTOMATIONS_ID)
        );
        addSystemCommand.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.ADD_SYSTEM_ID),
                ChildUtil.name(internalName, Server.ADD_SYSTEM_ID)
        );
        combiDevices.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.DEVICE_COMBIS_ID),
                ChildUtil.name(internalName, Server.DEVICE_COMBIS_ID)
        );
        nodes.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.NODES_ID),
                ChildUtil.name(internalName, Server.NODES_ID)
        );
        addUserCommand.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.ADD_USER_ID),
                ChildUtil.name(internalName, Server.ADD_USER_ID)
        );
        users.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.USERS_ID),
                ChildUtil.name(internalName, Server.USERS_ID)
        );
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        deviceReferences.uninit();
        addAutomationCommand.uninit();
        automations.uninit();
        addSystemCommand.uninit();
        combiDevices.uninit();
        nodes.uninit();
        addUserCommand.uninit();
        users.uninit();
    }

    @Override
    public List<Device<?, ?, ?, ?, ?>, ?> getDevices() {
        throw new UnsupportedOperationException("This bridge is just for converting messages between api versions. Devices should be accessed from a real or proxy server");
    }

    public ProxyListBridge<ProxyValueBridge> getDeviceReferences() {
        return deviceReferences;
    }

    @Override
    public ProxyCommandBridge getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public ProxyListBridge<ProxyAutomationBridge> getAutomations() {
        return automations;
    }

    @Override
    public ProxyCommandBridge getAddSystemCommand() {
        return addSystemCommand;
    }

    @Override
    public ProxyListBridge<ProxyDeviceCombiBridge> getDeviceCombis() {
        return combiDevices;
    }

    @Override
    public ProxyListBridge<ProxyNodeBridge> getNodes() {
        return nodes;
    }

    @Override
    public ProxyCommandBridge getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public ProxyListBridge<ProxyUserBridge> getUsers() {
        return users;
    }

    public static class Service extends AbstractIdleService {

        private final ProxyServerBridge server;

        @Inject
        public Service(ProxyServerBridge server) {
            this.server = server;
        }

        @Override
        protected void startUp() throws Exception {
            server.start();
        }

        @Override
        protected void shutDown() throws Exception {
            server.stop();
        }
    }
}
