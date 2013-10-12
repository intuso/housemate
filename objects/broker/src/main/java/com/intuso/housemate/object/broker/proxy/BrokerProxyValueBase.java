package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the child
 * @param <VALUE> the type of the value
 */
public class BrokerProxyValueBase<
            DATA extends ValueBaseData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends BrokerProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>,
            VALUE extends BrokerProxyValueBase<DATA, CHILD_DATA, CHILD, VALUE>>
        extends BrokerProxyObject<DATA, CHILD_DATA, CHILD, VALUE, ValueListener<? super VALUE>>
        implements Value<BrokerProxyType, VALUE> {

    private BrokerProxyType type;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public BrokerProxyValueBase(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, CHILD_DATA, ? extends CHILD>> resources, DATA data) {
        super(resources, data);
    }

    @Override
    public BrokerProxyType getType() {
        return type;
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
    protected void getChildObjects() {
        super.getChildObjects();
        type = getResources().getRoot().getTypes().get(getData().getType());
        if(type == null)
            getLog().e("Could not unwrap value, value type \"" + getData().getType() + "\" is not known");
    }
}
