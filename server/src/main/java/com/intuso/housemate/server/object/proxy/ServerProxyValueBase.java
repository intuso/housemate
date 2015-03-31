package com.intuso.housemate.server.object.proxy;

import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 * @param <VALUE> the type of the value
 */
public abstract class ServerProxyValueBase<
            DATA extends ValueBaseData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends ServerProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            VALUE extends ServerProxyValueBase<DATA, CHILD_DATA, CHILD, VALUE>>
        extends ServerProxyObject<DATA, CHILD_DATA, CHILD, VALUE, ValueListener<? super VALUE>>
        implements Value<ServerProxyType, VALUE> {

    /**
     * @param log {@inheritDoc}
     * @param injector {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ServerProxyValueBase(Log log, ListenersFactory listenersFactory, Injector injector, DATA data) {
        super(log, listenersFactory, injector, data);
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public ServerProxyType getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypeInstances getTypeInstances() {
        return getData().getTypeInstances();
    }

    @Override
    public final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(VALUE_ID, new Receiver<ClientPayload<TypeInstances>>() {
            @Override
            public void messageReceived(Message<ClientPayload<TypeInstances>> stringMessageValueMessage) {
                for(ValueListener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanging(getThis());
                getData().setTypeInstances(stringMessageValueMessage.getPayload().getOriginal());
                for(ValueListener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanged(getThis());
            }
        }));
        return result;
    }

    @Override
    protected final void copyValues(HousemateData<?> data) {
        if(data instanceof ValueBaseData) {
            try {
                distributeMessage(new Message<>(getPath(), VALUE_ID, new ClientPayload(null, ((ValueBaseData)data).getTypeInstances())));
            } catch (HousemateException e) {
                getLog().e("Failed to update server proxy value based on new value from client");
            }
        }
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
    }
}
