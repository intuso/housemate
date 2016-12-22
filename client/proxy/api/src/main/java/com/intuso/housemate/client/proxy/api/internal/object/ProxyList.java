package com.intuso.housemate.client.proxy.api.internal.object;

import com.google.common.collect.Maps;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.Iterator;
import java.util.Map;

/**
 * @param <ELEMENT> the type of the child
 */
public abstract class ProxyList<ELEMENT extends ProxyObject<?, ?>, LIST extends ProxyList<ELEMENT, ?>>
        extends ProxyObject<List.Data, List.Listener<? super ELEMENT, ? super LIST>>
        implements List<ELEMENT, LIST> {

    private final Map<String, ELEMENT> elements = Maps.newHashMap();
    private final ProxyObject.Factory<ELEMENT> elementFactory;

    private JMSUtil.Receiver<Object.Data> existingObjectReceiver;

    /**
     * @param logger {@inheritDoc}
     * @param elementFactory
     */
    public ProxyList(Logger logger, ListenersFactory listenersFactory, Factory<ELEMENT> elementFactory) {
        super(logger, List.Data.class, listenersFactory);
        this.elementFactory = elementFactory;
    }

    @Override
    protected void initChildren(final String name, final Connection connection) throws JMSException {
        super.initChildren(name, connection);
        // subscribe to all child topics and create children as new topics are discovered
        existingObjectReceiver = new JMSUtil.Receiver<>(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, "*"), Object.Data.class,
                new JMSUtil.Receiver.Listener<Object.Data>() {
                    @Override
                    public void onMessage(Object.Data data, boolean wasPersisted) {
                        if(!elements.containsKey(data.getId())) {
                            ELEMENT element = elementFactory.create(ChildUtil.logger(logger, data.getId()));
                            if(element != null) {
                                elements.put(data.getId(), element);
                                try {
                                    element.initChildren(ChildUtil.name(name, data.getId()), connection);
                                } catch (JMSException e) {
                                    logger.error("Failed to init child {}", data.getId(), e);
                                }
                                for(List.Listener<? super ELEMENT, ? super LIST> listener : listeners)
                                    listener.elementAdded((LIST) ProxyList.this, element);
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
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<ELEMENT> iterator() {
        return elements.values().iterator();
    }

    @Override
    public ListenerRegistration addObjectListener(List.Listener<? super ELEMENT, ? super LIST> listener, boolean callForExistingElements) {
        for(ELEMENT element : elements.values())
            listener.elementAdded((LIST) this, element);
        return this.addObjectListener(listener);
    }
}
