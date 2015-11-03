package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/10/13
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public class ComputedValue implements Value<TypeInstances, ComputedValue> {

    private final RealType<?> type;
    private TypeInstances typeInstances;
    private Listeners<Value.Listener<? super ComputedValue>> listeners;

    public ComputedValue(ListenersFactory listenersFactory, RealType<?> type) {
        this.type = type;
        this.listeners = listenersFactory.create();
    }

    @Override
    public String getTypeId() {
        return type.getId();
    }

    @Override
    public TypeInstances getValue() {
        return typeInstances;
    }

    public void setTypeInstances(TypeInstances typeInstances) {
        this.typeInstances = typeInstances;
        for(Value.Listener<? super ComputedValue> listener : listeners)
            listener.valueChanged(this);
    }

    @Override
    public String getId() {
        return "computed";
    }

    @Override
    public String getName() {
        return "computed";
    }

    @Override
    public String getDescription() {
        return "computed";
    }

    @Override
    public String[] getPath() {
        return new String[] {"computed"};
    }

    @Override
    public ListenerRegistration addObjectListener(Value.Listener<? super ComputedValue> listener) {
        return listeners.addListener(listener);
    }
}
