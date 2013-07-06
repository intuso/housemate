package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.real.RealType;

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the type of the value
 */
public class BrokerRealValue<O>
        extends BrokerRealValueBase<ValueData, NoChildrenData, NoChildrenBrokerRealObject, O, BrokerRealValue<O>>
        implements Value<RealType<?, ?, O>, BrokerRealValue<O>> {

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the value
     * @param values the initial values
     */
    public BrokerRealValue(BrokerRealResources resources, String id, String name, String description,
                           RealType<?, ?, O> type, O ... values) {
        this(resources, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the value
     * @param values the initial values
     */
    public BrokerRealValue(BrokerRealResources resources, String id, String name, String description,
                           RealType<?, ?, O> type, List<O> values) {
        super(resources, new ValueData(id, name, description, type.getId(),
                RealType.serialiseAll(type, values)), type);
    }
}
