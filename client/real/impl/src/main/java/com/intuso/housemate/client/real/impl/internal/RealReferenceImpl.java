package com.intuso.housemate.client.real.impl.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Reference;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.DeviceView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.proxy.internal.object.ProxyDevice;
import com.intuso.housemate.client.proxy.internal.object.ProxyObject;
import com.intuso.housemate.client.proxy.internal.object.ProxyServer;
import com.intuso.housemate.client.real.api.internal.RealReference;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * Base class for all reference
 */
public final class RealReferenceImpl<OBJECT_VIEW extends View, OBJECT extends ProxyObject<?, ?, OBJECT_VIEW>>
        extends RealObject<Reference.Data, Reference.Listener<? super RealReferenceImpl<OBJECT_VIEW, OBJECT>>, OBJECT_VIEW>
        implements RealReference<OBJECT_VIEW, OBJECT, RealReferenceImpl<OBJECT_VIEW, OBJECT>> {

    private final ObjectReference<OBJECT> reference;

    /**
     * @param logger {@inheritDoc}
     * @param managedCollectionFactory
     */
    @Inject
    public RealReferenceImpl(@Assisted final Logger logger,
                             @Assisted("id") String id,
                             @Assisted("name") String name,
                             @Assisted("description") String description,
                             @Assisted("path") String path,
                             ProxyServer.Simple server,
                             ManagedCollectionFactory managedCollectionFactory,
                             Sender.Factory senderFactory) {
        super(logger, new Reference.Data(id, name, description, path), managedCollectionFactory, senderFactory);
        this.reference = server.reference(path);
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

        // create a result even for a null view
        Tree result = new Tree(getData());

        // todo somehow add to the tree the reference object, but at its correct location

        return result;
    }

    @Override
    public RealObject<?, ?, ?> getChild(String id) {
        return null;
    }

    public interface Factory {
        RealReferenceImpl create(Logger logger,
                                 @Assisted("id") String id,
                                 @Assisted("name") String name,
                                 @Assisted("description") String description,
                                 @Assisted("path") String path);
    }

    public static class LoadPersistedDeviceReference implements RealListPersistedImpl.ElementFactory<Reference.Data, RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> {

        private final RealReferenceImpl.Factory factory;

        @Inject
        public LoadPersistedDeviceReference(RealReferenceImpl.Factory factory) {
            this.factory = factory;
        }

        @Override
        public RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>> create(Logger logger, Reference.Data data, RealListPersistedImpl.RemoveCallback<RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>> removeCallback) {
            return (RealReferenceImpl<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?, ?>>)
                    factory.create(logger,
                            data.getId(),
                            data.getName(),
                            data.getDescription(),
                            data.getPath());
        }
    }
}
