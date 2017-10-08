package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanObjectType;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanPrimitiveType;
import com.intuso.housemate.client.v1_0.real.impl.type.ByteObjectType;
import com.intuso.housemate.client.v1_0.real.impl.type.BytePrimitiveType;
import com.intuso.housemate.client.v1_0.real.impl.type.DoubleObjectType;
import com.intuso.housemate.client.v1_0.real.impl.type.DoublePrimitiveType;
import com.intuso.housemate.client.v1_0.real.impl.type.EmailType;
import com.intuso.housemate.client.v1_0.real.impl.type.IntegerObjectType;
import com.intuso.housemate.client.v1_0.real.impl.type.IntegerPrimitiveType;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;

import java.util.Map;

/**
 * Created by tomc on 13/05/16.
 */
public final class TypeSerialisersV1_0Repository implements TypeSerialiser.Repository {

    private final Map<TypeSpec, TypeSerialiser<?>> serialisers = Maps.newHashMap();

    @Inject
    public TypeSerialisersV1_0Repository(// primitive types
                                         BooleanObjectType booleanObjectType,
                                         BooleanPrimitiveType booleanPrimitiveType,
                                         ByteObjectType byteObjectType,
                                         BytePrimitiveType bytePrimitiveType,
                                         DoubleObjectType doubleObjectType,
                                         DoublePrimitiveType doublePrimitiveType,
                                         IntegerObjectType integerObjectType,
                                         IntegerPrimitiveType integerPrimitiveType,
                                         StringType stringType,
                                         // regex types
                                         EmailType emailType) {
        serialiserAvailable(new TypeSpec(Boolean.class), booleanObjectType);
        serialiserAvailable(new TypeSpec(boolean.class), booleanPrimitiveType);
        serialiserAvailable(new TypeSpec(Byte.class), byteObjectType);
        serialiserAvailable(new TypeSpec(byte.class), bytePrimitiveType);
        serialiserAvailable(new TypeSpec(Double.class), doubleObjectType);
        serialiserAvailable(new TypeSpec(double.class), doublePrimitiveType);
        serialiserAvailable(new TypeSpec(Integer.class), integerObjectType);
        serialiserAvailable(new TypeSpec(int.class), integerPrimitiveType);
        serialiserAvailable(new TypeSpec(String.class), stringType);
        serialiserAvailable(new TypeSpec(String.class, "email"), emailType);
    }

    @Override
    public <O> TypeSerialiser<O> getSerialiser(TypeSpec typeSpec) {
        return (TypeSerialiser<O>) serialisers.get(typeSpec);
    }

    public synchronized void serialiserAvailable(TypeSpec typeSpec, TypeSerialiser<?> serialiser) {
        if(serialisers.containsKey(typeSpec))
            throw new HousemateException("Duplicate type found when registering type serialiser for " + typeSpec);
        serialisers.put(typeSpec, serialiser);
    }

    public synchronized void serialiserUnavailable(TypeSpec typeSpec) {
        if(serialisers.containsKey(typeSpec))
            serialisers.remove(typeSpec);
    }
}
