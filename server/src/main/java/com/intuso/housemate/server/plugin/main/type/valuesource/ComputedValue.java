package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealType;
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
public class ComputedValue implements Value<RealType<?, ?, ?>, ComputedValue> {

    private final RealType<?, ?, ?> type;
    private TypeInstances typeInstances;
    private Listeners<ValueListener<? super ComputedValue>> listeners;

    public ComputedValue(ListenersFactory listenersFactory, RealType<?, ?, ?> type) {
        this.type = type;
        this.listeners = listenersFactory.create();
    }

    @Override
    public String getTypeId() {
        return type.getId();
    }

    @Override
    public TypeInstances getTypeInstances() {
        return typeInstances;
    }

    public void setTypeInstances(TypeInstances typeInstances) {
        this.typeInstances = typeInstances;
        for(ValueListener<? super ComputedValue> listener : listeners)
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
    public ListenerRegistration addObjectListener(ValueListener<? super ComputedValue> listener) {
        return listeners.addListener(listener);
    }
}
