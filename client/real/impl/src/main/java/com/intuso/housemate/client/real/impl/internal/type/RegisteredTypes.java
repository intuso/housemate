package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;
import com.intuso.housemate.client.real.impl.internal.type.ioc.TypeModule;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tomc on 13/05/16.
 */
public final class RegisteredTypes {

    private final TypeBuilder typeBuilder;
    private final Set<String> ids = Sets.newHashSet();
    private final Map<String, RealParameterImpl.Factory<?>> parameterFactories = Maps.newHashMap();
    private final Map<String, RealPropertyImpl.Factory<?>> propertyFactories = Maps.newHashMap();
    private final Map<String, RealValueImpl.Factory> valueFactories = Maps.newHashMap();

    @Inject
    public RegisteredTypes(TypeBuilder typeBuilder, Iterable<TypeFactories<?>> systemTypes) {
        this.typeBuilder = typeBuilder;
        for(TypeFactories typeFactories : systemTypes) {
            String typeId = typeFactories.getType().getId();
            if(ids.contains(typeId))
                throw new HousemateException("Duplicate type found when registering system type " + typeId);
            ids.add(typeId);
            parameterFactories.put(typeId, typeFactories.getParameterFactory());
            propertyFactories.put(typeId, typeFactories.getPropertyFactory());
            valueFactories.put(typeId, typeFactories.getValueFactory());
        }
    }

    public boolean exists(String id) {
        return ids.contains(id);
    }

    public synchronized <O> RealParameterImpl<O> createParameter(String typeId, Logger logger, String id, String name, String description, int minValues, int maxValues) {
        if(!exists(id))
            throw new HousemateException("Unknown type " + id);
        return (RealParameterImpl<O>) parameterFactories.get(typeId).create(logger, id, name, description, minValues, maxValues);
    }

    public synchronized <O> RealPropertyImpl<O> createProperty(String typeId, Logger logger, String id, String name, String description, int minValues, int maxValues, Iterable<O> values) {
        if(!exists(id))
            throw new HousemateException("Unknown type " + id);
        return (RealPropertyImpl<O>) propertyFactories.get(typeId).create(logger, id, name, description, minValues, maxValues, (List) values);
    }

    public synchronized <O> RealValueImpl<O> createValue(String typeId, Logger logger, String id, String name, String description, int minValues, int maxValues, Iterable<O> values) {
        if(!exists(id))
            throw new HousemateException("Unknown type " + id);
        return (RealValueImpl<O>) valueFactories.get(typeId).create(logger, id, name, description, minValues, maxValues, (List) values);
    }

    public synchronized void typeAvailable(Injector injector, Class<?> typeClass) {
        RealTypeImpl<?> type = typeBuilder.buildType(injector, typeClass);
        typeAvailable(injector, type, typeClass);
    }

    public synchronized void typeAvailable(Injector injector, RealTypeImpl<?> type, Type typeType) {
        Injector typeInjector = injector.createChildInjector(new TypeModule(type, typeType));
        ids.add(type.getId());
        parameterFactories.put(type.getId(), typeInjector.getInstance(new Key<RealParameterImpl.Factory<?>>() {}));
        propertyFactories.put(type.getId(), typeInjector.getInstance(new Key<RealPropertyImpl.Factory<?>>() {}));
        valueFactories.put(type.getId(), typeInjector.getInstance(new Key<RealValueImpl.Factory>() {}));
    }

    public synchronized void typeUnavailable(String id) {
        ids.remove(id);
        parameterFactories.remove(id);
        propertyFactories.remove(id);
        valueFactories.remove(id);
    }
}
