package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.ListMapper;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.housemate.client.proxy.api.internal.object.JMSUtil;
import com.intuso.utilities.listener.MemberRegistration;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
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

    private JMSUtil.Receiver<Object.Data> existingObjectReceiver;

    @Inject
    protected ProxyListBridge(@Assisted Logger logger,
                              ListMapper listMapper,
                              ProxyObjectBridge.Factory<ELEMENT> elementFactory,
                              ManagedCollectionFactory managedCollectionFactory) {
        super(logger, List.Data.class, listMapper, managedCollectionFactory);
        this.elementFactory = elementFactory;
    }

    @Override
    protected void initChildren(final String versionName, final String internalName, final Connection connection) throws JMSException {
        super.initChildren(versionName, internalName, connection);
        // subscribe to all child topics and create children as new topics are discovered
        existingObjectReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(internalName, "*"), Object.Data.class,
                new JMSUtil.Receiver.Listener<Object.Data>() {
                    @Override
                    public void onMessage(Object.Data data, boolean wasPersisted) {
                        if(!elements.containsKey(data.getId())) {
                            ELEMENT element = elementFactory.create(ChildUtil.logger(logger, data.getId()));
                            if(element != null) {
                                elements.put(data.getId(), element);
                                try {
                                    element.init(com.intuso.housemate.client.v1_0.proxy.api.ChildUtil.name(versionName, data.getId()), ChildUtil.name(internalName, data.getId()), connection);
                                } catch (JMSException e) {
                                    logger.error("Failed to init child {}", data.getId(), e);
                                }
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
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    @Override
    public MemberRegistration addObjectListener(List.Listener<? super ELEMENT, ? super ProxyListBridge<ELEMENT>> listener, boolean callForExistingElements) {
        for(ELEMENT element : elements.values())
            listener.elementAdded(this, element);
        return this.addObjectListener(listener);
    }
}
