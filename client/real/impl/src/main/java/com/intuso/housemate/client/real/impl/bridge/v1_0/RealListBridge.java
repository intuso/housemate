package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.ListMapper;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.real.impl.ChildUtil;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by tomc on 28/11/16.
 */
public class RealListBridge<ELEMENT extends RealObjectBridge<?, ?, ?>>
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.List.Data, List.Data, List.Listener<? super ELEMENT, ? super RealListBridge<ELEMENT>>>
        implements List<ELEMENT, RealListBridge<ELEMENT>> {

    private final Map<String, ELEMENT> elements = Maps.newHashMap();
    private final RealObjectBridge.Factory<ELEMENT> elementFactory;

    private Receiver<Object.Data> existingObjectReceiver;

    @Inject
    protected RealListBridge(@Assisted Logger logger,
                             ListMapper listMapper,
                             RealObjectBridge.Factory<ELEMENT> elementFactory,
                             ManagedCollectionFactory managedCollectionFactory,
                             Receiver.Factory v1_0ReceiverFactory,
                             com.intuso.housemate.client.messaging.api.internal.Sender.Factory internalSenderFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.List.Data.class, listMapper, managedCollectionFactory, v1_0ReceiverFactory, internalSenderFactory);
        this.elementFactory = elementFactory;
    }

    @Override
    protected void initChildren(final String versionName, final String internalName) {
        super.initChildren(versionName, internalName);
        // subscribe to all child topics and create children as new topics are discovered
        existingObjectReceiver = v1_0ReceiverFactory.create(logger, ChildUtil.name(versionName, "*"), Object.Data.class);
        existingObjectReceiver.listen(new Receiver.Listener<Object.Data>() {
                    @Override
                    public void onMessage(Object.Data data, boolean persistent) {
                        if(!elements.containsKey(data.getId())) {
                            ELEMENT element = elementFactory.create(ChildUtil.logger(logger, data.getId()));
                            if(element != null) {
                                elements.put(data.getId(), element);
                                element.init(ChildUtil.name(versionName, data.getId()), com.intuso.housemate.client.real.impl.internal.ChildUtil.name(internalName, data.getId()));
                                for(List.Listener<? super ELEMENT, ? super RealListBridge<ELEMENT>> listener : listeners)
                                    listener.elementAdded(RealListBridge.this, element);
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
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(List.Listener<? super ELEMENT, ? super RealListBridge<ELEMENT>> listener, boolean callForExistingElements) {
        for(ELEMENT element : elements.values())
            listener.elementAdded(this, element);
        return this.addObjectListener(listener);
    }
}
