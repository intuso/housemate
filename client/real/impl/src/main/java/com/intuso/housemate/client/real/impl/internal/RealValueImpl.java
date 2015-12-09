package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of this value's value
 */
public class RealValueImpl<O> extends RealValueBaseImpl<ValueData, NoChildrenData, RealObject<NoChildrenData, ?, ?, ?>, O,
        Value.Listener<? super RealValue<O>>, RealValue<O>>
        implements RealValue<O> {

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValueImpl(Logger logger,
                         ListenersFactory listenersFactory,
                         String id,
                         String name,
                         String description,
                         RealType<O> type,
                         O... values) {
        this(logger, listenersFactory, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    @Inject
    public RealValueImpl(Logger logger,
                         ListenersFactory listenersFactory,
                         @Assisted("id") String id,
                         @Assisted("name") String name,
                         @Assisted("description") String description,
                         @Assisted RealType<O> type,
                         @Nullable @Assisted List<O> values) {
        super(logger, listenersFactory, new ValueData(id, name, description, type.getId(), RealTypeImpl.serialiseAll(type, values)), type);
    }
}
