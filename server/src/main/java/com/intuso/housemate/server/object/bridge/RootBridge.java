package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.payload.*;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.housemate.server.object.proxy.ServerProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.object.BaseObject;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootBridge
        extends BridgeObject<RootData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>,
            RootBridge, Root.Listener<? super RootBridge>>
        implements ObjectRoot<Root.Listener<? super RootBridge>, RootBridge>,
        Message.Receiver<Message.Payload>,
        Server.Container<ListBridge<ServerData, ServerBridge>> {

    public final static String SERVERS_ID = "servers";

    private final ListenersFactory listenersFactory;
    private final ListBridge<ServerData, ServerBridge> servers;
    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = new HashMap<>();
    private final Persistence persistence;

    @Inject
    public RootBridge(Logger logger, ListenersFactory listenersFactory, Persistence persistence) {
        super(logger, listenersFactory, new RootData());
        this.listenersFactory = listenersFactory;
        this.persistence = persistence;
        servers = new ListBridge<>(logger, listenersFactory, new ListData<ServerData>(SERVERS_ID, "Servers", "Servers"));
        addChild(servers);
        init(null);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener("clear-loaded", new Message.Receiver<ClientPayload<NoPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<NoPayload>> message) {
                clearClientInfo(message.getPayload().getClient());
            }
        }));
        return result;
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) {
        distributeMessage(message);
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        super.ancestorObjectAdded(ancestorPath, ancestor);
        objectAdded(ancestorPath, ancestor);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?> ancestor) {
        super.ancestorObjectRemoved(ancestorPath, ancestor);
        objectRemoved(ancestorPath, ancestor);
    }

    @Override
    public ListBridge<ServerData, ServerBridge> getServers() {
        return servers;
    }

    public void addProxyRoot(ServerProxyRoot root) {
        servers.add(new ServerBridge(getLogger(), getListenersFactory(), root, persistence));
    }

    private void objectAdded(String path, BaseObject<?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(BaseObject.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, (BaseHousemateObject<?>) object);
        }
        for(BaseObject<?, ?, ?> child : object.getChildren())
            objectAdded(path + BaseObject.PATH_SEPARATOR + child.getId(), (BaseObject<?, ?, ?>) child);
    }

    private void objectRemoved(String path, BaseObject<?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(BaseObject.PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, (BaseHousemateObject<?>) object);
        }
        for(BaseObject<?, ?, ?> child : object.getChildren())
            objectRemoved(path + BaseObject.PATH_SEPARATOR + child.getId(), (BaseObject<?, ?, ?>) child);
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

    @Override
    public BaseHousemateObject<?> findObject(String[] path) {
        return (BaseHousemateObject<?>) BaseObject.getChild(this, path, 1);
    }
}
