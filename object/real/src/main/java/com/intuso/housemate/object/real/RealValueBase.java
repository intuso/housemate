package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
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
    private O typedValue;

    public RealValueBase(RealResources resources, WBL wrappable, RealType<?, ?, O> type) {
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
    public TypeInstance getTypeInstance() {
        return getWrappable().getValue();
    }

    public final void setTypedValue(O typedValue) {
        if((this.typedValue == null && typedValue == null)
            || (this.typedValue != null && typedValue != null && this.typedValue.equals(typedValue)))
        return;
        for(ValueListener<? super V> listener : getObjectListeners())
            listener.valueChanging((V)this);
        this.typedValue = typedValue;
        this.getWrappable().setValue(getType().serialise(typedValue));
        for(ValueListener<? super V> listener : getObjectListeners())
            listener.valueChanged((V)this);
    }

    @Override
    public final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addObjectListener(this));
        return result;
    }

    @Override
    public void valueChanging(V value) {
        // do nothing
    }

    @Override
    public final void valueChanged(RealValueBase value) {
        sendMessage(VALUE, getTypeInstance());
    }
}
