package com.intuso.housemate.proxy;

import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.comms.Receiver;
import com.intuso.housemate.core.comms.message.StringMessageValue;
import com.intuso.housemate.core.object.HousemateObjectFactory;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.value.Value;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.core.object.value.ValueWrappableBase;
import com.intuso.listeners.ListenerRegistration;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyValueBase<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, SWBL, SWR>>,
            SR extends ProxyResources<?>,
            WBL extends ValueWrappableBase<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>,
            T extends ProxyType<?, ?, ?, ?, ?, ?>,
            V extends ProxyValueBase<R, SR, WBL, SWBL, SWR, T, V>>
        extends ProxyObject<R, SR, WBL, SWBL, SWR, V, ValueListener<? super V>>
        implements Value<T, V> {

    public ProxyValueBase(R resources, SR subResources, WBL value) {
        super(resources, subResources, value);
    }

    @Override
    public final List<ListenerRegistration<?>> registerListeners() {
        List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(addMessageListener(VALUE, new Receiver<StringMessageValue>() {
            @Override
            public void messageReceived(Message<StringMessageValue> stringMessageValueMessage) {
                getWrappable().setValue(stringMessageValueMessage.getPayload().getValue());
                for(ValueListener<? super V> listener : getObjectListeners())
                    listener.valueChanged(getThis());
            }
        }));
        return result;
    }

    @Override
    public T getType() {
        return (T) getProxyRoot().getTypes().get(getWrappable().getType());
    }

    @Override
    public final String getValue() {
        return getWrappable().getValue();
    }

}
