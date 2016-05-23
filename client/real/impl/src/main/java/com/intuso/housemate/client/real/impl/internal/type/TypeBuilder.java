package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.TypeSerialiser;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.ioc.Types;
import com.intuso.housemate.plugin.api.internal.annotations.Composite;
import com.intuso.housemate.plugin.api.internal.annotations.Regex;
import com.intuso.housemate.plugin.api.internal.annotations.TypeInfo;
import com.intuso.housemate.plugin.api.internal.type.RegexType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tomc on 16/05/16.
 */
public class TypeBuilder {

    private final static Set<? extends Class<?>> SIMPLE_TYPE_CLASSES = Sets.newHashSet(Boolean.class, Double.class, Integer.class, String.class);
    private final static Map<Class<?>, Type.Simple> SIMPLE_TYPES = Maps.newHashMap();
    {
        SIMPLE_TYPES.put(Boolean.class, Type.Simple.Boolean);
        SIMPLE_TYPES.put(Double.class, Type.Simple.Double);
        SIMPLE_TYPES.put(Integer.class, Type.Simple.Integer);
        SIMPLE_TYPES.put(String.class, Type.Simple.String);
    }

    private final static Map<Class<?>, TypeSerialiser<?>> SIMPLE_SERIALISERS = Maps.newHashMap();
    {
        SIMPLE_SERIALISERS.put(Boolean.class, new TypeSerialiser<Boolean>() {
            @Override
            public Type.Instance serialise(Boolean b) {
                return b != null ? new Type.Instance(b.toString()) : null;
            }

            @Override
            public Boolean deserialise(Type.Instance value) {
                return value != null && value.getValue() != null ? Boolean.valueOf(value.getValue()) : null;
            }
        });
        SIMPLE_SERIALISERS.put(Double.class, new TypeSerialiser<Double>() {
            @Override
            public Type.Instance serialise(Double d) {
                return d != null ? new Type.Instance(d.toString()) : null;
            }

            @Override
            public Double deserialise(Type.Instance value) {
                return value != null && value.getValue() != null ? Double.valueOf(value.getValue()) : null;
            }
        });
        SIMPLE_SERIALISERS.put(Integer.class, new TypeSerialiser<Integer>() {
            @Override
            public Type.Instance serialise(Integer i) {
                return i != null ? new Type.Instance(i.toString()) : null;
            }

            @Override
            public Integer deserialise(Type.Instance value) {
                return value != null && value.getValue() != null ? Integer.valueOf(value.getValue()) : null;
            }
        });
        SIMPLE_SERIALISERS.put(String.class, new TypeSerialiser<String>() {
            @Override
            public Type.Instance serialise(String s) {
                return s != null ? new Type.Instance(s) : null;
            }

            @Override
            public String deserialise(Type.Instance value) {
                return value != null ? value.getValue() : null;
            }
        });
    }

    private final Logger typesLogger;
    private final ListenersFactory listenersFactory;
    private final RealOptionImpl.Factory optionFactory;

    @Inject
    public TypeBuilder(@Types Logger typesLogger, ListenersFactory listenersFactory, RealOptionImpl.Factory optionFactory) {
        this.typesLogger = typesLogger;
        this.listenersFactory = listenersFactory;
        this.optionFactory = optionFactory;
    }

    public RealTypeImpl<?> buildType(Injector injector, Class<?> typeClass) {
        if(SIMPLE_TYPE_CLASSES.contains(typeClass)) {
            Type.Simple simpleType = SIMPLE_TYPES.get(typeClass);
            return new RealSimpleType<>(ChildUtil.logger(typesLogger, simpleType.getId()),
                    simpleType,
                    SIMPLE_SERIALISERS.get(typeClass),
                    listenersFactory);
        } else if(Enum.class.isAssignableFrom(typeClass)) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            return new EnumChoiceType<>(ChildUtil.logger(typesLogger, id),
                    id, name, description, listenersFactory,
                    (Class<Enum>) typeClass,
                    optionFactory);
        } else if(RegexType.class.isAssignableFrom(typeClass)) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            String regex = getRegex(typeClass);
            RegexType.Factory<?> factory = getRegexTypeFactory(injector, typeClass);
            return new RealRegexType<>(ChildUtil.logger(typesLogger, id),
                    id,
                    name,
                    description,
                    listenersFactory,
                    regex,
                    factory);
        } else if(typeClass.getAnnotation(Composite.class) != null) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            return new RealCompositeType<>(ChildUtil.logger(typesLogger, id),
                    id,
                    name,
                    description,
                    listenersFactory,
                    parseSubTypes(typeClass));
        }
        throw new HousemateException("Unable to determine type of typeClass " + typeClass.getName());
    }

    private String getId(Class<?> typeClass) {
        TypeInfo typeInfo = typeClass.getAnnotation(TypeInfo.class);
        if(typeInfo == null)
            throw new HousemateException(typeClass.getName() + " is missing " + TypeInfo.class + " annotation");
        return typeInfo.id();
    }

    private String getName(Class<?> typeClass, String id) {
        TypeInfo typeInfo = typeClass.getAnnotation(TypeInfo.class);
        if(typeInfo == null)
            throw new HousemateException(typeClass.getName() + " is missing " + TypeInfo.class + " annotation");
        return typeInfo.name().length() == 0 ? id : typeInfo.name();
    }

    private String getDescription(Class<?> typeClass, String name) {
        TypeInfo typeInfo = typeClass.getAnnotation(TypeInfo.class);
        if(typeInfo == null)
            throw new HousemateException(typeClass.getName() + " is missing " + TypeInfo.class + " annotation");
        return typeInfo.description().length() == 0 ? name : typeInfo.description();
    }

    private String getRegex(Class<?> typeClass) {
        Regex regex = typeClass.getAnnotation(Regex.class);
        if(regex == null)
            throw new HousemateException(typeClass.getName() + " is missing " + TypeInfo.class + " annotation");
        return regex.regex();
    }

    private RegexType.Factory<?> getRegexTypeFactory(Injector injector, Class<?> typeClass) {
        Regex regex = typeClass.getAnnotation(Regex.class);
        if(regex == null)
            throw new HousemateException(typeClass.getName() + " is missing " + TypeInfo.class + " annotation");
        return injector.getInstance(regex.factory());
    }

    private List<RealSubTypeImpl<?>> parseSubTypes(Class<?> typeClass) {
        return Lists.newArrayList(); // todo
    }
}
