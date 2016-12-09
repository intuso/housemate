package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.real.api.internal.annotations.Composite;
import com.intuso.housemate.client.real.api.internal.annotations.Regex;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.type.RegexType;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.RealSubTypeImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.ioc.Types;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by tomc on 16/05/16.
 */
public class TypeBuilder {

    private final Logger typesLogger;
    private final EnumChoiceType.Factory enumChoiceTypeFactory;
    private final RealRegexType.Factory regexTypeFactory;
    private final RealCompositeType.Factory compositeTypeFactory;

    @Inject
    public TypeBuilder(@Types Logger typesLogger, RealCompositeType.Factory compositeTypeFactory, EnumChoiceType.Factory enumChoiceTypeFactory, RealRegexType.Factory regexTypeFactory) {
        this.typesLogger = typesLogger;
        this.regexTypeFactory = regexTypeFactory;
        this.compositeTypeFactory = compositeTypeFactory;
        this.enumChoiceTypeFactory = enumChoiceTypeFactory;
    }

    public RealTypeImpl<?> buildType(Injector injector, Class<?> typeClass) {
        if(Enum.class.isAssignableFrom(typeClass)) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            return enumChoiceTypeFactory.create(ChildUtil.logger(typesLogger, id),
                    id,
                    name,
                    description,
                    typeClass);
        } else if(RegexType.class.isAssignableFrom(typeClass)) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            String regex = getRegex(typeClass);
            RegexType.Factory<?> factory = getRegexTypeFactory(injector, typeClass);
            return regexTypeFactory.create(ChildUtil.logger(typesLogger, id),
                    id,
                    name,
                    description,
                    regex,
                    factory);
        } else if(typeClass.getAnnotation(Composite.class) != null) {
            String id = getId(typeClass);
            String name = getName(typeClass, id);
            String description = getDescription(typeClass, name);
            return compositeTypeFactory.create(ChildUtil.logger(typesLogger, id),
                    id,
                    name,
                    description,
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
