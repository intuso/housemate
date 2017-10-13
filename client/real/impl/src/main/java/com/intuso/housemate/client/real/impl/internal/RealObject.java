package com.intuso.housemate.client.real.impl.internal;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public abstract class RealObject<DATA extends Object.Data,
        LISTENER extends Object.Listener,
        VIEW extends View>
        implements Object<DATA, LISTENER, VIEW> {

    public final static String REAL = "real";

    protected final Logger logger;
    protected final DATA data;
    protected final ManagedCollection<LISTENER> listeners;
    protected final Sender.Factory senderFactory;

    private Sender sender;

    protected RealObject(Logger logger,
                         DATA data,
                         ManagedCollectionFactory managedCollectionFactory,
                         Sender.Factory senderFactory) {
        this.senderFactory = senderFactory;
        logger.debug("Creating");
        this.logger = logger;
        this.data = data;
        this.listeners = managedCollectionFactory.create();
    }

    public final void init(String name) {
        logger.debug("Init {}", name);
        sender = senderFactory.create(logger, name);
        sendData();
        initChildren(name);
    }

    protected void initChildren(String name) {}

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

    public final DATA getData() {
        return data;
    }

    protected final void sendData() {
        if(sender != null) {
            try {
                sender.send(data, true);
            } catch (Throwable t) {
                logger.error("Failed to send data object", t);
            }
        }
    }
}
