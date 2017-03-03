package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.intuso.housemate.client.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.impl.internal.RealListGeneratedImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.Arrays;

/**
 * Created by tomc on 19/03/15.
 */
public class FactoryType<FACTORY> extends RealChoiceType<PluginDependency<FACTORY>> {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final RealOptionImpl.Factory optionFactory;
    private final BiMap<String, Entry<FACTORY>> factories = HashBiMap.create();

    protected FactoryType(Logger logger,
                          String id,
                          String name,
                          String description,
                          ManagedCollectionFactory managedCollectionFactory,
                          Sender.Factory senderFactory,
                          RealOptionImpl.Factory optionFactory,
                          RealListGeneratedImpl.Factory<RealOptionImpl> optionsFactory) {
        super(logger, id, name, description, Arrays.<RealOptionImpl>asList(), managedCollectionFactory, senderFactory, optionsFactory);
        this.managedCollectionFactory = managedCollectionFactory;
        this.optionFactory = optionFactory;
    }

    public <SUB_FACTORY extends FACTORY> Entry<SUB_FACTORY> getFactoryEntry(String key, boolean autoCreate) {
        if(!factories.containsKey(key) && autoCreate)
            factories.put(key, new Entry<FACTORY>(managedCollectionFactory));
        return (Entry<SUB_FACTORY>) factories.get(key);
    }

    public void addFactory(String id, String name, String description, FACTORY factory) {
        getFactoryEntry(id, true).factoryAvailable(factory);
        options.add(optionFactory.create(logger, id, name, description, Lists.<RealSubTypeImpl<?>>newArrayList()));
    }

    public void removeFactory(String id) {
        getFactoryEntry(id, true).factoryUnavailable();
        getOptions().remove(id);
    }

    @Override
    public Type.Instance serialise(PluginDependency<FACTORY> factory) {
        return factory == null ? null : new Type.Instance(factories.inverse().get(factory));
    }

    @Override
    public Entry<FACTORY> deserialise(Type.Instance value) {
        return value != null && value.getValue() != null ? getFactoryEntry(value.getValue(), true) : null;
    }

    public static class Entry<FACTORY> implements PluginDependency<FACTORY> {

        private FACTORY factory;
        private final ManagedCollection<Listener<FACTORY>> listeners;

        public Entry(ManagedCollectionFactory managedCollectionFactory) {
            this.listeners = managedCollectionFactory.create();
        }

        @Override
        public FACTORY getDependency() {
            return factory;
        }

        @Override
        public ManagedCollection.Registration addListener(Listener<FACTORY> listener) {
            return listeners.add(listener);
        }

        private void factoryAvailable(FACTORY factory) {
            this.factory = factory;
            for(Listener<FACTORY> listener : listeners)
                listener.dependencyAvailable(factory);
        }

        private void factoryUnavailable() {
            this.factory = null;
            for(Listener<FACTORY> listener : listeners)
                listener.dependencyUnavailable();
        }
    }
}
