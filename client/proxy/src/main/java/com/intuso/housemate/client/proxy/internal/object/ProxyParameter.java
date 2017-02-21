package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<TYPE extends ProxyType<?>,
        PARAMETER extends ProxyParameter<TYPE, PARAMETER>>
        extends ProxyObject<Parameter.Data, Parameter.Listener<? super PARAMETER>>
        implements Parameter<TYPE, PARAMETER> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyParameter(Logger logger, ManagedCollectionFactory managedCollectionFactory) {
        super(logger, Parameter.Data.class, managedCollectionFactory);
    }

    @Override
    public TYPE getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        return null;
    }

    /**
    * Created with IntelliJ IDEA.
    * User: tomc
    * Date: 14/01/14
    * Time: 13:17
    * To change this template use File | Settings | File Templates.
    */
    public static final class Simple extends ProxyParameter<ProxyType.Simple, Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      ManagedCollectionFactory managedCollectionFactory) {
            super(logger, managedCollectionFactory);
        }
    }
}
