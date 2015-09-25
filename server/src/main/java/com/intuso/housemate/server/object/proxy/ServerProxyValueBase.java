package com.intuso.housemate.server.object.proxy;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.comms.api.internal.payload.ValueBaseData;
import com.intuso.housemate.comms.api.internal.payload.ValueData;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.ValueBase;
import com.intuso.housemate.server.comms.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.ObjectFactory;

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
            LISTENER extends ValueBase.Listener<? super VALUE>,
            VALUE extends ServerProxyValueBase<DATA, CHILD_DATA, CHILD, LISTENER, VALUE>>
        extends ServerProxyObject<DATA, CHILD_DATA, CHILD, VALUE, LISTENER>
        implements ValueBase<TypeInstances, LISTENER, VALUE> {

    /**
     * @param log {@inheritDoc}
     * @param objectFactory {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ServerProxyValueBase(Log log, ListenersFactory listenersFactory, ObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> objectFactory, DATA data) {
        super(log, listenersFactory, objectFactory, data);
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public TypeInstances getValue() {
        return getData().getTypeInstances();
    }

    @Override
    public final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(ValueData.VALUE_ID, new Message.Receiver<ClientPayload<TypeData.TypeInstancesPayload>>() {
            @Override
            public void messageReceived(Message<ClientPayload<TypeData.TypeInstancesPayload>> stringMessageValueMessage) {
                for(ValueBase.Listener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanging(getThis());
                getData().setTypeInstances(stringMessageValueMessage.getPayload().getOriginal().getTypeInstances());
                for(ValueBase.Listener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanged(getThis());
            }
        }));
        return result;
    }

    @Override
    protected final void copyValues(HousemateData<?> data) {
        if(data instanceof ValueBaseData) {
            try {
                distributeMessage(new Message<>(getPath(), ValueData.VALUE_ID, new ClientPayload(null, new TypeData.TypeInstancesPayload(((ValueBaseData)data).getTypeInstances()))));
            } catch (Throwable t) {
                getLog().e("Failed to update server proxy value based on new value from client", t);
            }
        }
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
    }
}
