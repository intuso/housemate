package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.impl.internal.*;
import com.intuso.housemate.client.real.impl.internal.type.ioc.TypeModule;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by tomc on 13/05/16.
 */
public final class RegisteredTypes {

    private final TypeBuilder typeBuilder;
    private final RealListImpl.Factory<RealTypeImpl<?>> typesFactory;
    private final List<RealListImpl<RealTypeImpl<?>>> lists = Lists.newArrayList();
    private final Map<String, RealTypeImpl<?>> types = Maps.newHashMap();
    private final Map<String, RealParameterImpl.Factory<?>> parameterFactories = Maps.newHashMap();
    private final Map<String, RealPropertyImpl.Factory<?>> propertyFactories = Maps.newHashMap();
    private final Map<String, RealValueImpl.Factory> valueFactories = Maps.newHashMap();

    @Inject
    public RegisteredTypes(TypeBuilder typeBuilder, RealListImpl.Factory<RealTypeImpl<?>> typesFactory, Iterable<TypeFactories<?>> systemTypes) {
        this.typeBuilder = typeBuilder;
        this.typesFactory = typesFactory;
        for(TypeFactories typeFactories : systemTypes) {
            String typeId = typeFactories.getType().getId();
            if(types.containsKey(typeId))
                throw new HousemateException("Duplicate type found when registering system type " + typeId);
            types.put(typeId, typeFactories.getType());
            parameterFactories.put(typeId, typeFactories.getParameterFactory());
            propertyFactories.put(typeId, typeFactories.getPropertyFactory());
            valueFactories.put(typeId, typeFactories.getValueFactory());
        }
    }

    public RealListImpl<RealTypeImpl<?>> createList(Logger logger, String id, String name, String description) {
        RealListImpl<RealTypeImpl<?>> result = typesFactory.create(logger, id, name, description, types.values());
        lists.add(result);
        return result;
    }

    public boolean exists(String id) {
        return types.containsKey(id);
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
        if(types.containsKey(type.getId()))
            throw new HousemateException("Duplicate type found when registering type " + type.getId());
        Injector typeInjector = injector.createChildInjector(new TypeModule(type, typeType));
        for(RealListImpl<RealTypeImpl<?>> list : lists)
            list.add(type);
        types.put(type.getId(), type);
        parameterFactories.put(type.getId(), typeInjector.getInstance(new Key<RealParameterImpl.Factory<?>>() {}));
        propertyFactories.put(type.getId(), typeInjector.getInstance(new Key<RealPropertyImpl.Factory<?>>() {}));
        valueFactories.put(type.getId(), typeInjector.getInstance(new Key<RealValueImpl.Factory>() {}));
    }

    public synchronized void typeUnavailable(String id) {
        for(RealListImpl<RealTypeImpl<?>> list : lists)
            list.remove(id);
        types.remove(id);
        parameterFactories.remove(id);
        propertyFactories.remove(id);
        valueFactories.remove(id);
    }
}
