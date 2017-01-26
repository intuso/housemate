package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.v1_0.api.type.ObjectReference;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyServer;
import com.intuso.utilities.listener.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Type for an object from the object tree
 */
public abstract class RealObjectV1_0Type<O extends ProxyObject<?, ?>>
        extends RealTypeImpl<ObjectReference<O>> {

    private final static Joiner JOINER = Joiner.on("/");
    private final static Splitter SPLITTER = Splitter.on("/");

    private final Serialiser<O> serialiser;

    /**
     * @param managedCollectionFactory
     * @param server the root to get the object from
     */
    @Inject
    public RealObjectV1_0Type(@Assisted Logger logger,
                              @Assisted("id") String id,
                              @Assisted("name") String name,
                              @Assisted("description") String description,
                              @Assisted Set<String> allowedTypes,
                              ManagedCollectionFactory managedCollectionFactory,
                              ProxyServer<?, ?, ?, ?, ?, ?> server) {
        super(logger, new ObjectData(id, name, description), managedCollectionFactory);
        serialiser = new Serialiser<>(server, allowedTypes);
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

        private final ProxyServer<?, ?, ?, ?, ?, ?> server;
        private final Set<String> allowedTypes;

        /**
         * @param server the root to get the object from
         * @param allowedTypes
         */
        @Inject
        public Serialiser(ProxyServer<?, ?, ?, ?, ?, ?> server, Set<String> allowedTypes) {
            this.server = server;
            this.allowedTypes = allowedTypes;
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
            O object = server.find(path, false);
            if(object != null && !allowedTypes.contains(object.getObjectClass()))
                object = null;
            return new ObjectReference<>(path, object);
        }
    }
}
