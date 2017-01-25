package com.intuso.housemate.client.real.impl.internal.type;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.v1_0.api.type.TypeSpec;
import com.intuso.housemate.client.v1_0.api.type.serialiser.TypeSerialiser;
import com.intuso.housemate.client.v1_0.real.impl.type.BooleanType;
import com.intuso.housemate.client.v1_0.real.impl.type.ByteType;
import com.intuso.housemate.client.v1_0.real.impl.type.DoubleType;
import com.intuso.housemate.client.v1_0.real.impl.type.EmailType;
import com.intuso.housemate.client.v1_0.real.impl.type.IntegerType;
import com.intuso.housemate.client.v1_0.real.impl.type.StringType;

import java.util.Map;

/**
 * Created by tomc on 13/05/16.
 */
public final class TypeSerialisersV1_0Repository implements TypeSerialiser.Repository {

    private final Map<TypeSpec, TypeSerialiser<?>> serialisers = Maps.newHashMap();

    @Inject
    public TypeSerialisersV1_0Repository(// primitive types
                                         BooleanType booleanType,
                                         ByteType byteType,
                                         DoubleType doubleType,
                                         IntegerType integerType,
                                         StringType stringType,
                                         // regex types
                                         EmailType emailType) {
        serialiserAvailable(new TypeSpec(Boolean.class), booleanType);
        serialiserAvailable(new TypeSpec(boolean.class), booleanType);
        serialiserAvailable(new TypeSpec(Byte.class), byteType);
        serialiserAvailable(new TypeSpec(byte.class), byteType);
        serialiserAvailable(new TypeSpec(Double.class), doubleType);
        serialiserAvailable(new TypeSpec(double.class), doubleType);
        serialiserAvailable(new TypeSpec(Integer.class), integerType);
        serialiserAvailable(new TypeSpec(int.class), integerType);
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
