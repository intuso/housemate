package com.intuso.housemate.broker.object.real;

import com.intuso.housemate.core.object.HousemateObject;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.value.Value;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.core.object.value.ValueWrappableBase;
import com.intuso.housemate.real.RealType;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class BrokerRealValueBase<WBL extends ValueWrappableBase<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends HousemateObject<?, ? extends SWBL, ?, ?, ?>,
            O,
            V extends BrokerRealValueBase<WBL, SWBL, SWR, O, V>>
        extends BrokerRealObject<WBL, SWBL, SWR, ValueListener<? super V>>
        implements Value<RealType<?, ?, O>, V> {

    private RealType<?, ?, O> type;

    public BrokerRealValueBase(BrokerRealResources resources, WBL wrappable, RealType<?, ?, O> type) {
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
    public String getValue() {
        return getWrappable().getValue();
    }

    public final void setValue(String value) {
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

    protected abstract V getThis();
}
