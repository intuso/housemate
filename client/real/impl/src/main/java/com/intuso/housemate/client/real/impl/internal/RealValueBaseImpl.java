package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.api.internal.object.view.ValueBaseView;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.api.internal.RealValueBase;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <O> the type of the value's value
 * @param <DATA> the type of the data object
 * @param <VALUE> the type of the value
 */
public abstract class RealValueBaseImpl<O,
        DATA extends ValueBase.Data,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VIEW extends ValueBaseView,
        VALUE extends RealValueBase<DATA, O, RealTypeImpl<O>, LISTENER, VIEW, VALUE>>
        extends RealObject<DATA, LISTENER, VIEW>
        implements RealValueBase<DATA, O, RealTypeImpl<O>, LISTENER, VIEW, VALUE> {

    private final RealTypeImpl<O> type;

    private Sender valueSender;

    private List<O> values;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     * @param data {@inheritDoc}
     * @param type the type of the value's value
     */
    public RealValueBaseImpl(Logger logger,
                             DATA data,
                             ManagedCollectionFactory managedCollectionFactory,
                             RealTypeImpl<O> type,
                             List<O> values) {
        super(logger, data, managedCollectionFactory);
        this.type = type;
        this.values = values;
    }

    @Override
    protected void initChildren(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        super.initChildren(name, senderFactory, receiverFactory);

        boolean gotPersisted = false;

        // get the persisted value, if there is one
        Type.Instances instances = receiverFactory.create(logger, ChildUtil.name(name, ValueBase.VALUE_ID), Type.Instances.class).getMessage();
        if(instances != null) {
            gotPersisted = true;
            setValues(RealTypeImpl.deserialiseAll(type, instances));
        }

        // create the sender
        valueSender = senderFactory.create(logger, ChildUtil.name(name, ValueBase.VALUE_ID));

        // if it there wasn't a persisted value, and we have a value, then publish it
        if(!gotPersisted && values != null)
            setValues(values);
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

    @Override
    public List<O> getValues() {
        return values;
    }

    /**
     * Gets the object representation of this value
     * @return
     */
    public Iterable<O> getTypedValues() {
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
        Type.Instances serialisedValues = RealTypeImpl.serialiseAll(type, values);
        getData().setValues(serialisedValues);
        if(valueSender != null) {
            try {
                valueSender.send(serialisedValues, true);
                logger.debug("Set to {}", serialisedValues);
            } catch (Throwable t) {
                logger.error("Failed to send value update", t);
            }
        }
        for(LISTENER listener : listeners)
            listener.valueChanged((VALUE)this);
    }
}
