package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueWrappableBase;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * @param <DATA> the type of the data object
 * @param <CHILD_DATA> the type of the child's data object
 * @param <CHILD> the type of the child
 * @param <O> the type of the value's value
 * @param <VALUE> the type of the value
 */
public abstract class RealValueBase<
            DATA extends ValueWrappableBase<CHILD_DATA>,
            CHILD_DATA extends HousemateObjectWrappable<?>,
            CHILD extends RealObject<? extends CHILD_DATA, ?, ?, ?>,
            O,
            VALUE extends RealValueBase<DATA, CHILD_DATA, CHILD, O, VALUE>>
        extends RealObject<DATA, CHILD_DATA, CHILD, ValueListener<? super VALUE>>
        implements Value<RealType<?, ?, O>, VALUE>, ValueListener<VALUE> {

    private RealType<?, ?, O> type;
    private O typedValue;

    /**
     * @param resources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     * @param type the type of the value's value
     */
    public RealValueBase(RealResources resources, DATA wrappable, RealType<?, ?, O> type) {
        super(resources, wrappable);
        this.type = type;
    }

    @Override
    public RealType<?, ?, O> getType() {
        return type;
    }

    @Override
    public TypeInstance getTypeInstance() {
        return getWrappable().getValue();
    }

    /**
     * Gets the object representation of this value
     * @return
     */
    public O getTypedValue() {
        return typedValue;
    }

    /**
     * Sets the object representation of this value
     * @param typedValue the new value
     */
    public final void setTypedValue(O typedValue) {
        if((this.typedValue == null && typedValue == null)
            || (this.typedValue != null && typedValue != null && this.typedValue.equals(typedValue)))
            return;
        for(ValueListener<? super VALUE> listener : getObjectListeners())
            listener.valueChanging((VALUE)this);
        this.typedValue = typedValue;
        this.getWrappable().setValue(getType().serialise(typedValue));
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
        sendMessage(VALUE_ID, getTypeInstance());
    }
}
