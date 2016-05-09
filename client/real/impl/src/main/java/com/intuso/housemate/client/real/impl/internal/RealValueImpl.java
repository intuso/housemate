package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of this value's value
 */
public final class RealValueImpl<O>
        extends RealValueBaseImpl<O, Value.Data, Value.Listener<? super RealValueImpl<O>>, RealValueImpl<O>>
        implements RealValue<O, RealTypeImpl<O>, RealValueImpl<O>> {

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param type the type of the value's value
     */
    @Inject
    public RealValueImpl(@Assisted Logger logger,
                         @Assisted Value.Data data,
                         ListenersFactory listenersFactory,
                         @Assisted RealTypeImpl<?> type) {
        this(logger, data, listenersFactory, (RealTypeImpl<O>) type, Lists.<O>newArrayList());
    }

    /**
     * @param listenersFactory
     * @param logger {@inheritDoc}
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValueImpl(Logger logger,
                         Value.Data data,
                         ListenersFactory listenersFactory,
                         RealTypeImpl<O> type,
                         O... values) {
        this(logger, data, listenersFactory, type, Arrays.asList(values));
    }

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValueImpl(final Logger logger,
                         Value.Data data,
                         ListenersFactory listenersFactory,
                         RealTypeImpl<O> type,
                         List<O> values) {
        super(logger, data, listenersFactory, type, values);
    }

    public interface Factory {
        RealValueImpl<?> create(Logger logger, Value.Data data, RealTypeImpl<?> type);
    }
}
