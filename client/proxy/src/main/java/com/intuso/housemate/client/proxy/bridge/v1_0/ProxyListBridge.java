package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.ListMapper;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyListBridge<ELEMENT extends ProxyObjectBridge<?, ?, ?>>
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.List.Data, List.Data, List.Listener<? super ELEMENT, ? super ProxyListBridge<ELEMENT>>>
        implements List<ELEMENT, ProxyListBridge<ELEMENT>> {

    private final Map<String, ELEMENT> elements = Maps.newHashMap();
    private final ProxyObjectBridge.Factory<ELEMENT> elementFactory;

    private com.intuso.housemate.client.messaging.api.internal.Receiver<Object.Data> existingObjectReceiver;

    @Inject
    protected ProxyListBridge(@Assisted Logger logger,
                              ListMapper listMapper,
                              ManagedCollectionFactory managedCollectionFactory,
                              com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                              Sender.Factory v1_0SenderFactory,
                              Factory<ELEMENT> elementFactory) {
        super(logger, List.Data.class, listMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        this.elementFactory = elementFactory;
    }

    @Override
    protected void initChildren(final String versionName, final String internalName) {
        super.initChildren(versionName, internalName);
        // subscribe to all child topics and create children as new topics are discovered
        existingObjectReceiver = internalReceiverFactory.create(logger, ChildUtil.name(internalName, "*"), Object.Data.class);
        existingObjectReceiver.listen(new com.intuso.housemate.client.messaging.api.internal.Receiver.Listener<Object.Data>() {
                    @Override
                    public void onMessage(Object.Data data, boolean wasPersisted) {
                        if(!elements.containsKey(data.getId())) {
                            ELEMENT element = elementFactory.create(ChildUtil.logger(logger, data.getId()));
                            if(element != null) {
                                elements.put(data.getId(), element);
                                element.init(com.intuso.housemate.client.v1_0.proxy.ChildUtil.name(versionName, data.getId()), ChildUtil.name(internalName, data.getId()));
                                for(List.Listener<? super ELEMENT, ? super ProxyListBridge<ELEMENT>> listener : listeners)
                                    listener.elementAdded(ProxyListBridge.this, element);
                            }
                        }
                    }
                });
    }

    @Override
    protected void uninitChildren() {
        for(ELEMENT ELEMENT : elements.values())
            ELEMENT.uninit();
        if(existingObjectReceiver != null) {
            existingObjectReceiver.close();
            existingObjectReceiver = null;
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
    public ProxyObjectBridge<?, ?, ?> getChild(String id) {
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

    @Override
    public ManagedCollection.Registration addObjectListener(List.Listener<? super ELEMENT, ? super ProxyListBridge<ELEMENT>> listener, boolean callForExistingElements) {
        for(ELEMENT element : elements.values())
            listener.elementAdded(this, element);
        return this.addObjectListener(listener);
    }
}
