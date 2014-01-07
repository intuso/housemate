package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.Type;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.type.TypeListener;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * @param <DATA> the type of the data object
 * @param <CHILD_DATA> the type of the children's data object
 * @param <O> the type of the type instances
 */
public abstract class RealType<
            DATA extends TypeData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            O>
        extends RealObject<DATA, CHILD_DATA, RealObject<CHILD_DATA, ?, ?, ?>, TypeListener>
        implements Type, TypeSerialiser<O> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected RealType(Log log, DATA data) {
        super(log, data);
    }

    public static <O> TypeInstances serialiseAll(TypeSerialiser<O> serialiser, O ... typedValues) {
        return serialiseAll(serialiser, Arrays.asList(typedValues));
    }

    public static <O> TypeInstances serialiseAll(TypeSerialiser<O> serialiser, List<O> typedValues) {
        if(typedValues == null)
            return null;
        TypeInstances result = new TypeInstances();
        for(O typedValue : typedValues)
            result.add(serialiser.serialise(typedValue));
        return result;
    }

    public static <O> List<O> deserialiseAll(TypeSerialiser<O> serialiser, TypeInstances values) {
        if(values == null)
            return null;
        List<O> result = Lists.newArrayList();
        for(TypeInstance value : values)
            result.add(serialiser.deserialise(value));
        return result;
    }
}
