package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValueBase<
            DATA extends ValueBaseData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            TYPE extends ProxyType<?, ?, ?, ?>,
            VALUE extends ProxyValueBase<DATA, CHILD_DATA, CHILD, TYPE, VALUE>>
        extends ProxyObject<DATA, CHILD_DATA, CHILD, VALUE, ValueListener<? super VALUE>>
        implements Value<TYPE, VALUE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyValueBase(Log log, ListenersFactory listenersFactory, DATA data) {
        super(log, listenersFactory, data);
    }

    @Override
    public final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(VALUE_ID, new Receiver<TypeInstances>() {
            @Override
            public void messageReceived(Message<TypeInstances> stringMessageValueMessage) {
                for(ValueListener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanging(getThis());
                getData().setTypeInstances(stringMessageValueMessage.getPayload());
                for(ValueListener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanged(getThis());
            }
        }));
        return result;
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public final TypeInstances getTypeInstances() {
        return getData().getTypeInstances();
    }
}
