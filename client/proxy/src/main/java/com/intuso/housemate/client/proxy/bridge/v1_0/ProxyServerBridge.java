package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.bridge.v1_0.object.ServerMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.api.internal.object.view.ServerView;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.housemate.client.v1_0.messaging.api.ioc.Messaging;
import com.intuso.housemate.client.v1_0.messaging.jms.JMS;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyObject;
import com.intuso.housemate.client.v1_0.serialisation.javabin.JavabinSerialiser;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 28/11/16.
 */
public abstract class ProxyServerBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Server.Data, Server.Data, Server.Listener<? super ProxyServerBridge>, ServerView>
        implements Server<ProxyCommandBridge,
        ProxyListBridge<ProxyAutomationBridge>, ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>>,
        ProxyListBridge<ProxyDeviceGroupBridge>,
        ProxyListBridge<ProxyUserBridge>,
        ProxyListBridge<ProxyNodeBridge>,
        ProxyServerBridge> {

    private final String type;

    private final ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> devices;
    private final ProxyCommandBridge addAutomationCommand;
    private final ProxyListBridge<ProxyAutomationBridge> automations;
    private final ProxyCommandBridge addDeviceGroupCommand;
    private final ProxyListBridge<ProxyDeviceGroupBridge> deviceGroups;
    private final ProxyListBridge<ProxyNodeBridge> nodes;
    private final ProxyCommandBridge addUserCommand;
    private final ProxyListBridge<ProxyUserBridge> users;

    ProxyServerBridge(Logger logger,
                      ServerMapper serverMapper,
                      ManagedCollectionFactory managedCollectionFactory,
                      com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                      String type,
                      Sender.Factory v1_0SenderFactory,
                      Factory<ProxyCommandBridge> commandFactory,
                      Factory<ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>>> devicesFactory,
                      Factory<ProxyListBridge<ProxyAutomationBridge>> automationsFactory,
                      Factory<ProxyListBridge<ProxyDeviceGroupBridge>> deviceGroupsFactory,
                      Factory<ProxyListBridge<ProxyNodeBridge>> nodesFactory,
                      Factory<ProxyListBridge<ProxyUserBridge>> usersFactory) {
        super(logger, Server.Data.class, serverMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        this.type = type;
        devices = devicesFactory.create(ChildUtil.logger(logger, Server.DEVICES_ID));
        addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_AUTOMATION_ID));
        automations = automationsFactory.create(ChildUtil.logger(logger, Server.AUTOMATIONS_ID));
        addDeviceGroupCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_DEVICE_GROUP_ID));
        deviceGroups = deviceGroupsFactory.create(ChildUtil.logger(logger, Server.DEVICE_GROUPS_ID));
        nodes = nodesFactory.create(ChildUtil.logger(logger, Server.NODES_ID));
        addUserCommand = commandFactory.create(ChildUtil.logger(logger, Server.ADD_USER_ID));
        users = usersFactory.create(ChildUtil.logger(logger, Server.USERS_ID));
    }

    public void start() {
        init(com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(null, com.intuso.housemate.client.v1_0.proxy.object.ProxyObject.PROXY, com.intuso.housemate.client.v1_0.api.object.Object.VERSION, type),
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
        devices.init(
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
        addDeviceGroupCommand.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.ADD_DEVICE_GROUP_ID),
                ChildUtil.name(internalName, Server.ADD_DEVICE_GROUP_ID)
        );
        deviceGroups.init(
                com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Server.DEVICE_GROUPS_ID),
                ChildUtil.name(internalName, Server.DEVICE_GROUPS_ID)
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
        devices.uninit();
        addAutomationCommand.uninit();
        automations.uninit();
        addDeviceGroupCommand.uninit();
        deviceGroups.uninit();
        nodes.uninit();
        addUserCommand.uninit();
        users.uninit();
    }

    @Override
    public ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>> getDevices() {
        return devices;
    }

    @Override
    public ProxyCommandBridge getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public ProxyListBridge<ProxyAutomationBridge> getAutomations() {
        return automations;
    }

    public ProxyCommandBridge getAddDeviceGroupCommand() {
        return addDeviceGroupCommand;
    }

    @Override
    public ProxyListBridge<ProxyDeviceGroupBridge> getDeviceGroups() {
        return deviceGroups;
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

    @Override
    public Object<?, ?, ?> getChild(String id) {
        if(ADD_AUTOMATION_ID.equals(id))
            return addAutomationCommand;
        else if(ADD_DEVICE_GROUP_ID.equals(id))
            return addDeviceGroupCommand;
        else if(ADD_USER_ID.equals(id))
            return addUserCommand;
        else if(DEVICES_ID.equals(id))
            return devices;
        else if(AUTOMATIONS_ID.equals(id))
            return automations;
        else if(DEVICE_GROUPS_ID.equals(id))
            return deviceGroups;
        else if(NODES_ID.equals(id))
            return nodes;
        else if(USERS_ID.equals(id))
            return users;
        return null;
    }

    public static class Javabin extends ProxyServerBridge {

        @Inject
        protected Javabin(ServerMapper serverMapper,
                          ManagedCollectionFactory managedCollectionFactory,
                          com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                          @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.CONTENT_TYPE) Sender.Factory senderFactory,
                          Factory<ProxyCommandBridge> commandFactory,
                          Factory<ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>>> devicesFactory,
                          Factory<ProxyListBridge<ProxyAutomationBridge>> automationsFactory,
                          Factory<ProxyListBridge<ProxyDeviceGroupBridge>> deviceGroupsFactory,
                          Factory<ProxyListBridge<ProxyNodeBridge>> nodesFactory,
                          Factory<ProxyListBridge<ProxyUserBridge>> usersFactory) {
            super(ChildUtil.logger(LoggerFactory.getLogger("bridge"), ProxyObject.PROXY, com.intuso.housemate.client.v1_0.api.object.Object.VERSION, JavabinSerialiser.TOPIC),
                  serverMapper,
                  managedCollectionFactory,
                  internalReceiverFactory,
                  JavabinSerialiser.TOPIC,
                  senderFactory,
                  commandFactory,
                  devicesFactory,
                  automationsFactory,
                  deviceGroupsFactory,
                  nodesFactory,
                  usersFactory);
        }

        public static class Service extends AbstractIdleService {

            private final ProxyServerBridge.Javabin server;

            @Inject
            public Service(ProxyServerBridge.Javabin server) {
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

    public static class Json extends ProxyServerBridge {

        @Inject
        protected Json(ServerMapper serverMapper,
                       ManagedCollectionFactory managedCollectionFactory,
                       com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                       @Messaging(transport = JMS.TYPE, contentType = JsonSerialiser.CONTENT_TYPE) Sender.Factory senderFactory,
                       Factory<ProxyCommandBridge> commandFactory,
                       Factory<ProxyListBridge<ProxyReferenceBridge<DeviceView<?>, ProxyDeviceBridge<?, ?, ?, DeviceView<?>, ?>>>> devicesFactory,
                       Factory<ProxyListBridge<ProxyAutomationBridge>> automationsFactory,
                       Factory<ProxyListBridge<ProxyDeviceGroupBridge>> deviceGroupsFactory,
                       Factory<ProxyListBridge<ProxyNodeBridge>> nodesFactory,
                       Factory<ProxyListBridge<ProxyUserBridge>> usersFactory) {
            super(ChildUtil.logger(LoggerFactory.getLogger("bridge"), ProxyObject.PROXY, com.intuso.housemate.client.v1_0.api.object.Object.VERSION, JsonSerialiser.TOPIC),
                  serverMapper,
                  managedCollectionFactory,
                  internalReceiverFactory,
                  JsonSerialiser.TOPIC,
                  senderFactory,
                  commandFactory,
                  devicesFactory,
                  automationsFactory,
                  deviceGroupsFactory,
                  nodesFactory,
                  usersFactory);
        }

        public static class Service extends AbstractIdleService {

            private final ProxyServerBridge.Json server;

            @Inject
            public Service(ProxyServerBridge.Json server) {
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
}
