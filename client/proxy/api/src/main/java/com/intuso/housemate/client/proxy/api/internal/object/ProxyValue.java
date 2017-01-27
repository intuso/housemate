package com.intuso.housemate.client.proxy.api.internal.object;

import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValue<
            TYPE extends ProxyType<?>,
            VALUE extends ProxyValue<TYPE, VALUE>>
        extends ProxyValueBase<Value.Data, TYPE, Value.Listener<? super VALUE>, VALUE>
        implements Value<Type.Instances, TYPE, VALUE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyValue(Logger logger, ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Value.Data.class, managedCollectionFactory);
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        return null;
    }
}
