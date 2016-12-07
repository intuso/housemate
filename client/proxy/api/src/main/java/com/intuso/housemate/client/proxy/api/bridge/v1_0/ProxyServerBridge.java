package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.bridge.v1_0.ServerMapper;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.proxy.api.bridge.v1_0.ioc.ProxyV1_0;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyServerBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Server.Data, Server.Data, Server.Listener<? super ProxyServerBridge>>
        implements Server<ProxyCommandBridge, ProxyListBridge<ProxyAutomationBridge>, ProxyListBridge<ProxyDeviceBridge>, ProxyListBridge<ProxyUserBridge>, ProxyListBridge<ProxyNodeBridge>, ProxyServerBridge> {

    private final Connection connection;

    private final ProxyCommandBridge addAutomationCommand;
    private final ProxyListBridge<ProxyAutomationBridge> automations;
    private final ProxyCommandBridge addDeviceCommand;
    private final ProxyListBridge<ProxyDeviceBridge> devices;
    private final ProxyListBridge<ProxyNodeBridge> nodes;
    private final ProxyCommandBridge addUserCommand;
    private final ProxyListBridge<ProxyUserBridge> users;

    @Inject
    protected ProxyServerBridge(Connection connection,
                                @ProxyV1_0 Logger logger,
                                ServerMapper serverMapper,
                                Factory<ProxyCommandBridge> commandFactory,
                                Factory<ProxyListBridge<ProxyAutomationBridge>> automationsFactory,
                                Factory<ProxyListBridge<ProxyDeviceBridge>> devicesFactory,
                                Factory<ProxyListBridge<ProxyNodeBridge>> nodesFactory,
                                Factory<ProxyListBridge<ProxyUserBridge>> usersFactory,
                                ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Server.Data.class, serverMapper, listenersFactory);
        this.connection = connection;
        addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_AUTOMATION_ID));
        automations = automationsFactory.create(ChildUtil.logger(logger, Server.AUTOMATIONS_ID));
        addDeviceCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_DEVICE_ID));
        devices = devicesFactory.create(ChildUtil.logger(logger, Server.DEVICES_ID));
        nodes = nodesFactory.create(ChildUtil.logger(logger, Server.NODES_ID));
        addUserCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_USER_ID));
        users = usersFactory.create(ChildUtil.logger(logger, Server.USERS_ID));
    }

    public void start() {
        try {
            init(com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(null, com.intuso.housemate.client.v1_0.api.object.Object.VERSION, com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject.PROXY),
                    // don't put "proxy" in the internal name - this way real and proxy link up together
                    ChildUtil.name(null, Object.VERSION),
                    connection);
        } catch(JMSException e) {
            throw new HousemateException("Failed to initalise objects");
        }
    }

    public void stop() {
        uninit();
    }

    @Override
    protected void initChildren(String versionName, String internalName, Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        addAutomationCommand.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.ADD_AUTOMATION_ID),
                ChildUtil.name(internalName, Server.ADD_AUTOMATION_ID),
                connection);
        automations.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.AUTOMATIONS_ID),
                ChildUtil.name(internalName, Server.AUTOMATIONS_ID),
                connection);
        addDeviceCommand.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.ADD_DEVICE_ID),
                ChildUtil.name(internalName, Server.ADD_DEVICE_ID),
                connection);
        devices.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.DEVICES_ID),
                ChildUtil.name(internalName, Server.DEVICES_ID),
                connection);
        nodes.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.NODES_ID),
                ChildUtil.name(internalName, Server.NODES_ID),
                connection);
        addUserCommand.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.ADD_USER_ID),
                ChildUtil.name(internalName, Server.ADD_USER_ID),
                connection);
        users.init(
                com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.USERS_ID),
                ChildUtil.name(internalName, Server.USERS_ID),
                connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        addAutomationCommand.uninit();
        automations.uninit();
        addDeviceCommand.uninit();
        devices.uninit();
        nodes.uninit();
        addUserCommand.uninit();
        users.uninit();
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
    public ProxyCommandBridge getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public ProxyListBridge<ProxyDeviceBridge> getDevices() {
        return devices;
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
