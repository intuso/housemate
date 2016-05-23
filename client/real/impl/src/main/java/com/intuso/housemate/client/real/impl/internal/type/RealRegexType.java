package com.intuso.housemate.client.real.impl.internal.type;

import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.plugin.api.internal.type.RegexType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Type for text input restricted to text that matches a regex
 */
public class RealRegexType<O extends RegexType> extends RealTypeImpl<O> {

    private final Serialiser<O> serialiser;

    /**
     * @param logger the log
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param listenersFactory
     * @param regexPattern the regex pattern that values must match
     * @param factory
     */
    protected RealRegexType(Logger logger, String id, String name, String description, ListenersFactory listenersFactory, String regexPattern, RegexType.Factory<O> factory) {
        super(logger, new RegexData(id, name, description, regexPattern), listenersFactory);
        this.serialiser = new Serialiser<>(factory);
    }

    @Override
    public Instance serialise(O o) {
        return serialiser.serialise(o);
    }

    @Override
    public O deserialise(Instance instance) {
        return serialiser.deserialise(instance);
    }

    private class Serialiser<O extends RegexType> implements TypeSerialiser<O> {

        private final RegexType.Factory<O> factory;

        private Serialiser(RegexType.Factory<O> factory) {
            this.factory = factory;
        }

        @Override
        public O deserialise(Instance instance) {
            return instance == null || instance.getValue() == null ? null : factory.create(instance.getValue());
        }

        @Override
        public Instance serialise(O o) {
            return o == null ? null : new Instance(o.getValue());
        }
    }
}
