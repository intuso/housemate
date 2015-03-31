package com.intuso.housemate.server.object.proxy;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.api.object.application.HasApplications;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.automation.HasAutomations;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.HasDevices;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.hardware.HasHardwares;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.HasTypes;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.HasUsers;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.housemate.server.comms.RemoteClient;
import com.intuso.housemate.server.comms.RemoteClientListener;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;
import java.util.Map;

public class ServerProxyRoot
        extends ServerProxyObject<RootData, HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>,
            ServerProxyRoot, RootListener<? super ServerProxyRoot>>
        implements Root<ServerProxyRoot>,
                HasTypes<ServerProxyList<TypeData<?>, ServerProxyType>>,
                HasHardwares<ServerProxyList<HardwareData, ServerProxyHardware>>,
                HasDevices<ServerProxyList<DeviceData, ServerProxyDevice>>,
                HasAutomations<ServerProxyList<AutomationData, ServerProxyAutomation>>,
                HasApplications<ServerProxyList<ApplicationData, ServerProxyApplication>>,
                HasUsers<ServerProxyList<UserData, ServerProxyUser>>,
                RemoteClientListener {

    private final RootBridge rootBridge;

    private RemoteClient client;
    private ListenerRegistration clientListenerRegistration;

    private ServerProxyList<TypeData<?>, ServerProxyType> types;
    private ServerProxyCommand addHardware;
    private ServerProxyList<HardwareData, ServerProxyHardware> hardwares;
    private ServerProxyCommand addDevice;
    private ServerProxyList<DeviceData, ServerProxyDevice> devices;
    private ServerProxyCommand addAutomation;
    private ServerProxyList<AutomationData, ServerProxyAutomation> automations;
    private ServerProxyList<ApplicationData, ServerProxyApplication> applications;
    private ServerProxyCommand addUser;
    private ServerProxyList<UserData, ServerProxyUser> users;

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     */
    @Inject
    public ServerProxyRoot(Log log, ListenersFactory listenersFactory, RootBridge rootBridge, Injector injector) {
        super(log, listenersFactory, injector, new RootData());
        this.rootBridge = rootBridge;
        init(null);
    }

    @Override
    public ApplicationStatus getApplicationStatus() {
        return ApplicationStatus.AllowInstances;
    }

    @Override
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return ApplicationInstanceStatus.Allowed;
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(RealRoot.INITIAL_DATA, new Receiver<ClientPayload<RootData>>() {
            @Override
            public void messageReceived(Message<ClientPayload<RootData>> message) throws HousemateException {
                for(Map.Entry<String, HousemateData<?>> entry : message.getPayload().getOriginal().getChildData().entrySet())
                    getData().getChildData().put(entry.getKey(), entry.getValue());
                init(null);
                rootBridge.addProxyRoot(ServerProxyRoot.this);
            }
        }));
        return result;
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        throw new HousemateRuntimeException("Cannot connect this type of root object");
    }

    @Override
    public void unregister() {
        throw new HousemateRuntimeException("Cannot disconnect this type of root object");
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    public ServerProxyCommand getAddHardwareCommand() {
        return addHardware;
    }

    public ServerProxyCommand getAddDeviceCommand() {
        return addDevice;
    }

    public ServerProxyCommand getAddAutomationCommand() {
        return addAutomation;
    }

    public ServerProxyCommand getAddUserCommand() {
        return addUser;
    }

    @Override
    public ServerProxyList<ApplicationData, ServerProxyApplication> getApplications() {
        return applications;
    }

    @Override
    public ServerProxyList<AutomationData, ServerProxyAutomation> getAutomations() {
        return automations;
    }

    @Override
    public ServerProxyList<DeviceData, ServerProxyDevice> getDevices() {
        return devices;
    }

    @Override
    public ServerProxyList<HardwareData, ServerProxyHardware> getHardwares() {
        return hardwares;
    }

    @Override
    public ServerProxyList<TypeData<?>, ServerProxyType> getTypes() {
        return types;
    }

    @Override
    public ServerProxyList<UserData, ServerProxyUser> getUsers() {
        return users;
    }

    @Override
    protected void copyValues(HousemateData<?> data) {
        // do nothing
    }

    /**
     * Sets the client that this object is a proxy to
     * @param client the client
     * @throws HousemateException
     */
    public final void setClient(RemoteClient client) throws HousemateException {
        if(this.client != null)
            throw new HousemateException("This object already has a client");
        else if(client.getClientInstance().getClientType() != ClientType.Real)
            throw new HousemateException("Client is not of type " + ClientType.Real);
        if(clientListenerRegistration != null)
            clientListenerRegistration.removeListener();
        this.client = client;
        clientListenerRegistration = client.addListener(this);
        if(getData().getChildData().size() == 0)
            sendMessage(RealRoot.SEND_INITIAL_DATA, NoPayload.INSTANCE);
    }

    @Override
    public void statusChanged(ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus) {

    }

    @Override
    public final void unregistered(RemoteClient client) {
        clientListenerRegistration.removeListener();
        this.client = null;
    }

    /**
     * Sends a message to the client this object is a proxy to
     * @param type the type of the message
     * @param payload the message payload
     * @throws HousemateException if an error occurs sending the message, for example the client is not connected
     */
    protected final void sendMessage(String[] path, String type, Message.Payload payload) throws HousemateException {
        if(client == null)
            throw new HousemateException("Client has disconnected. This object should no longer be used");
        else
            client.sendMessage(path, type, payload);
    }

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("What ARE you trying to do!?!?");
    }
}
