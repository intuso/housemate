package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * @param <DATA> the type of the data object
 * @param <CHILD_DATA> the type of the child's data object
 * @param <CHILD> the type of the child
 * @param <O> the type of the value's value
 * @param <VALUE> the type of the value
 */
public abstract class RealValueBase<
            DATA extends ValueBaseData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends RealObject<? extends CHILD_DATA, ?, ?, ?>,
            O,
            VALUE extends RealValueBase<DATA, CHILD_DATA, CHILD, O, VALUE>>
        extends RealObject<DATA, CHILD_DATA, CHILD, ValueListener<? super VALUE>>
        implements Value<RealType<?, ?, O>, VALUE>, ValueListener<VALUE> {

    private RealType<?, ?, O> type;
    private List<O> typedValues;

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     * @param type the type of the value's value
     */
    public RealValueBase(Log log, DATA data, RealType<?, ?, O> type) {
        super(log, data);
        this.type = type;
    }

    @Override
    public String getTypeId() {
        return getData().getType();
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
    public final void setTypedValues(O ... typedValues) {
        if(typedValues == null)
            setTypedValues((List)null);
        else
            setTypedValues(Arrays.asList(typedValues));
    }

    /**
     * Sets the object representation of this value
     * @param typedValues the new value
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

    @Override
    public final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addObjectListener(this));
        return result;
    }

    @Override
    public void valueChanging(VALUE value) {
        // do nothing
    }

    @Override
    public final void valueChanged(RealValueBase value) {
        sendMessage(VALUE_ID, getTypeInstances());
    }
}
