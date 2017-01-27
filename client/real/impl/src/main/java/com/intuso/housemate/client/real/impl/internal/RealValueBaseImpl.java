package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.real.api.internal.RealValueBase;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.List;

/**
 * @param <O> the type of the value's value
 * @param <DATA> the type of the data object
 * @param <VALUE> the type of the value
 */
public abstract class RealValueBaseImpl<O,
            DATA extends ValueBase.Data,
            LISTENER extends ValueBase.Listener<? super VALUE>,
            VALUE extends RealValueBase<O, RealTypeImpl<O>, LISTENER, VALUE>>
        extends RealObject<DATA, LISTENER>
        implements RealValueBase<O, RealTypeImpl<O>, LISTENER, VALUE> {

    private final RealTypeImpl<O> type;

    private JMSUtil.Sender valueSender;

    private Iterable<O> values;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param data {@inheritDoc}
     * @param type the type of the value's value
     */
    public RealValueBaseImpl(Logger logger, DATA data, ManagedCollectionFactory managedCollectionFactory, RealTypeImpl<O> type, Iterable<O> values) {
        super(logger, data, managedCollectionFactory);
        this.type = type;
        this.values = values;
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        valueSender = new JMSUtil.Sender(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, ValueBase.VALUE_ID));
        // get the persisted value
        Type.Instances instances = JMSUtil.getPersisted(logger, connection, JMSUtil.Type.Topic, ChildUtil.name(name, ValueBase.VALUE_ID), Type.Instances.class);
        if(instances != null)
            setValues(RealTypeImpl.deserialiseAll(type, instances));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(valueSender != null) {
            valueSender.close();
            valueSender = null;
        }
    }

    @Override
    public RealTypeImpl<O> getType() {
        return type;
    }

    @Override
    public O getValue() {
        return values != null && values.iterator().hasNext() ? values.iterator().next() : null;
    }

    /**
     * Gets the object representation of this value
     * @return
     */
    public Iterable<O> getValues() {
        return values;
    }

    @Override
    public void setValue(O value) {
        setValues(Lists.newArrayList(value));
    }

    /**
     * Sets the object representation of this value
     * @param values the new value
     */
    public final void setValues(List<O> values) {
        for(LISTENER listener : listeners)
            listener.valueChanging((VALUE)this);
        this.values = values;
        if(valueSender != null) {
            try {
                valueSender.send(RealTypeImpl.serialiseAll(type, values), true);
            } catch (JMSException e) {
                logger.error("Failed to send value update", e);
            }
        }
        for(LISTENER listener : listeners)
            listener.valueChanged((VALUE)this);
    }
}
