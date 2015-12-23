package com.intuso.housemate.client.real.impl.internal;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.api.internal.driver.HardwareDriver;
import com.intuso.housemate.client.real.impl.internal.factory.device.AddDeviceCommand;
import com.intuso.housemate.client.real.impl.internal.factory.device.DeviceFactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.AddHardwareCommand;
import com.intuso.housemate.client.real.impl.internal.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.comms.api.internal.*;
import com.intuso.housemate.comms.api.internal.access.ApplicationDetails;
import com.intuso.housemate.comms.api.internal.access.ApplicationRegistration;
import com.intuso.housemate.comms.api.internal.access.ConnectionStatus;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.Application;
import com.intuso.housemate.object.api.internal.ApplicationInstance;
import com.intuso.housemate.object.api.internal.BaseHousemateObject;
import com.intuso.housemate.object.api.internal.ObjectLifecycleListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.properties.api.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BasicRealRoot
        extends RealObject<RootData, HousemateData<?>, RealObject<?, ? extends HousemateData<?>, ?, ?>, RealRoot.Listener>
        implements RealRoot,
        AddDeviceCommand.Callback,
        AddHardwareCommand.Callback {

    public final static String SEND_INITIAL_DATA = "send-initial-data";
    public final static String INITIAL_DATA = "initial-data";

    public final static String DEVICES_ID = "devices";
    public final static String HARDWARES_ID = "hardwares";
    public final static String TYPES_ID = "types";
    public final static String ADD_DEVICE_ID = "add-device";
    public final static String ADD_HARDWARE_ID = "add-hardware";

    private final static Logger logger = LoggerFactory.getLogger("com.intuso.housemate.real");

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = Maps.newHashMap();

    private final RealList<RealDevice<?>> devices;
    private final RealList<RealHardware<?>> hardwares;
    private final RealList<RealType<?>> types;

    private final RealDevice.Factory deviceFactory;
    private final RealHardware.Factory hardwareFactory;

    private final RealCommandImpl addDeviceCommand;
    private final RealCommandImpl addHardwareCommand;

    private final Router.Registration routerRegistration;
    private final AccessManager accessManager;

    private final MessageSequencer messageSequencer = new MessageSequencer(new Message.Receiver<Message.Payload>() {
        @Override
        public void messageReceived(Message<Message.Payload> message) {
            distributeMessage(message);
        }
    });

    private boolean initialDataSent = false;

    @Inject
    public BasicRealRoot(ListenersFactory listenersFactory,
                         PropertyRepository properties,
                         Router<?> router,
                         AddHardwareCommand.Factory addHardwareCommandFactory,
                         AddDeviceCommand.Factory addDeviceCommandFactory,
                         DeviceFactoryType deviceFactoryType,
                         HardwareFactoryType hardwareFactoryType,
                         RealDevice.Factory deviceFactory,
                         RealHardware.Factory hardwareFactory) {
        super(listenersFactory, logger, new RootData());

        this.devices = (RealList)new RealListImpl<>(LoggerUtil.child(logger, DEVICES_ID), listenersFactory, DEVICES_ID, "Devices", "Devices");
        this.hardwares = (RealList)new RealListImpl<>(LoggerUtil.child(logger, HARDWARES_ID), listenersFactory, HARDWARES_ID, "Hardware", "Hardware");
        this.types = (RealList)new RealListImpl<>(LoggerUtil.child(logger, TYPES_ID), listenersFactory, TYPES_ID, "Types", "Types");;

        this.deviceFactory = deviceFactory;
        this.hardwareFactory = hardwareFactory;

        this.addDeviceCommand = addDeviceCommandFactory.create(LoggerUtil.child(LoggerUtil.child(logger, ADD_DEVICE_ID), ADD_DEVICE_ID), ADD_DEVICE_ID, ADD_DEVICE_ID, "Add a device", this, this);
        this.addHardwareCommand = addHardwareCommandFactory.create(LoggerUtil.child(LoggerUtil.child(logger, ADD_HARDWARE_ID), ADD_HARDWARE_ID), ADD_HARDWARE_ID, ADD_HARDWARE_ID, "Add hardware", this, this);

        this.accessManager = new AccessManager(listenersFactory, properties, ApplicationRegistration.ClientType.Real, this);

        // need to do this once the connection manager is created and once the object is init'ed so the path is not null
        this.routerRegistration = router.registerReceiver(new Router.Receiver() {
            @Override
            public void messageReceived(Message message) {
                if(message.getSequenceId() != null)
                    sendMessage(new Message<Message.Payload>(new String[] {""}, Message.RECEIVED_TYPE, new Message.ReceivedPayload(message.getSequenceId())));
                messageSequencer.messageReceived(message);
            }

            @Override
            public void serverConnectionStatusChanged(ClientConnection clientConnection, ConnectionStatus connectionStatus) {

            }

            @Override
            public void newServerInstance(ClientConnection clientConnection, String serverId) {

            }
        });

        addChild((RealListImpl)devices);
        addChild((RealListImpl)hardwares);
        addChild((RealListImpl)types);

        addChild(addDeviceCommand);
        addChild(addHardwareCommand);

        addType(deviceFactoryType);
        addType(hardwareFactoryType);

        init(null);
    }

    @Override
    public Application.Status getApplicationStatus() {
        return accessManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstance.Status getApplicationInstanceStatus() {
        return accessManager.getApplicationInstanceStatus();
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        accessManager.register(applicationDetails);
    }

    @Override
    public void unregister() {
        accessManager.unregister();
    }

    @Override
    public void sendMessage(Message<?> message) {
        // check if we're ready to send a message, or it's one we always want to send
        if(checkReadyToSendMessage(message)) {
            // if we want to send it, check if we we're allowed or not
            if (checkCanSendMessage(message))
                routerRegistration.sendMessage(message);
            else
                throw new HousemateCommsException("Client application instance is not allowed access to the server");
        }
    }

    private boolean checkReadyToSendMessage(Message<?> message) {
        return initialDataSent
                || message.getPayload() instanceof ApplicationRegistration;
    }

    protected boolean checkCanSendMessage(Message<?> message) {
        return getApplicationInstanceStatus() == ApplicationInstance.Status.Allowed
                || (message.getPath().length == 1
                        && (message.getType().equals(Message.RECEIVED_TYPE)
                                || message.getType().equals(ApplicationRegistration.APPLICATION_REGISTRATION_TYPE)
                                || message.getType().equals(ApplicationRegistration.APPLICATION_UNREGISTRATION_TYPE)));
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        final List<ListenerRegistration> result = super.registerListeners();
        result.add(accessManager.addStatusChangeListener(new RequiresAccess.Listener<AccessManager>() {

            @Override
            public void applicationStatusChanged(AccessManager accessManager, Application.Status applicationStatus) {
                for(RealRoot.Listener listener : getObjectListeners())
                    listener.applicationStatusChanged(BasicRealRoot.this, applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(AccessManager accessManager, ApplicationInstance.Status applicationInstanceStatus) {
                for(RealRoot.Listener listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(BasicRealRoot.this, applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(AccessManager accessManager, String instanceId) {
                for (RealRoot.Listener listener : getObjectListeners())
                    listener.newApplicationInstance(BasicRealRoot.this, instanceId);
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_ID_TYPE, new Message.Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) {
                accessManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_STATUS_TYPE, new Message.Receiver<ApplicationData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationData.StatusPayload> message) {
                accessManager.setApplicationStatus(message.getPayload().getStatus());
            }
        }));
        result.add(addMessageListener(RootData.APPLICATION_INSTANCE_STATUS_TYPE, new Message.Receiver<ApplicationInstanceData.StatusPayload>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceData.StatusPayload> message) {
                accessManager.setApplicationInstanceStatus(message.getPayload().getStatus());
            }
        }));
        result.add(addMessageListener(SEND_INITIAL_DATA, new Message.Receiver<Message.Payload>() {
            @Override
            public void messageReceived(Message<Message.Payload> message) {
                initialDataSent = true;
                BasicRealRoot.this.sendMessage(INITIAL_DATA, getData());
            }
        }));
        return result;
    }

    @Override
    public final RealList<RealDevice<?>> getDevices() {
        return devices;
    }

    @Override
    public final void addDevice(RealDevice device) {
        devices.add((RealDeviceImpl<?>) device);
    }

    @Override
    public <DRIVER extends DeviceDriver> RealDevice<DRIVER> createAndAddDevice(DeviceData data) {
        RealDevice<?> device = deviceFactory.create(LoggerUtil.child(getLogger(), DEVICES_ID, data.getId()), data, this);
        addDevice(device);
        return (RealDevice<DRIVER>) device;
    }

    @Override
    public final void removeDevice(RealDevice realDevice) {
        devices.remove(realDevice.getId());
    }

    @Override
    public RealCommand getAddDeviceCommand() {
        return addDeviceCommand;
    }

    @Override
    public RealList<RealHardware<?>> getHardwares() {
        return hardwares;
    }

    @Override
    public final void addHardware(RealHardware hardware) {
        hardwares.add((RealHardwareImpl<?>) hardware);
    }

    @Override
    public <DRIVER extends HardwareDriver> RealHardware<DRIVER> createAndAddHardware(HardwareData data) {
        RealHardware<?> hardware = hardwareFactory.create(LoggerUtil.child(getLogger(), HARDWARES_ID, data.getId()), data, this);
        addHardware(hardware);
        return (RealHardware<DRIVER>) hardware;
    }

    @Override
    public final void removeHardware(RealHardware realHardware) {
        hardwares.remove(realHardware.getId());
    }

    @Override
    public RealCommand getAddHardwareCommand() {
        return addHardwareCommand;
    }

    @Override
    public final RealList<RealType<?>> getTypes() {
        return types;
    }

    @Override
    public final void addType(RealType<?> type) {
        types.add(type);
    }

    @Override
    public final void removeType(RealType<?> type) {
        types.remove(type.getId());
    }

    @Override
    public void childObjectAdded(String childName, RealObject<?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void childObjectRemoved(String childName, RealObject<?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        if(ancestor instanceof RemoteObject)
            objectAdded(ancestorPath, (RemoteObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was added
     * @param path the path of the object
     * @param object the object
     */
    private void objectAdded(String path, RemoteObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(RemoteObject<?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        if(ancestor instanceof RemoteObject)
            objectRemoved(ancestorPath, (RemoteObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was removed
     * @param path the path of the object
     * @param object the object
     */
    private void objectRemoved(String path, RemoteObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(RemoteObject<?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = getListenersFactory().create();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }

    @Override
    public BaseHousemateObject<?> findObject(String[] path) {
        return (BaseHousemateObject<?>) BaseObject.getChild(this, path, 1);
    }
}
