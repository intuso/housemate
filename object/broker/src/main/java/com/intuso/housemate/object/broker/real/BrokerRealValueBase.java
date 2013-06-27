package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueWrappableBase;
import com.intuso.housemate.object.real.RealType;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 * @param <O> the type of the value
 * @param <VALUE> the type of the value object
 */
public abstract class BrokerRealValueBase<
            DATA extends ValueWrappableBase<CHILD_DATA>,
            CHILD_DATA extends HousemateObjectWrappable<?>,
            CHILD extends HousemateObject<?, ? extends CHILD_DATA, ?, ?, ?>,
            O,
            VALUE extends BrokerRealValueBase<DATA, CHILD_DATA, CHILD, O, VALUE>>
        extends BrokerRealObject<DATA, CHILD_DATA, CHILD, ValueListener<? super VALUE>>
        implements Value<RealType<?, ?, O>, VALUE> {

    private RealType<?, ?, O> type;
    private O typedValue;

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

    /**
     * Get the value instance as its real type
     * @return the value instance as its real type
     */
    public O getTypedValue() {
        return typedValue;
    }

    @Override
    public TypeInstance getTypeInstance() {
        return getData().getValue();
    }

    /**
     * Sets the value as its real type
     * @param typedValue the value as its real type
     */
    public final void setTypedValue(O typedValue) {
        if((this.typedValue == null && typedValue == null)
                || (this.typedValue != null && typedValue != null && this.typedValue.equals(typedValue)))
            return;
        for(ValueListener<? super VALUE> listener : getObjectListeners())
            listener.valueChanging((VALUE)this);
        this.typedValue = typedValue;
        this.getData().setValue(getType().serialise(typedValue));
        for(ValueListener<? super VALUE> listener : getObjectListeners())
            listener.valueChanged((VALUE)this);
    }
}
