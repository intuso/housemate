package com.intuso.housemate.client.proxy.internal.object;

import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.object.view.View;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <DATA> the type of the data
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
        DATA extends Object.Data,
        LISTENER extends Object.Listener,
        VIEW extends View<?>>
        implements Object<DATA, LISTENER> {

    public final static String PROXY = "proxy";

    protected final Logger logger;
    protected final String name;
    protected final Receiver.Factory receiverFactory;

    protected final ManagedCollection<LISTENER> listeners;

    protected DATA data = null;
    private Receiver<DATA> receiver;

    /**
     * @param logger the log
     * @param receiverFactory
     */
    protected ProxyObject(Logger logger, String name, Class<DATA> dataClass, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory) {
        logger.debug("Creating {}", name);
        this.logger = logger;
        this.name = name;
        this.receiverFactory = receiverFactory;
        this.listeners = managedCollectionFactory.create();
        receiver = receiverFactory.create(logger, name, dataClass);
        receiver.listen((data, wasPersisted) -> {
            ProxyObject.this.data = data;
            dataUpdated();
        });
    }

    public DATA getData() {
        return data;
    }

    @Override
    public String getObjectClass() {
        return data == null ? null : data.getObjectClass();
    }

    @Override
    public String getId() {
        return data == null ? null : data.getId();
    }

    @Override
    public String getName() {
        return data == null ? null : data.getName();
    }

    @Override
    public String getDescription() {
        return data == null ? null : data.getDescription();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(LISTENER listener) {
        return listeners.add(listener);
    }

    public abstract VIEW createView();

    public void view(VIEW view) {}

    protected final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(receiver != null) {
            receiver.close();
            receiver = null;
        }
    }

    protected void uninitChildren() {}

    protected void dataUpdated() {}

    public boolean isLoaded() {
        return data != null;
    }

    public interface Factory<OBJECT extends ProxyObject<?, ?, ?>> {
        OBJECT create(Logger logger, String name);
    }
}
