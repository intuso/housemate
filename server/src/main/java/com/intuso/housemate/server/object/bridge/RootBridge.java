package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.comms.ApplicationStatus;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.realclient.HasRealClients;
import com.intuso.housemate.api.object.realclient.RealClientData;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.housemate.server.object.proxy.ServerProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootBridge
        extends BridgeObject<RootData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>,
            RootBridge, RootListener<? super RootBridge>>
        implements Root<RootBridge>,
            HasRealClients<ListBridge<RealClientData, RealClientBridge>> {

    public final static String REAL_CLIENTS_ID = "real-clients";

    private final ListenersFactory listenersFactory;
    private final ListBridge<RealClientData, RealClientBridge> realClients;
    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<>();

    @Inject
    public RootBridge(Log log, ListenersFactory listenersFactory) {
        super(log, listenersFactory, new RootData());
        this.listenersFactory = listenersFactory;
        realClients = new ListBridge<>(log, listenersFactory, new ListData<RealClientData>(REAL_CLIENTS_ID, "Clients", "Clients"));
        addChild(realClients);
        init(null);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener("clear-loaded", new Receiver<ClientPayload<NoPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<NoPayload>> message) throws HousemateException {
                clearClientInfo(message.getPayload().getClient());
            }
        }));
        return result;
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

    @Override
    public void sendMessage(Message<?> message) {
        throw new HousemateRuntimeException("Watcha playing at, fool?");
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        super.ancestorObjectAdded(ancestorPath, ancestor);
        if(ancestor instanceof HousemateObject)
            objectAdded(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        super.ancestorObjectRemoved(ancestorPath, ancestor);
        if(ancestor instanceof HousemateObject)
            objectRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    @Override
    public ListBridge<RealClientData, RealClientBridge> getRealClients() {
        return realClients;
    }

    public void addProxyRoot(ServerProxyRoot root) {
        realClients.add(new RealClientBridge(getLog(), getListenersFactory(), root));
    }

    private void objectAdded(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(BaseObject.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + BaseObject.PATH_SEPARATOR + child.getId(), child);
    }

    private void objectRemoved(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(BaseObject.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + BaseObject.PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(BaseObject.PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = listenersFactory.create();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }
}
