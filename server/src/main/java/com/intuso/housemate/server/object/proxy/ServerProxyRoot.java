package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.intuso.housemate.comms.api.internal.HousemateCommsException;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.comms.v1_0.api.ObjectFactory;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.server.comms.ClientInstance;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.housemate.server.comms.RemoteClient;
import com.intuso.housemate.server.comms.RemoteClientListener;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerProxyRoot
        extends ServerProxyObject<RootData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyRoot, Root.Listener<? super ServerProxyRoot>>
        implements Root<Root.Listener<? super ServerProxyRoot>, ServerProxyRoot>,
                Type.Container<ServerProxyList<TypeData<?>, ServerProxyType>>,
                Hardware.Container<ServerProxyList<HardwareData, ServerProxyHardware>>,
                Device.Container<ServerProxyList<DeviceData, ServerProxyDevice>>,
                Automation.Container<ServerProxyList<AutomationData, ServerProxyAutomation>>,
                Application.Container<ServerProxyList<ApplicationData, ServerProxyApplication>>,
                User.Container<ServerProxyList<UserData, ServerProxyUser>>,
                RemoteClientListener,
                Message.Receiver<Message.Payload> {

    public final static String SEND_INITIAL_DATA = "send-initial-data";
    public final static String INITIAL_DATA = "initial-data";

    public final static String APPLICATIONS_ID = "applications";
    public final static String USERS_ID = "users";
    public final static String HARDWARES_ID = "hardwares";
    public final static String TYPES_ID = "types";
    public final static String DEVICES_ID = "devices";
    public final static String AUTOMATIONS_ID = "automations";
    public final static String ADD_AUTOMATION_ID = "add-automation";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_HARDWARE_ID = "add-hardware";
    public final static String ADD_USER_ID = "add-user";

    private final static Logger logger = LoggerFactory.getLogger(ServerProxyRoot.class);

    private final RootBridge rootBridge;

    private RemoteClient client;
    private ListenerRegistration clientListenerRegistration;

    /**
     * @param objectFactory {@inheritDoc}
     */
    @Inject
    public ServerProxyRoot(ListenersFactory listenersFactory, RootBridge rootBridge, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory) {
        super(listenersFactory, objectFactory, logger, new RootData());
        this.rootBridge = rootBridge;
        init(null);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(INITIAL_DATA, new Message.Receiver<ClientPayload<RootData>>() {
            @Override
            public void messageReceived(Message<ClientPayload<RootData>> message) {
                for(HousemateData<?> childData : message.getPayload().getOriginal().getChildData().values())
                    getData().addChildData(childData);
                init(null);
                rootBridge.addProxyRoot(ServerProxyRoot.this);
            }
        }));
        return result;
    }

    @Override
    public final void unregistered(RemoteClient client) {
        clientListenerRegistration.removeListener();
        this.client = null;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) {
        distributeMessage(message);
    }

    public ServerProxyCommand getAddAutomationCommand() {
        return (ServerProxyCommand) getChild(ADD_AUTOMATION_ID);
    }

    public ServerProxyCommand getAddDeviceCommand() {
        return (ServerProxyCommand) getChild(ADD_DEVICE_ID);
    }

    public ServerProxyCommand getAddHardwareCommand() {
        return (ServerProxyCommand) getChild(ADD_HARDWARE_ID);
    }

    public ServerProxyCommand getAddUserCommand() {
        return (ServerProxyCommand) getChild(ADD_USER_ID);
    }

    @Override
    public ServerProxyList<ApplicationData, ServerProxyApplication> getApplications() {
        return (ServerProxyList<ApplicationData, ServerProxyApplication>) getChild(APPLICATIONS_ID);
    }

    @Override
    public ServerProxyList<AutomationData, ServerProxyAutomation> getAutomations() {
        return (ServerProxyList<AutomationData, ServerProxyAutomation>) getChild(AUTOMATIONS_ID);
    }

    @Override
    public ServerProxyList<DeviceData, ServerProxyDevice> getDevices() {
        return (ServerProxyList<DeviceData, ServerProxyDevice>) getChild(DEVICES_ID);
    }

    @Override
    public ServerProxyList<HardwareData, ServerProxyHardware> getHardwares() {
        return (ServerProxyList<HardwareData, ServerProxyHardware>) getChild(HARDWARES_ID);
    }

    @Override
    public ServerProxyList<TypeData<?>, ServerProxyType> getTypes() {
        return (ServerProxyList<TypeData<?>, ServerProxyType>) getChild(TYPES_ID);
    }

    @Override
    public ServerProxyList<UserData, ServerProxyUser> getUsers() {
        return (ServerProxyList<UserData, ServerProxyUser>) getChild(USERS_ID);
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }

    /**
     * Sets the client that this object is a proxy to
     * @param client the client
     */
    public final void setClient(RemoteClient client) {
        if(this.client != null)
            throw new HousemateCommsException("This object already has a client");
        else if(!(client.getClientInstance() instanceof ClientInstance.Application)
                || ((ClientInstance.Application) client.getClientInstance()).getClientType() != ApplicationRegistration.ClientType.Real)
            throw new HousemateCommsException("Client is not of type " + ApplicationRegistration.ClientType.Real);
        if(clientListenerRegistration != null)
            clientListenerRegistration.removeListener();
        this.client = client;
        clientListenerRegistration = client.addListener(this);
    }

    @Override
    public void statusChanged(Application.Status applicationStatus, ApplicationInstance.Status applicationInstanceStatus) {
        try {
            if (applicationInstanceStatus == ApplicationInstance.Status.Allowed && getData().getChildData().size() == 0)
                sendMessage(SEND_INITIAL_DATA, NoPayload.INSTANCE);
        } catch(Throwable t) {
            getLogger().error("Failed to send message to load server's initial data", t);
        }
    }

    /**
     * Sends a message to the client this object is a proxy to
     * @param type the type of the message
     * @param payload the message payload
     */
    protected final void sendMessage(String[] path, String type, Message.Payload payload) {
        if(client == null)
            throw new HousemateCommsException("Client has disconnected. This object should no longer be used");
        else
            client.sendMessage(path, type, payload);
    }

    public RemoteClient getClient() {
        return client;
    }
}
