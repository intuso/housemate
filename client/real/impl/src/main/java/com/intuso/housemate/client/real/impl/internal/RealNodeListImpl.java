package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.NodeView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.impl.bridge.v1_0.RealNodeBridge;
import com.intuso.housemate.client.v1_0.api.object.Node;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Server;
import com.intuso.housemate.client.v1_0.messaging.api.ioc.Messaging;
import com.intuso.housemate.client.v1_0.messaging.jms.JMS;
import com.intuso.housemate.client.v1_0.serialisation.javabin.JavabinSerialiser;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealNodeListImpl
        extends RealObject<List.Data, List.Listener<? super ServerBaseNode<?, ?, ?, ?>, ? super RealNodeListImpl>, ListView<?>>
        implements RealList<ServerBaseNode<?, ?, ?, ?>, RealNodeListImpl> {

    private final com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0JavabinSenderFactory;
    private final com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0JsonReceiverFactory;
    private final com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0JsonSenderFactory;
    private final com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0JavabinReceiverFactory;
    private final RealNodeBridge.Factory nodeV1_0Factory;

    private final Map<String, ServerBaseNode<?, ?, ?, ?>> elements = Maps.newHashMap();

    private String name;
    private Sender.Factory senderFactory;
    private Receiver.Factory receiverFactory;
    private com.intuso.housemate.client.v1_0.messaging.api.Receiver<Node.Data> nodesV1_0JavabinReceiver;
    private com.intuso.housemate.client.v1_0.messaging.api.Receiver<Node.Data> nodesV1_0JsonReceiver;

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
                            @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.CONTENT_TYPE) com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0JavabinSenderFactory,
                            @Messaging(transport = JMS.TYPE, contentType = JavabinSerialiser.CONTENT_TYPE) com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0JavabinReceiverFactory,
                            @Messaging(transport = JMS.TYPE, contentType = JsonSerialiser.CONTENT_TYPE) com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0JsonSenderFactory,
                            @Messaging(transport = JMS.TYPE, contentType = JsonSerialiser.CONTENT_TYPE) com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0JsonReceiverFactory,
                            RealNodeBridge.Factory nodeV1_0Factory) {
        super(logger, new List.Data(id, name, description), managedCollectionFactory);
        this.v1_0JavabinSenderFactory = v1_0JavabinSenderFactory;
        this.v1_0JavabinReceiverFactory = v1_0JavabinReceiverFactory;
        this.v1_0JsonSenderFactory = v1_0JsonSenderFactory;
        this.v1_0JsonReceiverFactory = v1_0JsonReceiverFactory;
        this.nodeV1_0Factory = nodeV1_0Factory;
    }

    @Override
    public ListView<?> createView(View.Mode mode) {
        return new ListView<>(mode);
    }

    @Override
    public Tree getTree(ListView<?> view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, java.util.List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    for(Map.Entry<String, ServerBaseNode<?, ?, ?, ?>> element : elements.entrySet())
                        result.getChildren().put(element.getKey(), element.getValue().getTree(new NodeView(View.Mode.ANCESTORS), referenceHandler, listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    for(Map.Entry<String, ServerBaseNode<?, ?, ?, ?>> element : elements.entrySet())
                        result.getChildren().put(element.getKey(), element.getValue().getTree((NodeView) view.getView(), referenceHandler, listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getElements() != null)
                        for (String elementId : view.getElements())
                            if (elements.containsKey(elementId))
                                result.getChildren().put(elementId, elements.get(elementId).getTree((NodeView) view.getView(), referenceHandler, listener, listenerRegistrations));
                    break;
            }

        }

        return result;
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
    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        super.initChildren(name, senderFactory, receiverFactory);
        this.name = name;
        final String nodesV1_0JavabinPath = com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(null, com.intuso.housemate.client.v1_0.real.impl.RealObject.REAL, Object.VERSION, JavabinSerialiser.TOPIC, Server.NODES_ID);
        nodesV1_0JavabinReceiver = v1_0JavabinReceiverFactory.create(ChildUtil.logger(LoggerFactory.getLogger("bridge"), com.intuso.housemate.client.v1_0.real.impl.RealObject.REAL, Object.VERSION, JavabinSerialiser.TOPIC, Server.NODES_ID), com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(nodesV1_0JavabinPath, "*"), Node.Data.class);
        nodesV1_0JavabinReceiver.listen(new com.intuso.housemate.client.v1_0.messaging.api.Receiver.Listener<Node.Data>() {
                    @Override
                    public void onMessage(Node.Data nodeData, boolean persistent) {
                        if(!elements.containsKey(nodeData.getId())) {
                            try {
                                ServerBaseNode<?, ?, ?, ?> node = nodeV1_0Factory.create(nodeData.getId(), ChildUtil.logger(logger, nodeData.getId()), com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(nodesV1_0JavabinPath, nodeData.getId()), v1_0JavabinSenderFactory, v1_0JavabinReceiverFactory);
                                if(node != null)
                                    add(node);
                            } catch(Throwable t) {
                                logger.error("Failed to add v1.0 node bridge", t);
                            }
                        }
                    }
                }
        );
        final String nodesV1_0JsonPath = com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(null, com.intuso.housemate.client.v1_0.real.impl.RealObject.REAL, Object.VERSION, JsonSerialiser.TOPIC, Server.NODES_ID);
        nodesV1_0JsonReceiver = v1_0JsonReceiverFactory.create(ChildUtil.logger(LoggerFactory.getLogger("bridge"), com.intuso.housemate.client.v1_0.real.impl.RealObject.REAL, Object.VERSION, JsonSerialiser.TOPIC, Server.NODES_ID), com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(nodesV1_0JsonPath, "*"), Node.Data.class);
        nodesV1_0JsonReceiver.listen(new com.intuso.housemate.client.v1_0.messaging.api.Receiver.Listener<Node.Data>() {
                   @Override
                   public void onMessage(Node.Data nodeData, boolean persistent) {
                       if(!elements.containsKey(nodeData.getId())) {
                           try {
                               ServerBaseNode<?, ?, ?, ?> node = nodeV1_0Factory.create(nodeData.getId(), ChildUtil.logger(logger, nodeData.getId()), com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(nodesV1_0JsonPath, nodeData.getId()), v1_0JsonSenderFactory, v1_0JsonReceiverFactory);
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
            element.init(ChildUtil.name(name, element.getId()), senderFactory, receiverFactory);
        this.senderFactory = senderFactory;
        this.receiverFactory = receiverFactory;
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(nodesV1_0JavabinReceiver != null) {
            nodesV1_0JavabinReceiver.close();
            nodesV1_0JavabinReceiver = null;
        }
        if(nodesV1_0JsonReceiver != null) {
            nodesV1_0JsonReceiver.close();
            nodesV1_0JsonReceiver = null;
        }
        this.name = null;
        this.senderFactory = null;
        this.receiverFactory = null;
        for(ServerBaseNode<?, ?, ?, ?> element : elements.values())
            element.uninit();
    }

    @Override
    public void add(ServerBaseNode<?, ?, ?, ?> element) {
        if(elements.containsKey(element.getId()))
            throw new HousemateException("Element with id " + element.getId() + " already exists");
        elements.put(element.getId(), element);
        if(senderFactory != null && receiverFactory != null)
            element.init(ChildUtil.name(name, element.getId()), senderFactory, receiverFactory);
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
    public com.intuso.housemate.client.api.internal.object.Object<?, ?, ?> getChild(String id) {
        return get(id);
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
