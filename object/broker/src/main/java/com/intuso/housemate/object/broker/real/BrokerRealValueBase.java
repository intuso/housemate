package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueWrappableBase;
import com.intuso.housemate.object.real.RealType;

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
    private O typedValue;

    public BrokerRealValueBase(BrokerRealResources resources, WBL wrappable, RealType<?, ?, O> type) {
        super(resources, wrappable);
        this.type = type;
    }

    @Override
    public RealType<?, ?, O> getType() {
        return type;
    }

    public O getTypedValue() {
        return typedValue;
    }

    @Override
    public TypeInstance getValue() {
        return getWrappable().getValue();
    }

    public final void setTypedValue(O typedValue) {
        if((this.typedValue == null && typedValue == null)
                || (this.typedValue != null && typedValue != null && this.typedValue.equals(typedValue)))
            return;
        this.typedValue = typedValue;
        this.getWrappable().setValue(getType().serialise(typedValue));
    }
}
