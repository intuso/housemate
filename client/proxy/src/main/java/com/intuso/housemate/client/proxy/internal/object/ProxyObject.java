package com.intuso.housemate.client.proxy.internal.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Type;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @param <DATA> the type of the data
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyObject<
        DATA extends Object.Data,
        LISTENER extends Object.Listener> implements Object<LISTENER> {

    public final static String PROXY = "proxy";

    protected final Logger logger;
    private final Class<DATA> dataClass;
    private final ManagedCollectionFactory managedCollectionFactory;
    protected final Receiver.Factory receiverFactory;

    protected final ManagedCollection<LISTENER> listeners;
    private final List<ObjectReferenceImpl> references = Lists.newArrayList();
    private final Map<String, Map<ObjectReferenceImpl, Integer>> missingReferences = Maps.newHashMap();

    protected DATA data = null;
    private Receiver<DATA> receiver;

    /**
     * @param logger the log
     * @param receiverFactory
     */
    protected ProxyObject(Logger logger, Class<DATA> dataClass, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.dataClass = dataClass;
        this.managedCollectionFactory = managedCollectionFactory;
        this.receiverFactory = receiverFactory;
        this.listeners = managedCollectionFactory.create();
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

    protected final void init(String name) {
        logger.debug("Init {}", name);
        receiver = receiverFactory.create(logger, Type.Topic, name, dataClass);
        receiver.listen(new Receiver.Listener<DATA>() {
                    @Override
                    public void onMessage(DATA data, boolean wasPersisted) {
                        ProxyObject.this.data = data;
                        dataUpdated();
                    }
                });
        initChildren(name);
    }

    protected void initChildren(String name) {}

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

    public abstract ProxyObject<?, ?> getChild(String id);

    public <O extends ProxyObject<?, ?>> ObjectReference<O> reference(String[] path) {
        ObjectReferenceImpl<O> reference = new ObjectReferenceImpl<>(managedCollectionFactory, path);
        reference(reference, 0);
        return reference;
    }

    protected void reference(ObjectReferenceImpl reference, int pathIndex) {
        if(pathIndex == reference.getPath().length) {
            references.add(reference);
            reference.setObject(this);
        } else {
            String id = reference.getPath()[pathIndex];
            ProxyObject<?, ?> child = getChild(id);
            if(child != null)
                child.reference(reference, pathIndex + 1);
            else {
                if(!missingReferences.containsKey(id))
                    missingReferences.put(id, Maps.<ObjectReferenceImpl, Integer>newHashMap());
                missingReferences.get(id).put(reference, pathIndex);
            }
        }
    }

    protected Map<ObjectReferenceImpl, Integer> getMissingReferences(String id) {
        return missingReferences.containsKey(id) ? missingReferences.remove(id) : Maps.<ObjectReferenceImpl, Integer>newHashMap();
    }

    public interface Factory<OBJECT extends ProxyObject<?, ?>> {
        OBJECT create(Logger logger);
    }
}
