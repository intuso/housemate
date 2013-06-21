package com.intuso.housemate.broker.plugin.type.constant;

import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 16/06/13
* Time: 22:27
* To change this template use File | Settings | File Templates.
*/
public class ConstantInstance<O> implements Value<RealType<?, ?, O>, ConstantInstance<O>> {

    private final RealType<?, ?, O> type;
    private final TypeInstance value;
    private Listeners<ValueListener<? super ConstantInstance<O>>> listeners = new Listeners<ValueListener<? super ConstantInstance<O>>>();

    public ConstantInstance(RealType<?, ?, O> type, TypeInstance value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public ListenerRegistration addObjectListener(ValueListener<? super ConstantInstance<O>> listener) {
        return listeners.addListener(listener);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String[] getPath() {
        return null;
    }

    @Override
    public RealType<?, ?, O> getType() {
        return type;
    }

    @Override
    public TypeInstance getTypeInstance() {
        return value;
    }
}
