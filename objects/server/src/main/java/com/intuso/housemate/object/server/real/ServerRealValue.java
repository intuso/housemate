package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the value
 */
public class ServerRealValue<O>
        extends ServerRealValueBase<ValueData, NoChildrenData, NoChildrenServerRealObject, O, ServerRealValue<O>>
        implements Value<RealType<?, ?, O>, ServerRealValue<O>> {

    /**
     * @param log {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the value
     * @param values the initial values
     */
    public ServerRealValue(Log log, String id, String name, String description,
                           RealType<?, ?, O> type, O... values) {
        this(log, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param log {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the value
     * @param values the initial values
     */
    public ServerRealValue(Log log, String id, String name, String description,
                           RealType<?, ?, O> type, List<O> values) {
        super(log, new ValueData(id, name, description, type.getId(),
                RealType.serialiseAll(type, values)), type);
    }
}
