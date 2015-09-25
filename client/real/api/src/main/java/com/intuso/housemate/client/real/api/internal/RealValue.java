package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of this value's value
 */
public class RealValue<O> extends RealValueBase<ValueData, NoChildrenData, RealObject<NoChildrenData, ?, ?, ?>, O,
        Value.Listener<? super RealValue<O>>, RealValue<O>>
        implements Value<TypeInstances, RealValue<O>>{

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValue(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                     RealType<?, ?, O> type, O... values) {
        this(log, listenersFactory, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValue(Log log, ListenersFactory listenersFactory, String id, String name, String description,
                     RealType<?, ?, O> type, List<O> values) {
        super(log, listenersFactory, new ValueData(id, name, description, type.getId(), RealType.serialiseAll(type, values)), type);
    }
}
