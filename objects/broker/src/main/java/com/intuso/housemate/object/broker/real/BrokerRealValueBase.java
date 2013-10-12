package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealType;

import java.util.Arrays;
import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 * @param <O> the type of the value
 * @param <VALUE> the type of the value object
 */
public abstract class BrokerRealValueBase<
            DATA extends ValueBaseData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends HousemateObject<?, ? extends CHILD_DATA, ?, ?, ?>,
            O,
            VALUE extends BrokerRealValueBase<DATA, CHILD_DATA, CHILD, O, VALUE>>
        extends BrokerRealObject<DATA, CHILD_DATA, CHILD, ValueListener<? super VALUE>>
        implements Value<RealType<?, ?, O>, VALUE> {

    private RealType<?, ?, O> type;
    private List<O> typedValues;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     * @param type the type of the value
     */
    public BrokerRealValueBase(BrokerRealResources resources, DATA data, RealType<?, ?, O> type) {
        super(resources, data);
        this.type = type;
    }

    @Override
    public RealType<?, ?, O> getType() {
        return type;
    }

    @Override
    public TypeInstances getTypeInstances() {
        return getData().getTypeInstances();
    }

    /**
     * Gets the object representation of this value
     * @return
     */
    public O getTypedValue() {
        return typedValues != null && typedValues.size() != 0 ? typedValues.get(0) : null;
    }

    /**
     * Gets the object representation of this value
     * @return
     */
    public List<O> getTypedValues() {
        return typedValues;
    }

    /**
     * Sets the object representation of this value
     * @param typedValues the new value
     */
    public final void setTypedValue(O ... typedValues) {
        setTypedValues(Arrays.asList(typedValues));
    }

    /**
     * Sets the value as its real type
     * @param typedValues the value as its real type
     */
    public final void setTypedValues(List<O> typedValues) {
        if((this.typedValues == null && typedValues == null)
                || (this.typedValues != null && typedValues != null && this.typedValues.equals(typedValues)))
            return;
        for(ValueListener<? super VALUE> listener : getObjectListeners())
            listener.valueChanging((VALUE)this);
        this.typedValues = typedValues;
        this.getData().setTypeInstances(RealType.serialiseAll(getType(), typedValues));
        for(ValueListener<? super VALUE> listener : getObjectListeners())
            listener.valueChanged((VALUE)this);
    }
}
