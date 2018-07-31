package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

public abstract class RealObject<DATA extends Object.Data,
        LISTENER extends Object.Listener,
        VIEW extends View>
        implements Object<DATA, LISTENER, VIEW> {

    public final static String REAL = "real";

    protected final Logger logger;
    protected final DATA data;
    protected final ManagedCollection<LISTENER> listeners;
    private final ManagedCollection<Tree.Listener> treeListeners;

    private Sender sender;

    protected RealObject(Logger logger,
                         DATA data,
                         ManagedCollectionFactory managedCollectionFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.data = data;
        this.listeners = managedCollectionFactory.createSet();
        this.treeListeners = managedCollectionFactory.createSet();
    }

    public final void init(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        logger.debug("Init {}", name);
        sender = senderFactory.create(logger, name);
        dataUpdated();
        initChildren(name, senderFactory, receiverFactory);
    }

    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {}

    public final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(sender != null) {
            sender.close();
            sender = null;
        }
    }

    protected void uninitChildren() {}

    @Override
    public final String getObjectClass() {
        return data.getObjectClass();
    }

    @Override
    public final String getId() {
        return data.getId();
    }

    @Override
    public final String getName() {
        return data.getName();
    }

    @Override
    public final String getDescription() {
        return data.getDescription();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(LISTENER listener) {
        return listeners.add(listener);
    }

    public void addTreeListener(VIEW view, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {
        if(view != null && listener != null)
            listenerRegistrations.add(treeListeners.add(listener));
    }

    public final DATA getData() {
        return data;
    }

    protected final void dataUpdated() {
        for(Tree.Listener treeListener : treeListeners)
            treeListener.updated("path" /* todo give the actual path */, new Tree(data));
        if(sender != null) {
            try {
                sender.send(data, true);
            } catch (Throwable t) {
                logger.error("Failed to send data object", t);
            }
        }
    }
}
