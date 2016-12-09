package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.api.internal.object.RealType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the type instances
 */
public abstract class RealTypeImpl<O>
        extends RealObject<Type.Data, Type.Listener<? super RealTypeImpl<O>>>
        implements RealType<O, RealTypeImpl<O>> {

    /**
     * @param logger {@inheritDoc}
     * @param data {@inheritDoc}
     * @param listenersFactory {@inheritDoc}
     */
    protected RealTypeImpl(Logger logger,
                           Type.Data data,
                           ListenersFactory listenersFactory) {
        super(logger, false, data, listenersFactory);
    }

    public static <O> Instances serialiseAll(TypeSerialiser<O> serialiser, O ... typedValues) {
        return serialiseAll(serialiser, Arrays.asList(typedValues));
    }

    public static <O> Instances serialiseAll(TypeSerialiser<O> serialiser, List<O> typedValues) {
        if(typedValues == null)
            return null;
        Instances result = new Instances();
        for(O typedValue : typedValues)
            result.getElements().add(serialiser.serialise(typedValue));
        return result;
    }

    public static <O> List<O> deserialiseAll(TypeSerialiser<O> serialiser, Instances values) {
        if(values == null)
            return null;
        List<O> result = Lists.newArrayList();
        for(Instance value : values.getElements())
            result.add(serialiser.deserialise(value));
        return result;
    }
}
