package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.api.internal.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.proxy.internal.object.ProxyObject;
import com.intuso.housemate.client.proxy.internal.object.ProxyServer;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Type for an object from the object tree
 */
public abstract class RealObjectType<O extends ProxyObject<?, ?>>
        extends RealTypeImpl<ObjectReference<O>> {

    private final static Joiner JOINER = Joiner.on("/");
    private final static Splitter SPLITTER = Splitter.on("/");

    private final Serialiser<O> serialiser;

    /**
     * @param managedCollectionFactory
     * @param server the root to get the object from
     */
    @Inject
    public RealObjectType(@Assisted Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              @Assisted Set<String> allowedTypes,
                              ManagedCollectionFactory managedCollectionFactory,
                              ProxyServer<?, ?, ?, ?, ?, ?> server) {
        super(logger, new ObjectData(id, name, description), managedCollectionFactory);
        serialiser = new Serialiser<>(managedCollectionFactory, server, allowedTypes);
    }

    @Override
    public Instance serialise(ObjectReference<O> o) {
        return serialiser.serialise(o);
    }

    @Override
    public ObjectReference<O> deserialise(Instance instance) {
        return serialiser.deserialise(instance);
    }

    /**
     * Serialiser for an object reference
     * @param <O> the type of the object to serialise
     */
    public static class Serialiser<O extends ProxyObject<?, ?>> implements TypeSerialiser<ObjectReference<O>> {

        private final ManagedCollectionFactory managedCollectionFactory;
        private final ProxyServer<?, ?, ?, ?, ?, ?> server;
        private final Set<String> allowedClasses;

        /**
         * @param managedCollectionFactory
         * @param server the root to get the object from
         * @param allowedClasses
         */
        @Inject
        public Serialiser(ManagedCollectionFactory managedCollectionFactory, ProxyServer<?, ?, ?, ?, ?, ?> server, Set<String> allowedClasses) {
            this.managedCollectionFactory = managedCollectionFactory;
            this.server = server;
            this.allowedClasses = allowedClasses;
        }

        @Override
        public Instance serialise(ObjectReference<O> o) {
            if(o == null)
                return null;
            return new Instance(JOINER.join(o.getPath()));
        }

        @Override
        public ObjectReference<O> deserialise(Instance value) {
            if(value == null || value.getValue() == null)
                return null;
            List<String> pathList = Lists.newArrayList(SPLITTER.split(value.getValue()));
            String[] path = pathList.toArray(new String[pathList.size()]);
            return new RestrictedTypeObjectReference<>(managedCollectionFactory, server.<O>reference(path), allowedClasses);
        }
    }

    private static class RestrictedTypeObjectReference<O extends ProxyObject<?, ?>> implements ObjectReference<O>, ObjectReference.Listener<O> {

        private final ManagedCollection<Listener<O>> listeners;
        private final ObjectReference<O> original;
        private final Set<String> allowedClasses;
        private O object;

        private RestrictedTypeObjectReference(ManagedCollectionFactory managedCollectionFactory,
                                              ObjectReference<O> original,
                                              Set<String> allowedClasses) {
            this.listeners = managedCollectionFactory.create();
            this.original = original;
            this.allowedClasses = allowedClasses;
            original.addListener(this);
            if(original.getObject() != null)
                object = allowedClasses.contains(original.getObject().getObjectClass()) ? original.getObject() : null;
        }

        @Override
        public String[] getPath() {
            return original.getPath();
        }

        @Override
        public O getObject() {
            return object;
        }

        @Override
        public ManagedCollection.Registration addListener(Listener<O> listener) {
            return listeners.add(listener);
        }

        @Override
        public void available(O object) {
            O o = allowedClasses.contains(object.getObjectClass()) ? object : null;
            if(o != null && this.object != null) {
                if(!o.equals(this.object)) {
                    this.object = o;
                    for (Listener<O> listener : listeners)
                        listener.available(this.object);
                }
            } else if(o != null) {
                this.object = o;
                for(Listener<O> listener : listeners)
                    listener.available(this.object);
            } else if(this.object != null) {
                this.object = null;
                for(Listener<O> listener : listeners)
                    listener.unavailable();
            }
            // else both null so do nothing
        }

        @Override
        public void unavailable() {
            this.object = null;
            for(Listener<O> listener : listeners)
                listener.unavailable();
        }
    }
}
