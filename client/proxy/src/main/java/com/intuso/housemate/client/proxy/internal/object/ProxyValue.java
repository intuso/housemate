package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
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

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:21
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxyValue<ProxyType.Simple, Simple> {

        @Inject
        public Simple(ManagedCollectionFactory managedCollectionFactory,
                      @Assisted Logger logger) {
            super(logger, managedCollectionFactory);
        }
    }
}
