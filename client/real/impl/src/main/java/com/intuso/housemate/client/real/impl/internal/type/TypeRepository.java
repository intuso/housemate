package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.util.Types;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.driver.*;
import com.intuso.housemate.client.api.internal.type.TypeSpec;
import com.intuso.housemate.client.api.internal.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;

import java.util.Map;

/**
 * Created by tomc on 13/05/16.
 */
public final class TypeRepository implements TypeSerialiser.Repository {

    private final Map<TypeSpec, RealTypeImpl<?>> types = Maps.newHashMap();

    @Inject
    public TypeRepository(// primitive types
                          BooleanType booleanType,
                          DoubleType doubleType,
                          IntegerType integerType,
                          StringType stringType,
                          // regex types
                          EmailType emailType,
                          // factory types
                          ConditionDriverType conditionDriverType,
                          FeatureDriverType featureDriverType,
                          HardwareDriverType hardwareDriverType,
                          TaskDriverType taskDriverType) {
        typeAvailable(new TypeSpec(Boolean.class), booleanType);
        typeAvailable(new TypeSpec(boolean.class), booleanType);
        typeAvailable(new TypeSpec(Double.class), doubleType);
        typeAvailable(new TypeSpec(double.class), doubleType);
        typeAvailable(new TypeSpec(Integer.class), integerType);
        typeAvailable(new TypeSpec(int.class), integerType);
        typeAvailable(new TypeSpec(String.class), stringType);
        typeAvailable(new TypeSpec(String.class, "email"), emailType);
        typeAvailable(new TypeSpec(Types.newParameterizedType(PluginDependency.class, ConditionDriver.class)), conditionDriverType);
        typeAvailable(new TypeSpec(Types.newParameterizedType(PluginDependency.class, FeatureDriver.class)), featureDriverType);
        typeAvailable(new TypeSpec(Types.newParameterizedType(PluginDependency.class, HardwareDriver.class)), hardwareDriverType);
        typeAvailable(new TypeSpec(Types.newParameterizedType(PluginDependency.class, TaskDriver.class)), taskDriverType);
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

    public synchronized void typeAvailable(TypeSpec typeSpec, RealTypeImpl<?> typeImpl) {
        if(types.containsKey(typeSpec))
            throw new HousemateException("Duplicate type found when registering type " + typeImpl.getId());
        types.put(typeSpec, typeImpl);
    }

    public synchronized void typeUnavailable(TypeSpec typeSpec) {
        if(types.containsKey(typeSpec))
            types.remove(typeSpec);
    }
}
