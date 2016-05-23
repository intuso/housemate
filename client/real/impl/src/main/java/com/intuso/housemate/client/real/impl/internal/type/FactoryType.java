package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.plugin.api.internal.driver.PluginResource;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.Arrays;

/**
 * Created by tomc on 19/03/15.
 */
public class FactoryType<FACTORY> extends RealChoiceType<PluginResource<FACTORY>> {

    private final ListenersFactory listenersFactory;
    private final RealOptionImpl.Factory optionFactory;
    private final BiMap<String, Entry<FACTORY>> factories = HashBiMap.create();

    protected FactoryType(Logger logger, ListenersFactory listenersFactory, String id, String name, String description, RealOptionImpl.Factory optionFactory) {
        super(logger, id, name, description, listenersFactory, Arrays.<RealOptionImpl>asList());
        this.listenersFactory = listenersFactory;
        this.optionFactory = optionFactory;
    }

    public <SUB_FACTORY extends FACTORY> Entry<SUB_FACTORY> getFactoryEntry(String key, boolean autoCreate) {
        if(!factories.containsKey(key) && autoCreate)
            factories.put(key, new Entry<FACTORY>(listenersFactory));
        return (Entry<SUB_FACTORY>) factories.get(key);
    }

    public void factoryAvailable(String id, String name, String description, FACTORY factory) {
        getFactoryEntry(id, true).factoryAvailable(factory);
        options.add(optionFactory.create(logger, id, name, description, Lists.<RealSubTypeImpl<?>>newArrayList()));
    }

    public void factoryUnavailable(String id) {
        getFactoryEntry(id, true).factoryUnavailable();
        getOptions().remove(id);
    }

    @Override
    public Type.Instance serialise(PluginResource<FACTORY> factory) {
        return factory == null ? null : new Type.Instance(factories.inverse().get(factory));
    }

    @Override
    public Entry<FACTORY> deserialise(Type.Instance value) {
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
