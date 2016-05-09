package com.intuso.housemate.client.real.impl.internal.factory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.object.Option;
import com.intuso.housemate.client.real.api.internal.driver.PluginResource;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.type.RealChoiceType;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;

/**
 * Created by tomc on 19/03/15.
 */
public class FactoryType<FACTORY> extends RealChoiceType<FactoryType.Entry<FACTORY>> {

    private final ListenersFactory listenersFactory;
    private final BiMap<String, Entry<FACTORY>> factories = HashBiMap.create();

    @Inject
    protected FactoryType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description) {
        super(logger, id, name, description, listenersFactory, 1, 1, Arrays.<RealOptionImpl>asList());
        this.listenersFactory = listenersFactory;
    }

    public <SUB_FACTORY extends FACTORY> Entry<SUB_FACTORY> getFactoryEntry(String key, boolean autoCreate) {
        if(!factories.containsKey(key) && autoCreate)
            factories.put(key, new Entry<FACTORY>(listenersFactory));
        return (Entry<SUB_FACTORY>) factories.get(key);
    }

    public void factoryAvailable(String id, String name, String description, FACTORY factory) {
        getFactoryEntry(id, true).factoryAvailable(factory);
        options.add(new RealOptionImpl(logger, new Option.Data(id, name, description), listenersFactory));
    }

    public void factoryUnavailable(String id) {
        getFactoryEntry(id, true).factoryUnavailable();
        getOptions().remove(id);
    }

    @Override
    public Instance serialise(Entry<FACTORY> factory) {
        return factory == null ? null : new Instance(factories.inverse().get(factory));
    }

    @Override
    public Entry<FACTORY> deserialise(Instance value) {
        return value != null && value.getValue() != null ? factories.get(value.getValue()) : null;
    }

    public static class Entry<FACTORY> implements PluginResource<FACTORY> {

        private FACTORY factory;
        private final Listeners<Listener<FACTORY>> listeners;

        public Entry(ListenersFactory listenersFactory) {
            this.listeners = listenersFactory.create();
        }

        @Override
        public FACTORY getResource() {
            return factory;
        }

        @Override
        public ListenerRegistration addListener(Listener<FACTORY> listener) {
            return listeners.addListener(listener);
        }

        private void factoryAvailable(FACTORY factory) {
            this.factory = factory;
            for(Listener<FACTORY> listener : listeners)
                listener.resourceAvailable(factory);
        }

        private void factoryUnavailable() {
            this.factory = null;
            for(Listener<FACTORY> listener : listeners)
                listener.resourceUnavailable();
        }
    }
}
