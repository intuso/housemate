package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.impl.bridge.v1_0.RealNodeBridge;
import com.intuso.housemate.client.v1_0.api.object.Node;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealNodeListImpl
        extends RealObject<List.Data, List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl>>
        implements RealList<ServerBaseNode<?, ?, ?, ?>, RealNodeListImpl> {

    private final com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0ReceiverFactory;
    private final RealNodeBridge.Factory nodeV1_0Factory;

    private final Map<String, ServerBaseNode<?, ?, ?, ?>> elements = Maps.newHashMap();

    private String name;
    private com.intuso.housemate.client.v1_0.messaging.api.Receiver<Node.Data> nodeV1_0Receiver;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param nodeV1_0Factory
     */
    @Inject
    public RealNodeListImpl(@Assisted Logger logger,
                            @Assisted("id") String id,
                            @Assisted("name") String name,
                            @Assisted("description") String description,
                            ManagedCollectionFactory managedCollectionFactory,
                            com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0ReceiverFactory,
                            Sender.Factory senderFactory,
                            RealNodeBridge.Factory nodeV1_0Factory) {
        super(logger, new List.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.v1_0ReceiverFactory = v1_0ReceiverFactory;
        this.nodeV1_0Factory = nodeV1_0Factory;
    }

    @Override
    public ManagedCollection.Registration addObjectListener(List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl> listener, boolean callForExistingElements) {
        ManagedCollection.Registration listenerRegistration = super.addObjectListener(listener);
        if(callForExistingElements)
            for(ServerBaseNode<?, ?, ?, ?> node : this)
                listener.elementAdded(this, node);
        return listenerRegistration;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        this.name = name;
        final String nodesPathV1_0 = com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(null, com.intuso.housemate.client.v1_0.real.impl.RealObject.REAL, Object.VERSION, Server.NODES_ID);
        nodeV1_0Receiver = v1_0ReceiverFactory.create(ChildUtil.logger(LoggerFactory.getLogger("bridge"), com.intuso.housemate.client.v1_0.real.impl.RealObject.REAL, Object.VERSION, Server.NODES_ID), com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(nodesPathV1_0, "*"), Node.Data.class);
        nodeV1_0Receiver.listen(new com.intuso.housemate.client.v1_0.messaging.api.Receiver.Listener<Node.Data>() {
                    @Override
                    public void onMessage(Node.Data nodeData, boolean wasPersisted) {
                        if(!elements.containsKey(nodeData.getId())) {
                            try {
                                ServerBaseNode<?, ?, ?, ?> node = nodeV1_0Factory.create(nodeData.getId(), ChildUtil.logger(logger, nodeData.getId()), com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(nodesPathV1_0, nodeData.getId()));
                                if(node != null)
                                    add(node);
                            } catch(Throwable t) {
                                logger.error("Failed to add v1.0 node bridge", t);
                            }
                        }
                    }
                }
        );
        // init any elements that were added before we init'd
        for(ServerBaseNode<?, ?, ?, ?> element : elements.values())
            element.init(ChildUtil.name(name, element.getId()));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(nodeV1_0Receiver != null) {
            nodeV1_0Receiver.close();
            nodeV1_0Receiver = null;
        }
        this.name = null;
        for(ServerBaseNode<?, ?, ?, ?> element : elements.values())
            element.uninit();
    }

    @Override
    public void add(ServerBaseNode<?, ?, ?, ?> element) {
        if(elements.containsKey(element.getId()))
            throw new HousemateException("Element with id " + element.getId() + " already exists");
        elements.put(element.getId(), element);
        element.init(ChildUtil.name(name, element.getId()));
        for(List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    public ServerBaseNode<?, ?, ?, ?> remove(String id) {
        ServerBaseNode<?, ?, ?, ?> element = elements.remove(id);
        if(element != null) {
            // todo delete the element's queues/topics
            element.uninit();
            for (List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl> listener : listeners)
                listener.elementRemoved(this, element);
        }
        return element;
    }

    @Override
    public final ServerBaseNode<?, ?, ?, ?> get(String id) {
        return elements.get(id);
    }

    @Override
    public ServerBaseNode<?, ?, ?, ?> getByName(String name) {
        for (ServerBaseNode<?, ?, ?, ?> element : this)
            if (name.equalsIgnoreCase(element.getName()))
                return element;
        return null;
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ServerBaseNode<?, ?, ?, ?>> iterator() {
        return elements.values().iterator();
    }

    public interface Factory {
        RealNodeListImpl create(Logger logger,
                                @Assisted("id") String id,
                                @Assisted("name") String name,
                                @Assisted("description") String description);
    }
}
