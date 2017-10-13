package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.SubType;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.NoView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <SUB_TYPE> the type of the sub type
 */
public abstract class ProxySubType<TYPE extends ProxyType<?>,
        SUB_TYPE extends ProxySubType<TYPE, SUB_TYPE>>
        extends ProxyObject<SubType.Data, SubType.Listener<? super SUB_TYPE>, NoView>
        implements SubType<TYPE, SUB_TYPE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxySubType(Logger logger,
                        String name,
                        ManagedCollectionFactory managedCollectionFactory,
                        Receiver.Factory receiverFactory) {
        super(logger, name, SubType.Data.class, managedCollectionFactory, receiverFactory);
    }

    @Override
    public NoView createView(View.Mode mode) {
        return new NoView(mode);
    }

    @Override
    public Tree getTree(NoView view) {
        return new Tree(getData());
    }

    @Override
    public TYPE getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public ProxyObject<?, ?, ?> getChild(String id) {
        return null;
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:21
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxySubType<ProxyType.Simple, Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory) {
            super(logger, name, managedCollectionFactory, receiverFactory);
        }
    }
}
