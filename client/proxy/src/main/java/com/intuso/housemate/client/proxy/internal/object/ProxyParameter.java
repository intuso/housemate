package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.ParameterView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <PARAMETER> the type of the parameter
 */
public abstract class ProxyParameter<TYPE extends ProxyType<?>,
        PARAMETER extends ProxyParameter<TYPE, PARAMETER>>
        extends ProxyObject<Parameter.Data, Parameter.Listener<? super PARAMETER>, ParameterView>
        implements Parameter<TYPE, PARAMETER> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyParameter(Logger logger,
                          String path,
                          String name,
                          ManagedCollectionFactory managedCollectionFactory,
                          Receiver.Factory receiverFactory) {
        super(logger, path, name, Parameter.Data.class, managedCollectionFactory, receiverFactory);
    }

    @Override
    public ParameterView createView(View.Mode mode) {
        return new ParameterView(mode);
    }

    @Override
    public Tree getTree(ParameterView view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

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
     * Time: 13:17
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyParameter<ProxyType.Simple, Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted("path") String path,
                      @Assisted("name") String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory) {
            super(logger, path, name, managedCollectionFactory, receiverFactory);
        }
    }
}
