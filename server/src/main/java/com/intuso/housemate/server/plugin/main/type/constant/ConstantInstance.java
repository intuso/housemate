package com.intuso.housemate.server.plugin.main.type.constant;

import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 16/06/13
* Time: 22:27
* To change this template use File | Settings | File Templates.
*/
public class ConstantInstance<O> implements Value<RealType<?, ?, O>, ConstantInstance<O>> {

    private final RealType<?, ?, O> type;
    private final TypeInstances values;
    private final Listeners<ValueListener<? super ConstantInstance<O>>> listeners;

    public ConstantInstance(ListenersFactory listenersFactory, RealType<?, ?, O> type, TypeInstances values) {
        listeners = listenersFactory.create();
        this.type = type;
        this.values = values;
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
    public String getTypeId() {
        return type.getId();
    }

    @Override
    public RealType<?, ?, O> getType() {
        return type;
    }

    @Override
    public TypeInstances getTypeInstances() {
        return values;
    }
}
