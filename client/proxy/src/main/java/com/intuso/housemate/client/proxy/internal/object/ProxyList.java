package com.intuso.housemate.client.proxy.internal.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @param <ELEMENT> the type of the child
 */
public abstract class ProxyList<ELEMENT extends ProxyObject<?, ?, ?>, LIST extends ProxyList<ELEMENT, ?>>
        extends ProxyObject<List.Data, List.Listener<? super ELEMENT, ? super LIST>, ListView<?>>
        implements List<ELEMENT, LIST> {

    private final Map<String, ELEMENT> elements = Maps.newHashMap();
    private final Factory<ELEMENT> elementFactory;

    private boolean ancestors = false;
    private final java.util.List<ListView<?>> views = Lists.newArrayList();

    private Receiver<Object.Data> allExistingObjectReceiver;
    private final Map<String, Receiver<Object.Data>> namedExistingObjectReceivers = Maps.newHashMap();

    /**
     * @param logger {@inheritDoc}
     * @param elementFactory
     */
    public ProxyList(Logger logger,
                     String name,
                     ManagedCollectionFactory managedCollectionFactory,
                     Receiver.Factory receiverFactory,
                     Factory<ELEMENT> elementFactory) {
        super(logger, name, List.Data.class, managedCollectionFactory, receiverFactory);
        this.elementFactory = elementFactory;
    }

    private void loadElement(Object.Data data, boolean wasPersisted) {
        if (!elements.containsKey(data.getId())) {
            ELEMENT element = elementFactory.create(ChildUtil.logger(logger, data.getId()), ChildUtil.name(name, data.getId()));
            if (element != null) {
                // view the element according to views we've already been given
                if(ancestors)
                    ((ProxyObject) element).load(element.createView(View.Mode.ANCESTORS));
                else {
                    // check each list view to see if it's for this element
                    for (ListView<?> view : views)
                        if (view.getMode() == View.Mode.CHILDREN || (view.getElements() != null && view.getElements().contains(element.getId())))
                            ((ProxyObject) element).load(view.getView());
                }
                elements.put(data.getId(), element);
                for (List.Listener<? super ELEMENT, ? super LIST> listener : listeners)
                    listener.elementAdded((LIST) ProxyList.this, element);
            }
        }
    }

    @Override
    public ListView<?> createView(View.Mode mode) {
        return new ListView<>(mode);
    }

    @Override
    public Tree getTree(ListView<?> view, Tree.Listener listener, java.util.List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    for(Map.Entry<String, ELEMENT> element : elements.entrySet())
                        result.getChildren().put(element.getKey(), ((ProxyObject) element.getValue()).getTree(element.getValue().createView(View.Mode.ANCESTORS), listener, listenerRegistrations));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    for(Map.Entry<String, ELEMENT> element : elements.entrySet())
                        result.getChildren().put(element.getKey(), ((ProxyObject) element.getValue()).getTree(view.getView(), listener, listenerRegistrations));
                    break;

                case SELECTION:
                    if(view.getElements() != null)
                        for (String elementId : view.getElements())
                            if (elements.containsKey(elementId))
                                result.getChildren().put(elementId, ((ProxyObject) elements.get(elementId)).getTree(view.getView(), listener, listenerRegistrations));
                    break;
            }

        }

        return result;
    }

    @Override
    public synchronized void load(ListView<?> view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // if already viewing ancestors, don't do anything as entire object tree will already be loaded
        if (ancestors)
            return;

        switch (view.getMode()) {

            // if ancestors, set that flag, subscribe to all children, and view any current children with an ancestors view
            case ANCESTORS:
                ancestors = true;
                views.clear();
                ensureSubscribedToAll();
                for (ELEMENT element : elements.values())
                    ((ProxyObject) element).load(element.createView(View.Mode.ANCESTORS));
                break;

            // else if children, make sure we subscribed to all, and view any current elements with the element view
            case CHILDREN:
                views.add(view);
                ensureSubscribedToAll();
                for (ELEMENT element : elements.values())
                    ((ProxyObject) element).load(element.createView(View.Mode.SELECTION));
                break;

            // else if selection, make sure we're subscribed to those elements and view any current relevant elements
            case SELECTION:
                views.add(view);
                if (view.getElements() != null) {
                    ensureSubscribedTo(view.getElements());
                    for (String elementId : view.getElements()) {
                        if (elements.containsKey(elementId))
                            ((ProxyObject) elements.get(elementId)).load(view.getView());
                    }
                }
                break;
        }
    }

    private void ensureSubscribedToAll() {

        for(Receiver<Object.Data> namedReceived : namedExistingObjectReceivers.values())
            namedReceived.close();
        namedExistingObjectReceivers.clear();

        if(allExistingObjectReceiver == null) {
            // subscribe to all child topics and create children as new topics are discovered
            allExistingObjectReceiver = receiverFactory.create(logger, ChildUtil.name(name, "*"), Object.Data.class);
            allExistingObjectReceiver.listen(new Receiver.Listener<Object.Data>() {
                @Override
                public void onMessage(Object.Data data1, boolean wasPersisted) {
                    ProxyList.this.loadElement(data1, wasPersisted);
                }
            });
        }
    }

    private void ensureSubscribedTo(Set<String> elements) {

        // if subscribed to all already, then don't need to do anything
        if(allExistingObjectReceiver != null)
            return;

        for(String element : elements) {
            if(!namedExistingObjectReceivers.containsKey(element)) {
                namedExistingObjectReceivers.put(element, receiverFactory.create(logger, ChildUtil.name(name, element), Object.Data.class));
                namedExistingObjectReceivers.get(element).listen(new Receiver.Listener<Object.Data>() {
                    @Override
                    public void onMessage(Object.Data data1, boolean wasPersisted) {
                        ProxyList.this.loadElement(data1, wasPersisted);
                    }
                });
            }
        }
    }

    @Override
    protected synchronized void uninitChildren() {
        synchronized (elements) {
            for (ELEMENT ELEMENT : elements.values())
                ELEMENT.uninit();
            if (allExistingObjectReceiver != null) {
                allExistingObjectReceiver.close();
                allExistingObjectReceiver = null;
            }
            for(Receiver<Object.Data> namedReceived : namedExistingObjectReceivers.values())
                namedReceived.close();
            namedExistingObjectReceivers.clear();
        }
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
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(List.Listener<? super ELEMENT, ? super LIST> listener, boolean callForExistingElements) {
        for(ELEMENT element : elements.values())
            listener.elementAdded((LIST) this, element);
        return this.addObjectListener(listener);
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        ensureSubscribedTo(Sets.newHashSet(id));
        return get(id);
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:16
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple<ELEMENT extends ProxyObject<?, ?, ?>> extends ProxyList<ELEMENT, Simple<ELEMENT>> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ELEMENT> elementFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory, elementFactory);
        }
    }
}
