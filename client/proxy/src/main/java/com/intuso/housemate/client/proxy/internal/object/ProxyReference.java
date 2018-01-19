package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Reference;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @param <REFERENCE> the type of the reference
 */
public abstract class ProxyReference<
        OBJECT_VIEW extends View,
        OBJECT extends ProxyObject<?, ?, OBJECT_VIEW>,
        REFERENCE extends ProxyReference<OBJECT_VIEW, OBJECT, REFERENCE>>
        extends ProxyObject<Reference.Data, Reference.Listener<? super REFERENCE>, OBJECT_VIEW>
        implements Reference<OBJECT_VIEW, OBJECT, REFERENCE> {

    private ObjectReference<OBJECT> reference;

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyReference(Logger logger,
                          String path,
                          String name,
                          ManagedCollectionFactory managedCollectionFactory,
                          Receiver.Factory receiverFactory,
                          ProxyServer<?, ?, ?, ?, ?, ?, ?> server) {
        super(logger, path, name, Reference.Data.class, managedCollectionFactory, receiverFactory);
        this.reference = server.reference(data.getPath());
    }

    @Override
    public OBJECT get() {
        return reference.getObject();
    }

    @Override
    public OBJECT_VIEW createView(View.Mode mode) {
        return reference.getObject() == null ? null : reference.getObject().createView(mode);
    }

    @Override
    public Tree getTree(OBJECT_VIEW view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {

        // register the listener
        addTreeListener(view, listener, listenerRegistrations);

        referenceHandler.handle(data.getPath(), view, referenceHandler, listener, listenerRegistrations);

        // create a result even for a null view
        return new Tree(getData());
    }

    @Override
    public void load(OBJECT_VIEW view) {
        if(reference.getObject() != null)
            reference.getObject().load(view);
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
        return reference.getObject() == null ? null : reference.getObject().getChild(id);
    }

    /**
     * Created with IntelliJ IDEA.
     * Reference: tomc
     * Date: 14/01/14
     * Time: 13:21
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple<OBJECT_VIEW extends View, OBJECT extends ProxyObject<?, ?, OBJECT_VIEW>> extends ProxyReference<
            OBJECT_VIEW,
            OBJECT,
            Simple<OBJECT_VIEW, OBJECT>> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted("path") String path,
                      @Assisted("name") String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      ProxyServer.Simple server) {
            super(logger, path, name, managedCollectionFactory, receiverFactory, server);
        }
    }
}
