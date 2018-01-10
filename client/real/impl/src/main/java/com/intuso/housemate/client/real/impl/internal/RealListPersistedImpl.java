package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 */
public final class RealListPersistedImpl<CHILD_DATA extends Object.Data, ELEMENT extends RealObject<CHILD_DATA, ?, ?>>
        extends RealObject<List.Data, List.Listener<? super ELEMENT, ? super RealListPersistedImpl<CHILD_DATA, ELEMENT>>, ListView<?>>
        implements RealList<ELEMENT, RealListPersistedImpl<CHILD_DATA, ELEMENT>> {

    private final Receiver.Factory receiverFactory;
    private final Class<CHILD_DATA> childDataClass;
    private final ElementFactory<CHILD_DATA, ELEMENT> existingObjectHandler;
    private final RemoveCallback<ELEMENT> removeCallback;

    private final Map<String, ELEMENT> elements = Maps.newHashMap();

    private String name;
    private Receiver<CHILD_DATA> existingObjectReceiver;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealListPersistedImpl(@Assisted Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 ManagedCollectionFactory managedCollectionFactory,
                                 Receiver.Factory receiverFactory,
                                 Sender.Factory senderFactory,
                                 Class<CHILD_DATA> childDataClass,
                                 ElementFactory<CHILD_DATA, ELEMENT> elementFactory) {
        super(logger, new List.Data(id, name, description), managedCollectionFactory, senderFactory);
        this.receiverFactory = receiverFactory;
        this.childDataClass = childDataClass;
        this.existingObjectHandler = elementFactory;
        this.removeCallback = new RemoveCallback<ELEMENT>() {
            @Override
            public void remove(ELEMENT element) {
                RealListPersistedImpl.this.remove(element.getId());
            }
        };
    }

    @Override
    public ListView<?> createView(View.Mode mode) {
        return new ListView<>(mode);
    }

    @Override
    public Tree getTree(ListView<?> view, Tree.Listener listener, java.util.List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    for(Map.Entry<String, ELEMENT> element : elements.entrySet())
                        result.getChildren().put(element.getKey(), ((RealObject) element.getValue()).getTree(element.getValue().createView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    for(Map.Entry<String, ELEMENT> element : elements.entrySet())
                        result.getChildren().put(element.getKey(), ((RealObject) element.getValue()).getTree(view.getView(), listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getElements() != null)
                        for (String elementId : view.getElements())
                            if (elements.containsKey(elementId))
                                result.getChildren().put(elementId, ((RealObject) elements.get(elementId)).getTree(view.getView(), listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    public RemoveCallback<ELEMENT> getRemoveCallback() {
        return removeCallback;
    }

    @Override
    public ManagedCollection.Registration addObjectListener(List.Listener<? super ELEMENT, ? super RealListPersistedImpl<CHILD_DATA, ELEMENT>> listener, boolean callForExistingElements) {
        ManagedCollection.Registration listenerRegistration = super.addObjectListener(listener);
        if(callForExistingElements)
            for(ELEMENT element : this)
                listener.elementAdded(this, element);
        return listenerRegistration;
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        this.name = name;

        // init any existing elements
        for(ELEMENT element : elements.values())
            element.init(ChildUtil.name(name, element.getId()));

        existingObjectReceiver = receiverFactory.create(logger, ChildUtil.name(name, "*"), childDataClass);

        // get any persisted ones immediately
        Iterator<CHILD_DATA> datas = existingObjectReceiver.getMessages();
        while(datas.hasNext()) {
            CHILD_DATA data = datas.next();
            if (!elements.containsKey(data.getId())) {
                ELEMENT element = existingObjectHandler.create(ChildUtil.logger(logger, data.getId()), data, removeCallback);
                if (element != null)
                    add(element);
            }
        }

        // receive future messages
        // NB this will receive all the ones we received above, but in a different thread and probably not before we want to init them.
        existingObjectReceiver.listen(new Receiver.Listener<CHILD_DATA>() {
            @Override
            public void onMessage(CHILD_DATA data, boolean persistent) {
                if(!elements.containsKey(data.getId())) {
                    ELEMENT element = existingObjectHandler.create(ChildUtil.logger(logger, data.getId()), data, removeCallback);
                    if(element != null)
                        add(element);
                }
            }
        });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        this.name = null;
        for(ELEMENT element : elements.values())
            element.uninit();
        if(existingObjectReceiver != null) {
            existingObjectReceiver.close();
            existingObjectReceiver = null;
        }
    }

    @Override
    public void add(ELEMENT element) {
        if(elements.containsKey(element.getId()))
            throw new HousemateException("Element with id " + element.getId() + " already exists");
        elements.put(element.getId(), element);
        element.init(ChildUtil.name(name, element.getId()));
        for(List.Listener<? super ELEMENT, ? super RealListPersistedImpl<CHILD_DATA, ELEMENT>> listener : listeners)
            listener.elementAdded(this, element);
    }

    @Override
    public ELEMENT remove(String id) {
        ELEMENT element = elements.remove(id);
        if(element != null) {
            // todo delete the element's queues/topics
            element.uninit();
            for (List.Listener<? super ELEMENT, ? super RealListPersistedImpl<CHILD_DATA, ELEMENT>> listener : listeners)
                listener.elementRemoved(this, element);
        }
        return element;
    }

    @Override
    public final ELEMENT get(String id) {
        return elements.get(id);
    }

    @Override
    public ELEMENT getByName(String name) {
        for (ELEMENT element : this)
            if (name.equalsIgnoreCase(element.getName()))
                return element;
        return null;
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        return get(id);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    public interface Factory<CHILD_DATA extends Object.Data, ELEMENT extends RealObject<CHILD_DATA, ?, ?>> {
        RealListPersistedImpl<CHILD_DATA, ELEMENT> create(Logger logger,
                                                          @Assisted("id") String id,
                                                          @Assisted("name") String name,
                                                          @Assisted("description") String description);
    }

    public interface ElementFactory<DATA extends Object.Data, OBJECT extends RealObject<DATA, ?, ?>> {
        OBJECT create(Logger logger, DATA data, RemoveCallback<OBJECT> removeCallback);
    }

    public interface RemoveCallback<ELEMENT> {
        void remove(ELEMENT element);
    }
}
