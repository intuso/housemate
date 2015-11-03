package com.intuso.housemate.client.real.impl.internal.type;

import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Base class for types that have a simple type, such as string, integer etc
 */
public abstract class RealSimpleType<O> extends RealTypeImpl<SimpleTypeData, NoChildrenData, O> {

    private final TypeSerialiser<O> serialiser;

    /**
     * @param log the log
     * @param listenersFactory
     * @param type the type of the simple type
     * @param serialiser the serialiser for the type
     */
    protected RealSimpleType(Log log, ListenersFactory listenersFactory, SimpleTypeData.Type type, TypeSerialiser<O> serialiser) {
        super(log, listenersFactory, new SimpleTypeData(type));
        this.serialiser = serialiser;
    }

    @Override
    public TypeInstance serialise(O o) {
        return serialiser.serialise(o);
    }

    @Override
    public O deserialise(TypeInstance value) {
        return serialiser.deserialise(value);
    }
}
