package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.real.impl.internal.RealListGeneratedImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by tomc on 13/05/16.
 */
public final class TypeRepository implements TypeSerialiser.Repository {

    private final RealListGeneratedImpl.Factory<RealTypeImpl<?>> typesFactory;
    private final List<RealListGeneratedImpl<RealTypeImpl<?>>> lists = Lists.newArrayList();
    private final Map<TypeSpec, RealTypeImpl<?>> types = Maps.newHashMap();

    @Inject
    public TypeRepository(RealListGeneratedImpl.Factory<RealTypeImpl<?>> typesFactory,
                          // primitive types
                          BooleanType booleanType,
                          DoubleType doubleType,
                          IntegerType integerType,
                          StringType stringType,
                          // regex types
                          EmailType emailType) {
        this.typesFactory = typesFactory;
        typeAvailable(booleanType);
        typeAvailable(doubleType);
        typeAvailable(integerType);
        typeAvailable(stringType);
        typeAvailable(emailType);
    }

    public RealListGeneratedImpl<RealTypeImpl<?>> createList(Logger logger, String id, String name, String description) {
        RealListGeneratedImpl<RealTypeImpl<?>> result = typesFactory.create(logger, id, name, description, types.values());
        lists.add(result);
        return result;
    }

    public <O> RealTypeImpl<O> getType(TypeSpec typeSpec) {
        if(!types.containsKey(typeSpec))
            throw new HousemateException("Unknown type: " + typeSpec.toString());
        return (RealTypeImpl<O>) types.get(typeSpec);
    }

    @Override
    public <O> TypeSerialiser<O> getSerialiser(TypeSpec typeSpec) {
        return (TypeSerialiser<O>) types.get(typeSpec);
    }

    public synchronized void typeAvailable(RealTypeImpl<?> typeImpl) {
        if(types.containsKey(typeImpl.getSpec()))
            throw new HousemateException("Duplicate type found when registering type " + typeImpl.getId());
        types.put(typeImpl.getSpec(), typeImpl);
        for(RealListGeneratedImpl<RealTypeImpl<?>> list : lists)
            list.add(typeImpl);
    }

    public synchronized void typeUnavailable(TypeSpec typeSpec) {
        if(types.containsKey(typeSpec)) {
            types.remove(typeSpec);
            for (RealListGeneratedImpl<RealTypeImpl<?>> list : lists)
                list.remove(typeSpec.toString());
        }
    }
}
