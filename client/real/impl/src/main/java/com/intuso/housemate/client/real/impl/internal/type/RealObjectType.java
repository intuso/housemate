package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.comms.api.internal.RemoteObject;
import com.intuso.housemate.comms.api.internal.payload.NoChildrenData;
import com.intuso.housemate.comms.api.internal.payload.ObjectTypeData;
import com.intuso.housemate.object.api.internal.BaseHousemateObject;
import com.intuso.housemate.object.api.internal.Root;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeSerialiser;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Type for an object from the object tree
 */
public class RealObjectType<O extends BaseHousemateObject<?>>
        extends RealTypeImpl<ObjectTypeData, NoChildrenData, RealObjectType.Reference<O>> {

    public final static String ID = "object";
    public final static String NAME = "Object";

    private final static Joiner JOINER = Joiner.on("/");
    private final static Splitter SPLITTER = Splitter.on("/");

    private final Serialiser<O> serialiser;

    /**
     * @param log the log
     * @param listenersFactory
     * @param root the root to get the object from
     */
    @Inject
    public RealObjectType(Log log, ListenersFactory listenersFactory, Root<?, ?> root) {
        super(log, listenersFactory, new ObjectTypeData(ID, NAME, "Path to an object", 1, 1));
        serialiser = new Serialiser<>(root);
    }

    @Override
    public TypeInstance serialise(Reference<O> o) {
        return serialiser.serialise(o);
    }

    @Override
    public Reference<O> deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    /**
     * Reference for an object containing the object's path, and the object if it exists
     * @param <O>
     */
    public static class Reference<O extends BaseHousemateObject<?>> {

        private final String[] path;
        private O object;

        /**
         * @param path the path to the object
         */
        public Reference(String[] path) {
            this(path, null);
        }

        /**
         * @param object the object
         */
        public Reference(O object) {
            this(object == null ? null : object.getPath(), object);
        }

        /**
         * @param path the object's path
         * @param object the object
         */
        private Reference(String[] path, O object) {
            this.path = path;
            this.object = object;
        }

        /**
         * Gets the path
         * @return the path
         */
        public String[] getPath() {
            return path;
        }

        /**
         * Gets the object
         * @return the object
         */
        public O getObject() {
            return object;
        }

        /**
         * Sets the object
         * @param object the object
         */
        public void setObject(O object) {
            // todo check the object's path matches the path inside this reference
            this.object = object;
        }
    }

    /**
     * Serialiser for an object reference
     * @param <O> the type of the object to serialise
     */
    public static class Serialiser<O extends BaseHousemateObject<?>> implements TypeSerialiser<Reference<O>> {

        private final Root<?, ?> root;

        /**
         * @param root the root to get the object from
         */
        @Inject
        public Serialiser(Root<?, ?> root) {
            this.root = root;
        }

        @Override
        public TypeInstance serialise(Reference<O> o) {
            if(o == null)
                return null;
            return new TypeInstance(JOINER.join(o.getPath()));
        }

        @Override
        public Reference<O> deserialise(TypeInstance value) {
            if(value == null || value.getValue() == null)
                return null;
            List<String> pathList = Lists.newArrayList(SPLITTER.split(value.getValue()));
            String[] path = pathList.toArray(new String[pathList.size()]);
            return new Reference(path, (O) RemoteObject.getChild((RemoteObject) root, path, 1));
        }
    }
}
