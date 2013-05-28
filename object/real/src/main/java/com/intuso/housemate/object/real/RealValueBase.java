package com.intuso.housemate.object.real;

import com.intuso.housemate.api.comms.message.StringMessageValue;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueWrappableBase;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealValueBase<WBL extends ValueWrappableBase<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends RealObject<? extends SWBL, ?, ?, ?>,
            O,
            V extends RealValueBase<WBL, SWBL, SWR, O, V>>
        extends RealObject<WBL, SWBL, SWR, ValueListener<? super V>>
        implements Value<RealType<?, ?, O>, V>, ValueListener<V> {

    private RealType<?, ?, O> type;

    public RealValueBase(RealResources resources, WBL wrappable, RealType<?, ?, O> type) {
        super(resources, wrappable);
        this.type = type;
    }

    @Override
    public RealType<?, ?, O> getType() {
        return type;
    }

    public O getTypedValue() {
        return getType().deserialise(getValue());
    }

    @Override
    public java.lang.String getValue() {
        return getWrappable().getValue();
    }

    public final void setValue(java.lang.String value) {
        if((this.getWrappable().getValue() == null && value == null)
                || (this.getWrappable().getValue() != null && value != null && this.getWrappable().getValue().equals(value)))
            return;
        this.getWrappable().setValue(value);
        for(ValueListener<? super V> listener : getObjectListeners())
            listener.valueChanged(getThis());
    }

    public final void setTypedValue(O value) {
        setValue(getType().serialise(value));
    }

    @Override
    public final List<ListenerRegistration<?>> registerListeners() {
        List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(addObjectListener(this));
        return result;
    }

    @Override
    public final void valueChanged(RealValueBase value) {
        sendMessage(VALUE, new StringMessageValue(getValue()));
    }

    protected abstract V getThis();

}
