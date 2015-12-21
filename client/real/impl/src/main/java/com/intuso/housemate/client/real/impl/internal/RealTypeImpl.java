package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.object.api.internal.Type;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @param <DATA> the type of the data object
 * @param <CHILD_DATA> the type of the children's data object
 * @param <O> the type of the type instances
 */
public abstract class RealTypeImpl<
            DATA extends TypeData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            O>
        extends RealObject<DATA, CHILD_DATA, RealObject<CHILD_DATA, ?, ?, ?>, Type.Listener<? super RealType<O>>>
        implements RealType<O> {

    /**
     * @param logger {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     */
    protected RealTypeImpl(Logger logger, ListenersFactory listenersFactory, DATA data) {
        super(listenersFactory, logger, data);
    }

    public static <O> TypeInstances serialiseAll(TypeSerialiser<O> serialiser, O ... typedValues) {
        return serialiseAll(serialiser, Arrays.asList(typedValues));
    }

    public static <O> TypeInstances serialiseAll(TypeSerialiser<O> serialiser, List<O> typedValues) {
        if(typedValues == null)
            return null;
        TypeInstances result = new TypeInstances();
        for(O typedValue : typedValues)
            result.getElements().add(serialiser.serialise(typedValue));
        return result;
    }

    public static <O> List<O> deserialiseAll(TypeSerialiser<O> serialiser, TypeInstances values) {
        if(values == null)
            return null;
        List<O> result = Lists.newArrayList();
        for(TypeInstance value : values.getElements())
            result.add(serialiser.deserialise(value));
        return result;
    }
}
