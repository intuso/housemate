package com.intuso.housemate.object.real.factory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;

/**
 * Created by tomc on 19/03/15.
 */
public class FactoryType<FACTORY> extends RealChoiceType<FACTORY> {

    private final BiMap<String, FACTORY> factories = HashBiMap.create();

    @Inject
    protected FactoryType(Log log, ListenersFactory listenersFactory, String id, String name, String description) {
        super(log, listenersFactory, id, name, description, 1, 1, Arrays.<RealOption>asList());
    }

    public void addFactory(String id, String name, String description, FACTORY factory) {
        factories.put(id, factory);
        getOptions().add(new RealOption(getLog(), getListenersFactory(), id, name, description));
    }

    public void removeFactory(String id) {
        factories.remove(id);
        getOptions().remove(id);
    }

    @Override
    public TypeInstance serialise(FACTORY factory) {
        return factory == null ? null : new TypeInstance(factories.inverse().get(factory));
    }

    @Override
    public FACTORY deserialise(TypeInstance value) {
        return value != null && value.getValue() != null ? factories.get(value.getValue()) : null;
    }
}
